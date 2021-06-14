package com.yk.media.core.record.video.params;

/**
 * Mic参数
 */
public class MicParam {
    /**
     * 音频输入源
     */
    private int audioSource = -1;

    /**
     * 采样率
     */
    private int sampleRateInHz = -1;

    /**
     * 声道格式
     */
    private int channelConfig = -1;

    /**
     * 音频格式
     */
    private int audioFormat = -1;

    /**
     * 音频流类型
     */
    private int streamType = -1;

    /**
     * 音频流形式
     */
    private int mode = -1;

    private MicParam() {
    }

    public int getAudioSource() {
        return audioSource;
    }

    public void setAudioSource(int audioSource) {
        this.audioSource = audioSource;
    }

    public int getSampleRateInHz() {
        return sampleRateInHz;
    }

    public void setSampleRateInHz(int sampleRateInHz) {
        this.sampleRateInHz = sampleRateInHz;
    }

    public int getChannelConfig() {
        return channelConfig;
    }

    public void setChannelConfig(int channelConfig) {
        this.channelConfig = channelConfig;
    }

    public int getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(int audioFormat) {
        this.audioFormat = audioFormat;
    }

    public int getStreamType() {
        return streamType;
    }

    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public static class Builder {
        private MicParam micParam;

        public Builder() {
            micParam = new MicParam();
        }

        public Builder setAudioSource(int audioSource) {
            micParam.setAudioSource(audioSource);
            return this;
        }

        public Builder setSampleRateInHz(int sampleRateInHz) {
            micParam.setSampleRateInHz(sampleRateInHz);
            return this;
        }

        public Builder setChannelConfig(int channelConfig) {
            micParam.setChannelConfig(channelConfig);
            return this;
        }

        public Builder setAudioFormat(int audioFormat) {
            micParam.setAudioFormat(audioFormat);
            return this;
        }

        public Builder setStreamType(int streamType) {
            micParam.setStreamType(streamType);
            return this;
        }

        public Builder setMode(int mode) {
            micParam.setMode(mode);
            return this;
        }

        public MicParam build() {
            return micParam;
        }
    }

    public boolean isEmpty() {
        return sampleRateInHz == -1
                || channelConfig == -1
                || audioFormat == -1;
    }
}
