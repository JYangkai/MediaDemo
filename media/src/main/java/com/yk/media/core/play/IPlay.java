package com.yk.media.core.play;

import android.graphics.SurfaceTexture;

import com.yk.media.data.base.Size;

public interface IPlay {
    /**
     * 继续播放
     */
    void continuePlay();

    /**
     * 音频播放
     */
    void play(String path);

    /**
     * 音频播放
     */
    void play(String path, boolean isLoop);

    /**
     * 视频播放
     */
    void play(String path, SurfaceTexture surfaceTexture);

    /**
     * 播放（音频 or 视频）
     */
    void play(String path, SurfaceTexture surfaceTexture, boolean isLoop);

    /**
     * 跳转到指定位置
     */
    void seekTo(int time);

    /**
     * 暂停
     */
    void pause();

    /**
     * 停止
     */
    void stop();

    /**
     * 获取当前总时长
     */
    long getDuration();

    /**
     * 获取当前时长
     */
    long getCurrentPosition();

    /**
     * 是否正在播放
     */
    boolean isPlaying();

    /**
     * 获取视频尺寸
     */
    Size getVideoSize();

    /**
     * 添加播放回调
     */
    void addOnPlayListener(OnPlayListener onPlayListener);

    /**
     * 移除播放回调
     */
    void removeOnPlayListener(OnPlayListener onPlayListener);

    /**
     * 移除全部的播放回调
     */
    void removeAllOnPlayListener();
}
