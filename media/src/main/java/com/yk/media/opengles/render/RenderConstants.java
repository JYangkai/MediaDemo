package com.yk.media.opengles.render;

public interface RenderConstants {
    /**
     * 处理类型
     */
    interface Process {
        /**
         * 相机
         */
        int CAMERA = 0;

        /**
         * 视频
         */
        int VIDEO = 1;

        /**
         * 图像
         */
        int IMAGE = 2;
    }

    /**
     * 滤镜
     */
    interface Filter {
        /**
         * 高斯模糊
         */
        int GAUSSIAN_BLUR = 0;
    }
}
