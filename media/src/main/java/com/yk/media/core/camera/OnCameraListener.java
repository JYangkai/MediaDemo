package com.yk.media.core.camera;

import com.yk.media.data.base.Size;

public interface OnCameraListener {
    /**
     * 相机打开
     */
    void onCameraOpened(Size cameraSize, int facing);

    /**
     * 相机关闭
     */
    void onCameraClosed();

    /**
     * 相机异常
     */
    void onCameraError(Exception e);
}
