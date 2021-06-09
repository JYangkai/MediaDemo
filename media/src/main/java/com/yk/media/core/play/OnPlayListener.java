package com.yk.media.core.play;

import com.yk.media.data.base.Size;

public interface OnPlayListener {
    /**
     * 开始播放
     */
    void onStartPlay(Size videoSize, long duration);

    /**
     * 暂停播放
     */
    void onPausePlay(long currentPos);

    /**
     * 停止播放
     */
    void onStopPlay();

    /**
     * 播放完成
     */
    void onPlayCompletion();

    /**
     * 播放错误
     */
    void onErrorPlay(Exception e);
}
