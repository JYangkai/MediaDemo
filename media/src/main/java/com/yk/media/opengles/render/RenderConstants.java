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
         * 均值模糊
         */
        int MEAN_BLUR = 0;

        /**
         * 高斯模糊
         */
        int GAUSSIAN_BLUR = 1;

        /**
         * 灰度滤镜
         */
        int GRAY = 2;

        /**
         * 画中画
         */
        int PIP = 3;

        /**
         * 运动模糊
         */
        int MOTION_BLUR = 4;

        /**
         * 缩放
         */
        int SCALE = 5;

        /**
         * 扭曲
         */
        int SKEW = 6;
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

        /**
         * 推镜
         */
        int PUSH = 2;

        /**
         * 拉镜
         */
        int PULL = 3;

        /**
         * 旋涡
         */
        int VORTEX = 4;

        /**
         * 左移
         */
        int LEFT_MOVE = 5;

        /**
         * 右移
         */
        int RIGHT_MOVE = 6;

        /**
         * 上移
         */
        int TOP_MOVE = 7;

        /**
         * 下移
         */
        int DOWN_MOVE = 8;

        /**
         * 翻页
         */
        int PAGE_UP = 9;
    }
}
