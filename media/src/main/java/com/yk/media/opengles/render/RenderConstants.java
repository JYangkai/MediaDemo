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

        /**
         * 拍照
         */
        int TAKE_PHOTO = 3;

        /**
         * 录像
         */
        int RECORD_VIDEO = 4;
    }

    /**
     * 滤镜
     */
    interface Filter {
        /**
         * 原画
         */
        int NORMAL = -1;

        /**
         * 高斯模糊
         */
        int GAUSSIAN_BLUR = 0;

        /**
         * 灰度滤镜
         */
        int GRAY = 1;

        /**
         * 画中画
         */
        int PIP = 2;

        /**
         * 运动模糊
         */
        int MOTION_BLUR = 3;
    }

    /**
     * 转场特效
     */
    interface Transition {
        /**
         * 普通
         */
        int NORMAL = -1;

        /**
         * 混合
         */
        int MIX = 0;

        /**
         * 模糊
         */
        int BLUR = 1;
    }
}
