package com.yk.media.core.video;

public interface OnRecordListener {
    /**
     * 开始录制回调
     */
    void onRecordStart();

    /**
     * 录制时长回调
     */
    void onRecordTime(long time);

    /**
     * 录制完成回调
     */
    void onRecordComplete(String path);

    /**
     * 录制取消回调
     */
    void onRecordCancel();

    /**
     * 录制错误回调
     */
    void onRecordError(Exception e);
}
