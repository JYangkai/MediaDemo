package com.yk.media.utils;

import android.hardware.Camera;
import android.util.Log;

public class YuvUtils {
    private static final String TAG = "YuvUtils2";

    /**
     * nv21转换nv12
     */
    public static byte[] cameraNv21ToNv12(
            byte[] data,
            int width,
            int height,
            int facing,
            int orientation) {
        byte[] outputData;
        Log.d(TAG, "cameraNv21ToNv12: " + orientation + " facing:" + facing);
        int rotate = orientation;
        int w = width;
        int h = height;
        if (facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotate = 360 - orientation;
        }
        switch (rotate) {
            case 90:
                // 经过旋转，宽高互换
                w = height;
                h = width;
                outputData = nv21ToNv12AndRotate90(data, width, height);
                break;
            case 180:
                // 经过旋转，宽高不变
                outputData = nv21ToNv12AndRotate180(data, width, height);
                break;
            case 270:
                // 经过旋转，宽高互换
                w = height;
                h = width;
                outputData = nv21ToNv12AndRotate270(data, width, height);
                break;
            default:
                outputData = data;
                break;
        }

        return cameraNv21ToNv12WidthFacing(outputData, w, h, facing);
    }

    /**
     * 通过facing获取nv12数据
     */
    private static byte[] cameraNv21ToNv12WidthFacing(
            byte[] data,
            int width,
            int height,
            int facing) {
        if (facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // 前摄是镜像的，所有需要做一次镜像处理
            return yuvMirror(data, width, height);
        }
        return data;
    }

    /**
     * nv21转nv12，并且旋转90度
     */
    private static byte[] nv21ToNv12AndRotate90(byte[] inputData, int width, int height) {
        if (inputData == null) {
            return null;
        }
        int size = inputData.length;
        if (size != (width * height * 3 / 2)) {
            return null;
        }

        byte[] outputData = new byte[size];
        int k = 0;

        for (int i = 0; i < width; i++) {
            for (int j = height - 1; j >= 0; j--) {
                outputData[k++] = inputData[width * j + i];
            }
        }

        int start = width * height;
        for (int i = 0; i < width; i += 2) {
            for (int j = height / 2 - 1; j >= 0; j--) {
                outputData[k++] = inputData[start + width * j + i + 1];
                outputData[k++] = inputData[start + width * j + i];
            }
        }

        return outputData;
    }

    /**
     * nv21转nv12，并且旋转180度
     */
    private static byte[] nv21ToNv12AndRotate180(byte[] inputData, int width, int height) {
        if (inputData == null) {
            return null;
        }
        int size = inputData.length;
        if (size != (width * height * 3 / 2)) {
            return null;
        }

        byte[] outputData = new byte[size];
        int k = 0;

        for (int i = height - 1; i >= 0; i--) {
            for (int j = width - 1; j >= 0; j--) {
                outputData[k++] = inputData[width * i + j];
            }
        }

        int start = width * height;
        for (int i = height / 2 - 1; i >= 0; i--) {
            for (int j = width - 1; j >= 0; j -= 2) {
                outputData[k++] = inputData[start + width * i + j];
                outputData[k++] = inputData[start + width * i + j - 1];
            }
        }

        return outputData;
    }

    /**
     * nv21转nv12，并且旋转270度
     */
    private static byte[] nv21ToNv12AndRotate270(byte[] inputData, int width, int height) {
        if (inputData == null) {
            return null;
        }
        int size = inputData.length;
        if (size != (width * height * 3 / 2)) {
            return null;
        }

        byte[] outputData = new byte[size];
        int k = 0;

        for (int i = width - 1; i >= 0; i--) {
            for (int j = 0; j < height; j++) {
                outputData[k++] = inputData[width * j + i];
            }
        }

        int start = width * height;
        for (int i = width - 1; i >= 0; i -= 2) {
            for (int j = 0; j < height / 2; j++) {
                outputData[k++] = inputData[start + width * j + i];
                outputData[k++] = inputData[start + width * j + i - 1];
            }
        }

        return outputData;
    }

    /**
     * 镜像处理
     */
    private static byte[] yuvMirror(byte[] inputData, int width, int height) {
        if (inputData == null) {
            return null;
        }
        int size = inputData.length;

        byte[] outputData = new byte[size];
        int k = 0;

        for (int i = 0; i < height; i++) {
            for (int j = width - 1; j >= 0; j--) {
                outputData[k++] = inputData[width * i + j];
            }
        }

        int start = width * height;
        for (int i = 0; i < height / 2; i++) {
            for (int j = width - 1; j >= 0; j -= 2) {
                outputData[k++] = inputData[start + width * i + j - 1];
                outputData[k++] = inputData[start + width * i + j];
            }
        }

        return outputData;
    }

}
