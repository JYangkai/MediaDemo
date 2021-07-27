package com.yk.media.utils;

import android.util.Log;

public class YuvUtils {
    private static final String TAG = "YuvUtils";

    public static void nv21ToNv12(byte[] data, int width, int height) {
        if (data == null) {
            return;
        }
        int size = data.length;
        if (size != (width * height * 3 / 2)) {
            return;
        }

        Log.d(TAG, "nv21ToNv12: ");

        int start = width * height;

        for (int i = start; i + 1 < size; i += 2) {
            byte temp = data[i];
            data[i] = data[i + 1];
            data[i + 1] = temp;
        }
    }

    public static void rotateAndToNV12(byte[] nv21, byte[] nv12, int width, int height) {
        int wh = width * height;
        int k = 0;
        for (int i = 0; i < width; i++) {
            for (int j = height - 1; j >= 0; j--) {
                nv12[k] = nv21[width * j + i];
                k++;
            }
        }

        for (int i = 0; i < width; i += 2) {
            for (int j = height / 2 - 1; j >= 0; j--) {
                nv12[k] = nv21[wh + width * j + i + 1];
                nv12[k + 1] = nv21[wh + width * j + i];
                k += 2;
            }
        }
    }

}
