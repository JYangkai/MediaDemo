package com.yk.media.core.pcm;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.text.TextUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PcmPlayer {
    private PlayThread playThread;

    public void start(String path) {
        stop();
        playThread = new PlayThread(path,
                AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioTrack.MODE_STREAM);
        playThread.start();
    }

    public void stop() {
        if (playThread == null) {
            return;
        }
        playThread.stopPlay();
        playThread = null;
    }


    private static class PlayThread extends Thread {
        /**
         * pcm播放组件
         */
        private AudioTrack audioTrack;

        /**
         * 文件输入
         */
        private FileInputStream fis;
        private final String path;

        /**
         * 音频流格式（一般使用music）
         */
        private final int streamType;

        /**
         * 采样率
         */
        private final int sampleRateInHz;

        /**
         * 声道设置
         */
        private final int channelConfig;

        /**
         * 编码格式
         */
        private final int audioFormat;

        /**
         * 播放模式（一般使用流模式）
         */
        private final int mod;

        /**
         * 音频缓存大小
         */
        private int bufferSizeInBytes;

        /**
         * 是否停止播放
         */
        private boolean isStopPlay = false;

        /**
         * 构造方法（传入必要的参数）
         */
        public PlayThread(String path,
                          int streamType,
                          int sampleRateInHz,
                          int channelConfig,
                          int audioFormat,
                          int mod
        ) {
            this.path = path;
            this.streamType = streamType;
            this.sampleRateInHz = sampleRateInHz;
            this.channelConfig = channelConfig;
            this.audioFormat = audioFormat;
            this.mod = mod;
        }

        @Override
        public void run() {
            super.run();
            initIo();
            initAudioTrack();
            play();
        }

        /**
         * 初始化IO
         */
        private void initIo() {
            if (TextUtils.isEmpty(path)) {
                return;
            }

            try {
                fis = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fis = null;
            }
        }

        /**
         * 初始化pcm播放组件
         */
        private void initAudioTrack() {
            bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
            audioTrack = new AudioTrack(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mod);
        }

        /**
         * 开始播放
         */
        private void play() {
            if (audioTrack == null || fis == null) {
                return;
            }

            byte[] data = new byte[bufferSizeInBytes];

            audioTrack.play();

            for (; ; ) {
                if (isStopPlay) {
                    release();
                    break;
                }

                int readSize = -1;

                try {
                    readSize = fis.read(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (readSize <= 0) {
                    isStopPlay = true;
                    continue;
                }

                audioTrack.write(data, 0, readSize);
            }
        }

        /**
         * 停止播放
         */
        void stopPlay() {
            isStopPlay = true;
            try {
                join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 释放资源
         */
        private void release() {
            if (audioTrack != null) {
                audioTrack.stop();
                audioTrack.release();
                audioTrack = null;
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
