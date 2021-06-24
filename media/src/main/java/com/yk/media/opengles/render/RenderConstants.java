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

        /**
         * 蓝线挑战（横向）
         */
        int BLUE_LINE_CHALLENGE_H = 7;

        /**
         * 蓝线挑战（纵向）
         */
        int BLUE_LINE_CHALLENGE_V = 8;

        /**
         * 保留帧
         */
        int RETAIN_FRAME = 9;
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
         * 左上移
         */
        int LEFT_TOP_MOVE = 9;

        /**
         * 右上移
         */
        int RIGHT_TOP_MOVE = 10;

        /**
         * 左下移
         */
        int LEFT_DOWN_MOVE = 11;

        /**
         * 右下移
         */
        int RIGHT_DOWN_MOVE = 12;

        /**
         * 翻页
         */
        int PAGE_UP = 13;

        /**
         * 分割一
         */
        int CUT_1 = 14;

        /**
         * 分割二
         */
        int CUT_2 = 15;

        /**
         * 分割三
         */
        int CUT_3 = 16;

        /**
         * 分割四
         */
        int CUT_4 = 17;
    }
}
