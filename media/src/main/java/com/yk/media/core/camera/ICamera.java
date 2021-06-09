package com.yk.media.core.camera;

import android.app.Activity;
import android.graphics.SurfaceTexture;

import com.yk.media.data.base.Size;

public interface ICamera {
    /**
     * 打开相机
     */
    void openCamera(Activity activity, SurfaceTexture surfaceTexture);

    /**
     * 打开相机
     */
    void openCamera(int facing, Activity activity, SurfaceTexture surfaceTexture);

    /**
     * 关闭相机
     */
    void closeCamera();

    /**
     * 切换相机
     */
    void switchCamera();

    /**
     * 切换相机
     */
    void switchCamera(int facing);

    /**
     * 设置Facing
     */
    void setCameraFacing(int facing);

    /**
     * 获取Facing
     */
    int getCameraFacing();

    /**
     * 设置预览尺寸
     */
    void setPreviewSize(Size cameraSize);

    /**
     * 获取预览尺寸
     */
    Size getPreviewSize();

    /**
     * 设置显示旋转角度
     */
    void setDisplayOrientation(int displayOrientation);

    /**
     * 获取显示旋转角度
     */
    int getDisplayOrientation();

    /**
     * 释放相机
     */
    void releaseCamera();

    /**
     * 添加相机回调
     */
    void addOnCameraListener(OnCameraListener onCameraListener);

    /**
     * 移除相机回调
     */
    void removeOnCameraListener(OnCameraListener onCameraListener);

    /**
     * 移除所有回调
     */
    void removeAllOnCameraListener();
}
