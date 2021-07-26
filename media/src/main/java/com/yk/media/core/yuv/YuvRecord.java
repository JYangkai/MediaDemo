package com.yk.media.core.yuv;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.text.TextUtils;
import android.util.Log;

import com.yk.media.core.camera.CameraUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class YuvRecord {
    private static final String TAG = "YuvRecord";

    private RecordThread recordThread;

    public void start(String path, Activity activity) {
        stop();
        recordThread = new RecordThread(
                path,
                activity,
                Camera.CameraInfo.CAMERA_FACING_BACK
        );
        recordThread.start();
    }

    public void stop() {
        if (recordThread == null) {
            return;
        }
        recordThread.stopRecord();
        recordThread = null;
    }


    private static class RecordThread extends Thread implements Camera.PreviewCallback {
        private Camera camera;

        private final String path;
        private FileOutputStream fos;

        private final Activity activity;

        private final int id;

        private int width;
        private int height;

        private byte[] nv21Data;

        private boolean isStopRecord = false;

        public RecordThread(String path, Activity activity, int id) {
            this.path = path;
            this.activity = activity;
            this.id = id;
        }

        @Override
        public void run() {
            super.run();
            initIo();
            initCamera();
            record();
        }

        private void initIo() {
            if (TextUtils.isEmpty(path)) {
                return;
            }

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }

            try {
                fos = new FileOutputStream(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fos = null;
            }
        }

        private void initCamera() {
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
            nv21Data = new byte[width * height * 3 / 2];
            parameters.setPreviewSize(width, height);
            parameters.setPreviewFormat(ImageFormat.NV21);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(CameraUtils.getDisplayOrientation(activity, id));
            camera.addCallbackBuffer(nv21Data);
            camera.setPreviewCallbackWithBuffer(this);
            try {
                camera.setPreviewTexture(new SurfaceTexture(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }

        private void record() {
            for (; ; ) {
                if (isStopRecord) {
                    release();
                    return;
                }
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
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }

            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.d(TAG, "onPreviewFrame: " + nv21Data.length);
            camera.addCallbackBuffer(nv21Data);

            if (fos == null) {
                return;
            }
            try {
                fos.write(nv21Data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
