package com.yk.media.core.yuv;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.yk.media.core.camera.CameraUtils;
import com.yk.media.utils.YuvUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class YuvEncoder extends MediaCodec.Callback implements Camera.PreviewCallback {
    private static final String TAG = "YuvEncoder";

    private Camera camera;
    private byte[] data;
    private int width;
    private int height;

    private MediaCodec mediaCodec;

    private MediaMuxer mediaMuxer;
    private int trackIndex = -1;

    private Handler handler;
    private HandlerThread thread;

    private final BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>(10);

    public YuvEncoder() {
        thread = new HandlerThread("yuv_encoder");
        thread.start();
        handler = new Handler(thread.getLooper());
    }

    public void start(Activity activity, SurfaceTexture surfaceTexture, String path) {
        stop();
        handler.post(new Runnable() {
            @Override
            public void run() {
                initMediaMuxer(path);
                openCamera(activity, surfaceTexture, Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
        });
    }

    public void stop() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                closeCamera();
                stopMediaMuxer();
                stopMediaCodec();
                queue.clear();
            }
        });
    }

    private void initMediaMuxer(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        try {
            mediaMuxer = new MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
            mediaMuxer = null;
        }
    }

    private void stopMediaMuxer() {
        if (mediaMuxer == null) {
            return;
        }
        mediaMuxer.stop();
        mediaMuxer.release();
        mediaMuxer = null;
    }

    private void openCamera(Activity activity, SurfaceTexture surfaceTexture, int id) {
        camera = Camera.open(id);
        if (camera == null) {
            return;
        }
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        Camera.Size size = CameraUtils.findTheBestSize(previewSizeList,
                activity.getResources().getDisplayMetrics().widthPixels,
                activity.getResources().getDisplayMetrics().heightPixels);
        width = size.width;
        height = size.height;
        data = new byte[width * height * 3 / 2];

        initMediaCodec();

        parameters.setPreviewSize(width, height);
        parameters.setPreviewFormat(ImageFormat.NV21);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(CameraUtils.getDisplayOrientation(activity, id));
        camera.addCallbackBuffer(data);
        camera.setPreviewCallbackWithBuffer(this);
        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    private void closeCamera() {
        if (camera == null) {
            return;
        }
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private void initMediaCodec() {
        int width = this.height;
        int height = this.width;
        try {
            MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
            format.setInteger(MediaFormat.KEY_BIT_RATE,
                    width * height * 4);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

            //设置压缩等级  默认是baseline
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                format.setInteger(MediaFormat.KEY_PROFILE, MediaCodecInfo.CodecProfileLevel.AVCProfileMain);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    format.setInteger(MediaFormat.KEY_LEVEL, MediaCodecInfo.CodecProfileLevel.AVCLevel3);
                }
            }

            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            mediaCodec.setCallback(this);
            mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopMediaCodec() {
        if (mediaCodec == null) {
            return;
        }
        mediaCodec.stop();
        mediaCodec.release();
        mediaCodec = null;
    }

    public void release() {
        stop();
        thread.quitSafely();
        thread = null;
        handler = null;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (this.data == null) {
            this.data = new byte[width * height * 3 / 2];
        }
        camera.addCallbackBuffer(this.data);

        Log.d(TAG, "onPreviewFrame: queue offer:" + this.data.length);

//        YuvUtils.nv21ToNv12(this.data, width, height);

        byte[] nv12 = new byte[this.data.length];
        YuvUtils.rotateAndToNV12(this.data, nv12, width, height);

        queue.offer(nv12);
    }

    @Override
    public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
        ByteBuffer buffer = codec.getInputBuffer(index);
        buffer.clear();

        int size = 0;
        byte[] data = queue.poll();
        if (data != null) {
            Log.d(TAG, "onInputBufferAvailable: queue poll:" + data.length);
            buffer.put(data);
            size = data.length;
        }
        codec.queueInputBuffer(index, 0, size, System.nanoTime() / 1000, 0);
    }

    @Override
    public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {
        ByteBuffer buffer = codec.getOutputBuffer(index);
        if (buffer != null && info.size > 0) {
            if (mediaMuxer != null && trackIndex != -1) {
                Log.d(TAG, "onOutputBufferAvailable: write to media muxer:" + info.size);
                mediaMuxer.writeSampleData(trackIndex, buffer, info);
            }
            buffer.clear();
        }
        codec.releaseOutputBuffer(index, false);
    }

    @Override
    public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {
        Log.e(TAG, "onError: ", e);
    }

    @Override
    public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {
        if (trackIndex != -1) {
            return;
        }
        if (mediaMuxer == null) {
            return;
        }
        trackIndex = mediaMuxer.addTrack(format);
        mediaMuxer.start();
        Log.d(TAG, "onOutputFormatChanged: start media muxer");
    }
}
