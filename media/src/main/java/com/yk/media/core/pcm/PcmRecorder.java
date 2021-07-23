package com.yk.media.core.pcm;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PcmRecorder {
    private RecordThread recordThread;

    public void start(String path) {
        stop();
        recordThread = new RecordThread(path,
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        recordThread.start();
    }

    public void stop() {
        if (recordThread == null) {
            return;
        }
        recordThread.stopRecord();
        recordThread = null;
    }

    private static class RecordThread extends Thread {
        /**
         * pcm录制组件
         */
        private AudioRecord audioRecord;

        /**
         * 文件输出
         */
        private FileOutputStream fos;
        private final String path;

        /**
         * 声源（一般是来自麦克风）
         */
        private final int audioSource;

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
         * 音频缓存buffer
         */
        private int bufferSizeInByte;

        /**
         * 是否停止录制
         */
        private boolean isStopRecord = false;

        /**
         * 构造方法（传入必要的参数）
         */
        public RecordThread(String path,
                            int audioSource,
                            int sampleRateInHz,
                            int channelConfig,
                            int audioFormat
        ) {
            this.path = path;
            this.audioSource = audioSource;
            this.sampleRateInHz = sampleRateInHz;
            this.channelConfig = channelConfig;
            this.audioFormat = audioFormat;
        }

        @Override
        public void run() {
            super.run();
            initIo();
            initAudioRecord();
            record();
        }

        /**
         * 初始化IO
         */
        private void initIo() {
            if (TextUtils.isEmpty(path)) {
                return;
            }

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }

            try {
                fos = new FileOutputStream(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fos = null;
            }
        }

        /**
         * 初始化pcm录制组件
         */
        private void initAudioRecord() {
            bufferSizeInByte = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
            audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInByte);
        }

        /**
         * 开始录制
         */
        private void record() {
            if (audioRecord == null || fos == null) {
                return;
            }

            byte[] data = new byte[bufferSizeInByte];

            audioRecord.startRecording();

            for (; ; ) {
                if (isStopRecord) {
                    release();
                    break;
                }

                int readSize = audioRecord.read(data, 0, bufferSizeInByte);
                if (readSize <= 0) {
                    isStopRecord = true;
                    continue;
                }

                try {
                    fos.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 停止录制（外部调用）
         */
        void stopRecord() {
            isStopRecord = true;
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
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
            }

            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
