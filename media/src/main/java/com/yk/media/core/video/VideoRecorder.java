package com.yk.media.core.video;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.view.Surface;

import com.yk.media.core.video.params.AudioEncodeParam;
import com.yk.media.core.video.params.CameraParam;
import com.yk.media.core.video.params.MicParam;
import com.yk.media.core.video.params.RecordParam;
import com.yk.media.core.video.params.VideoEncodeParam;
import com.yk.media.opengles.egl.EglHelper;
import com.yk.media.opengles.view.CameraView;
import com.yk.media.opengles.view.base.EGLTextureView;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLContext;

public class VideoRecorder {
    private RecordThread recordThread;
    private GLThread glThread;

    private CameraParam cameraParam;
    private VideoEncodeParam videoEncodeParam;

    private MicParam micParam;
    private AudioEncodeParam audioEncodeParam;

    private RecordParam recordParam;

    private Surface surface;

    private OnRecordListener onRecordListener;

    private CameraView cameraView;

    public void attachCameraView(CameraView cameraView) {
        this.cameraView = cameraView;
    }

    public void startWithDefaultParams(String path) {
        if (cameraView == null) {
            return;
        }
        startWithDefaultParams(
                cameraView.getContext(),
                cameraView.getEglContext(),
                cameraView.getFboTextureId(),
                cameraView.getCameraManager().getCameraFacing(),
                cameraView.getCameraManager().getPreviewSize().getHeight(),
                cameraView.getCameraManager().getPreviewSize().getWidth(),
                path
        );
    }

    public void startWithDefaultParams(Context context, EGLContext eglContext, int textureId,
                                       int facing, int width, int height, String path) {
        MicParam micParam = new MicParam.Builder()
                .setAudioSource(android.media.MediaRecorder.AudioSource.MIC)
                .setSampleRateInHz(44100)
                .setChannelConfig(AudioFormat.CHANNEL_IN_STEREO)
                .setAudioFormat(AudioFormat.ENCODING_PCM_16BIT)
                .build();

        AudioEncodeParam audioEncodeParam = new AudioEncodeParam.Builder()
                .setBitRate(96000)
                .setMaxInputSize(4096)
                .setMime(MediaFormat.MIMETYPE_AUDIO_AAC)
                .build();

        VideoRender render = new VideoRender(context);
        render.setTextureId(textureId);
        CameraParam cameraParam = new CameraParam.Builder()
                .setFacing(facing)
                .setEGLContext(eglContext)
                .setRenderer(render)
                .setRenderMode(EGLTextureView.RENDERMODE_CONTINUOUSLY)
                .build();

        VideoEncodeParam videoEncodeParam = new VideoEncodeParam.Builder()
                .setFrameRate(30)
                .setSize(width, height)
                .setIFrameInterval(1)
                .setMime(MediaFormat.MIMETYPE_VIDEO_AVC)
                .build();

        RecordParam recordParam = new RecordParam.Builder()
                .setPath(path)
                .build();

        start(cameraParam, videoEncodeParam, micParam, audioEncodeParam, recordParam);
    }

    public void start(CameraParam cameraParam, VideoEncodeParam videoEncodeParam,
                      MicParam micParam, AudioEncodeParam audioEncodeParam,
                      RecordParam recordParam) {
        this.cameraParam = cameraParam;
        this.videoEncodeParam = videoEncodeParam;
        this.micParam = micParam;
        this.audioEncodeParam = audioEncodeParam;
        this.recordParam = recordParam;

        cancel();

        recordThread = new RecordThread();
        recordThread.init();

        glThread = new GLThread();
        glThread.isCreate = true;
        glThread.isChange = true;

        recordThread.start();
        glThread.start();
    }

    public void cancel() {
        if (recordThread != null) {
            recordThread.cancelRecord();
            recordThread = null;
        }
        if (glThread != null) {
            glThread.onDestroy();
            glThread = null;
        }
    }

    public void stop() {
        stopRecordThread();
        stopGLThread();
    }

    private void stopRecordThread() {
        if (recordThread != null) {
            recordThread.stopRecord();
            recordThread = null;
        }
    }

    private void stopGLThread() {
        if (glThread != null) {
            glThread.onDestroy();
            glThread = null;
        }
    }

    public void setOnRecordListener(OnRecordListener onRecordListener) {
        this.onRecordListener = onRecordListener;
    }

    private void onRecordStart() {
        if (onRecordListener != null) {
            onRecordListener.onRecordStart();
        }
    }

    private void onRecordTime(long time) {
        if (onRecordListener != null) {
            onRecordListener.onRecordTime(time);
        }
    }

    private void onRecordComplete(String path) {
        stopGLThread();
        if (onRecordListener != null) {
            onRecordListener.onRecordComplete(path);
        }
    }

    private void onRecordCancel() {
        if (onRecordListener != null) {
            onRecordListener.onRecordCancel();
        }
    }

    private void onRecordError(Exception e) {
        if (onRecordListener != null) {
            onRecordListener.onRecordError(e);
        }
    }

    private class RecordThread extends Thread {
        private MediaMuxer mediaMuxer;

        private MediaCodec audioCodec;
        private MediaCodec videoCodec;

        private AudioRecord audioRecord;

        private int bufferSizeInBytes;

        private boolean isStopRecord = false;
        private boolean isCancelRecord = false;

        void init() {
            initMuxer();
            initAudio();
            initVideo();
        }

        private void initMuxer() {
            try {
                mediaMuxer = new MediaMuxer(recordParam.getPath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            } catch (IOException e) {
                e.printStackTrace();
                mediaMuxer = null;
            }
        }

        private void initAudio() {
            bufferSizeInBytes = AudioRecord.getMinBufferSize(micParam.getSampleRateInHz(),
                    micParam.getChannelConfig(), micParam.getAudioFormat());
            audioRecord = new AudioRecord(micParam.getAudioSource(), micParam.getSampleRateInHz(),
                    micParam.getChannelConfig(), micParam.getAudioFormat(), bufferSizeInBytes);
            try {
                audioCodec = MediaCodec.createEncoderByType(audioEncodeParam.getMime());
                MediaFormat format = MediaFormat.createAudioFormat(audioEncodeParam.getMime(), micParam.getSampleRateInHz(), 2);
                format.setInteger(MediaFormat.KEY_BIT_RATE, audioEncodeParam.getBitRate());
                format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
                format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, audioEncodeParam.getMaxInputSize());
                audioCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            } catch (IOException e) {
                e.printStackTrace();
                audioRecord = null;
                audioCodec = null;
            }
        }

        private void initVideo() {
            try {
                videoCodec = MediaCodec.createEncoderByType(videoEncodeParam.getMime());
                MediaFormat format = MediaFormat.createVideoFormat(videoEncodeParam.getMime(),
                        videoEncodeParam.getWidth(), videoEncodeParam.getHeight());
                format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
                format.setInteger(MediaFormat.KEY_FRAME_RATE, videoEncodeParam.getFrameRate());
                format.setInteger(MediaFormat.KEY_BIT_RATE,
                        videoEncodeParam.getWidth() * videoEncodeParam.getHeight() * 4);
                format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, videoEncodeParam.getiFrameInterval());

                //设置压缩等级  默认是baseline
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    format.setInteger(MediaFormat.KEY_PROFILE, MediaCodecInfo.CodecProfileLevel.AVCProfileMain);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        format.setInteger(MediaFormat.KEY_LEVEL, MediaCodecInfo.CodecProfileLevel.AVCLevel3);
                    }
                }

                videoCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
                surface = videoCodec.createInputSurface();
            } catch (IOException e) {
                e.printStackTrace();
                videoCodec = null;
                surface = null;
            }
        }

        void cancelRecord() {
            isCancelRecord = true;
            try {
                join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        void stopRecord() {
            isStopRecord = true;
            try {
                join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void release() {
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
            }

            if (audioCodec != null) {
                audioCodec.stop();
                audioCodec.release();
                audioCodec = null;
            }

            if (videoCodec != null) {
                videoCodec.stop();
                videoCodec.release();
                videoCodec = null;
            }

            if (mediaMuxer != null) {
                mediaMuxer.stop();
                mediaMuxer.release();
                mediaMuxer = null;
            }

            if (isCancelRecord) {
                onRecordCancel();
                return;
            }

            if (isStopRecord) {
                onRecordComplete(recordParam.getPath());
            }
        }

        @Override
        public void run() {
            super.run();
            record();
        }

        private void record() {
            if (mediaMuxer == null || audioCodec == null || videoCodec == null) {
                onRecordError(new IllegalArgumentException("widget is null"));
                return;
            }

            boolean isStartMuxer = false; // 合成是否开始
            isStopRecord = false;

            long audioPts = 0;
            long videoPts = 0;

            int audioTrackIndex = -1;
            int videoTrackIndex = -1;

            onRecordStart();

            audioRecord.startRecording();
            audioCodec.start();
            videoCodec.start();

            MediaCodec.BufferInfo audioInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo videoInfo = new MediaCodec.BufferInfo();

            while (true) {
                if (isStopRecord || isCancelRecord) {
                    release();
                    break;
                }

                // 将AudioRecord录制的PCM原始数据送入编码器
                int audioInputBufferId = audioCodec.dequeueInputBuffer(0);
                if (audioInputBufferId >= 0) {
                    ByteBuffer inputBuffer = audioCodec.getInputBuffer(audioInputBufferId);
                    int readSize = -1;
                    if (inputBuffer != null) {
                        readSize = audioRecord.read(inputBuffer, bufferSizeInBytes);
                    }
                    if (readSize >= 0) {
                        audioCodec.queueInputBuffer(audioInputBufferId, 0, readSize, System.nanoTime() / 1000, 0);
                    }
                }

                // 获取从surface直接编码得到的数据，写入Muxer
                int videoOutputBufferId = videoCodec.dequeueOutputBuffer(videoInfo, 0);
                if (videoOutputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    videoTrackIndex = mediaMuxer.addTrack(videoCodec.getOutputFormat());
                    if (audioTrackIndex != -1 && !isStartMuxer) {
                        isStartMuxer = true;
                        mediaMuxer.start();
                    }
                } else if (videoOutputBufferId >= 0) {
                    ByteBuffer outputBuffer = videoCodec.getOutputBuffer(videoOutputBufferId);
                    if (outputBuffer != null && videoInfo.size != 0 && isStartMuxer) {
                        outputBuffer.position(videoInfo.offset);
                        outputBuffer.limit(videoInfo.offset + videoInfo.size);
                        if (videoPts == 0) {
                            videoPts = videoInfo.presentationTimeUs;
                        }
                        videoInfo.presentationTimeUs = videoInfo.presentationTimeUs - videoPts;
                        mediaMuxer.writeSampleData(videoTrackIndex, outputBuffer, videoInfo);
                    }
                    videoCodec.releaseOutputBuffer(videoOutputBufferId, false);
                }

                // 获取音频编码数据，写入Muxer
                int audioOutputBufferId = audioCodec.dequeueOutputBuffer(audioInfo, 0);
                if (audioOutputBufferId == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    audioTrackIndex = mediaMuxer.addTrack(audioCodec.getOutputFormat());
                    if (videoTrackIndex != -1 && !isStartMuxer) {
                        isStartMuxer = true;
                        mediaMuxer.start();
                    }
                } else if (audioOutputBufferId >= 0) {
                    ByteBuffer outputBuffer = audioCodec.getOutputBuffer(audioOutputBufferId);
                    if (outputBuffer != null && audioInfo.size != 0 && isStartMuxer) {
                        outputBuffer.position(audioInfo.offset);
                        outputBuffer.limit(audioInfo.offset + audioInfo.size);
                        if (audioPts == 0) {
                            audioPts = audioInfo.presentationTimeUs;
                        }
                        audioInfo.presentationTimeUs = audioInfo.presentationTimeUs - audioPts;
                        mediaMuxer.writeSampleData(audioTrackIndex, outputBuffer, audioInfo);
                    }
                    audioCodec.releaseOutputBuffer(audioOutputBufferId, false);
                }

                onRecordTime(videoInfo.presentationTimeUs);
            }
        }
    }

    /**
     * 渲染线程
     */
    private class GLThread extends Thread {
        private EglHelper eglHelper;

        private boolean isCreate;
        private boolean isChange;
        private boolean isStart;
        private boolean isExit;

        private final Object object = new Object();

        private ArrayList<Runnable> mEventQueue = new ArrayList<>();

        @Override
        public void run() {
            if (checkState()) {
                return;
            }
            try {
                guardedRun();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 检查参数状态
         *
         * @return 是否初始化完成
         */
        private boolean checkState() {
            return surface == null;
        }

        private void guardedRun() throws InterruptedException {
            isExit = false;
            isStart = false;
            eglHelper = new EglHelper();
            eglHelper.init(cameraParam.getEglContext(), surface);
            Runnable event = null;
            while (true) {
                if (isExit) {
                    release();
                    break;
                }
                if (isStart) {
                    if (cameraParam.getRenderMode() == EGLTextureView.RENDERMODE_WHEN_DIRTY) {
                        synchronized (object) {
                            object.wait();
                        }
                    } else if (cameraParam.getRenderMode() == EGLTextureView.RENDERMODE_CONTINUOUSLY) {
                        Thread.sleep(1000 / 60);
                    } else {
                        throw new IllegalArgumentException("render mode error");
                    }
                }

                if (!mEventQueue.isEmpty()) {
                    event = mEventQueue.remove(0);
                }

                if (event != null) {
                    event.run();
                    event = null;
                    continue;
                }

                onCreate();
                onChange(videoEncodeParam.getWidth(), videoEncodeParam.getHeight());
                onDrawFrame();
                isStart = true;
            }
        }

        private void onCreate() {
            if (!isCreate || cameraParam.getRenderer() == null) {
                return;
            }
            isCreate = false;
            cameraParam.getRenderer().onCreate();
        }

        private void onChange(int width, int height) {
            if (!isChange || cameraParam.getRenderer() == null) {
                return;
            }
            isChange = false;
            cameraParam.getRenderer().onChange(width, height);
        }

        private void onDrawFrame() {
            if (cameraParam.getRenderer() == null) {
                return;
            }
            cameraParam.getRenderer().onDraw();
            if (!isStart) {
                cameraParam.getRenderer().onDraw();
            }
            eglHelper.swapBuffers();
        }

        void requestRender() {
            if (object != null) {
                synchronized (object) {
                    object.notifyAll();
                }
            }
        }

        void onDestroy() {
            isExit = true;
            requestRender();
        }

        void release() {
            if (eglHelper != null) {
                eglHelper.destoryEgl();
                eglHelper = null;
            }
        }

        EGLContext getEGLContext() {
            if (eglHelper != null) {
                return eglHelper.getEglContext();
            }
            return null;
        }

        void queueEvent(Runnable r) {
            if (r == null) {
                return;
            }
            if (object != null) {
                synchronized (object) {
                    mEventQueue.add(r);
                    object.notifyAll();
                }
            }
        }
    }
}
