package com.yk.media.core.camera;

import android.app.Activity;
import android.hardware.Camera;
import android.view.Surface;

public class CameraUtils {
    /**
     * 获取摄像头旋转角度
     */
    public static int getDisplayOrientation(Activity activity, int facing) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(facing, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }
}
