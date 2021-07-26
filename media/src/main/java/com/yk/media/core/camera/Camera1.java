package com.yk.media.core.camera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import com.yk.media.data.base.Size;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Camera1 implements ICamera {
    /**
     * Camera
     */
    private Camera camera;

    /**
     * 预览尺寸
     */
    private Size cameraSize = new Size(-1, -1);

    /**
     * 默认摄像头方向
     */
    private int facing = CameraConstants.facing.BACK;

    /**
     * 旋转角度
     */
    private int displayOrientation = -1;

    /**
     * 相机回调
     */
    private final List<OnCameraListener> onCameraListenerList = new ArrayList<>();

    @Override
    public void openCamera(Activity activity, SurfaceTexture surfaceTexture) {
        openCamera(facing, activity, surfaceTexture);
    }

    @Override
    public void openCamera(int facing, Activity activity, SurfaceTexture surfaceTexture) {
        // 先关闭相机
        closeCamera();

        // 判断是否存在摄像头
        int cameraNum = Camera.getNumberOfCameras();
        if (cameraNum <= 0) {
            onCameraError(new IllegalStateException("camera num <= 0"));
            return;
        }

        // 检查传入的facing
        int cameraIndex = -1;
        if (facing == CameraConstants.facing.BACK) {
            cameraIndex = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else if (facing == CameraConstants.facing.FRONT) {
            cameraIndex = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        if (cameraIndex == -1) {
            onCameraError(new IllegalStateException("camera facing exception"));
            return;
        }

        // 判断摄像头个数，以决定使用哪个打开方式
        if (cameraNum >= 2) {
            camera = Camera.open(cameraIndex);
        } else {
            camera = Camera.open();
        }

        // 判断Camera是否初始化成功
        if (camera == null) {
            onCameraError(new IllegalStateException("camera is null"));
            return;
        }

        this.facing = facing;

        try {
            // 获取摄像头参数
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
            if (cameraSize.getWidth() <= 0 || cameraSize.getHeight() <= 0) {
                Camera.Size size = CameraUtils.findTheBestSize(previewSizeList,
                        activity.getResources().getDisplayMetrics().widthPixels,
                        activity.getResources().getDisplayMetrics().heightPixels);
                cameraSize.setWidth(size.width);
                cameraSize.setHeight(size.height);
            }

            // 设置预览尺寸
            parameters.setPreviewSize(cameraSize.getWidth(), cameraSize.getHeight());

            // 这里设置使用的对焦模式
            if (this.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }

            // 设置摄像头参数
            camera.setParameters(parameters);
            camera.setPreviewTexture(surfaceTexture);
            if (displayOrientation < 0) {
                displayOrientation = CameraUtils.getDisplayOrientation(activity, cameraIndex);
            }
            camera.setDisplayOrientation(displayOrientation);
            camera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    onCameraOpened(cameraSize, Camera1.this.facing);
                }
            });
            camera.startPreview();
        } catch (IOException e) {
            onCameraError(e);
        }
    }

    @Override
    public void closeCamera() {
        if (camera == null) {
            return;
        }
        camera.stopPreview();
        camera.release();
        camera = null;
        onCameraClosed();
    }

    @Override
    public void switchCamera() {
        if (facing == CameraConstants.facing.BACK) {
            facing = CameraConstants.facing.FRONT;
        } else {
            facing = CameraConstants.facing.BACK;
        }
    }

    @Override
    public void switchCamera(int facing) {
        this.facing = facing;
    }

    @Override
    public void setCameraFacing(int facing) {
        this.facing = facing;
    }

    @Override
    public int getCameraFacing() {
        return facing;
    }

    @Override
    public void setPreviewSize(Size cameraSize) {
        this.cameraSize = cameraSize;
    }

    @Override
    public Size getPreviewSize() {
        return cameraSize;
    }

    @Override
    public void setDisplayOrientation(int displayOrientation) {
        this.displayOrientation = displayOrientation;
    }

    @Override
    public int getDisplayOrientation() {
        return displayOrientation;
    }

    @Override
    public void releaseCamera() {
        closeCamera();
        removeAllOnCameraListener();
    }

    @Override
    public void addOnCameraListener(OnCameraListener onCameraListener) {
        onCameraListenerList.add(onCameraListener);
    }

    @Override
    public void removeOnCameraListener(OnCameraListener onCameraListener) {
        onCameraListenerList.remove(onCameraListener);
    }

    @Override
    public void removeAllOnCameraListener() {
        onCameraListenerList.clear();
    }

    /**
     * 相机打开回调
     */
    private void onCameraOpened(Size cameraSize, int facing) {
        if (onCameraListenerList.isEmpty()) {
            return;
        }
        for (OnCameraListener listener : onCameraListenerList) {
            listener.onCameraOpened(cameraSize, facing);
        }
    }

    /**
     * 相机关闭回调
     */
    private void onCameraClosed() {
        if (onCameraListenerList.isEmpty()) {
            return;
        }
        for (OnCameraListener listener : onCameraListenerList) {
            listener.onCameraClosed();
        }
    }

    /**
     * 相机错误回调
     */
    private void onCameraError(Exception error) {
        if (onCameraListenerList.isEmpty()) {
            return;
        }
        for (OnCameraListener listener : onCameraListenerList) {
            listener.onCameraError(error);
        }
    }
}
