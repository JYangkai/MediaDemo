package com.yk.media.core.video.params;

import android.text.TextUtils;

/**
 * 音频编码参数
 */
public class AudioEncodeParam {
    /**
     * 比特率
     */
    private int bitRate = -1;

    /**
     * 最大输入数据大小
     */
    private int maxInputSize = -1;

    /**
     * 编码格式
     */
    private String mime;

    private AudioEncodeParam() {
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getMaxInputSize() {
        return maxInputSize;
    }

    public void setMaxInputSize(int maxInputSize) {
        this.maxInputSize = maxInputSize;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public static class Builder {
        private AudioEncodeParam audioEncodeParam;

        public Builder() {
            audioEncodeParam = new AudioEncodeParam();
        }

        public Builder setBitRate(int bitRate) {
            audioEncodeParam.setBitRate(bitRate);
            return this;
        }

        public Builder setMaxInputSize(int maxInputSize) {
            audioEncodeParam.setMaxInputSize(maxInputSize);
            return this;
        }

        public Builder setMime(String mime) {
            audioEncodeParam.setMime(mime);
            return this;
        }

        public AudioEncodeParam build() {
            return audioEncodeParam;
        }
    }

    public boolean isEmpty() {
        return bitRate == -1 || maxInputSize == -1 ||
                TextUtils.isEmpty(mime);
    }
}
