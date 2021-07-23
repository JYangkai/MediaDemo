package com.yk.media.core.pcm;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PcmEncoder {
    private EncodeThread encodeThread;

    public void start(String path) {
        stop();
        encodeThread = new EncodeThread(
                path,
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                96000,
                4096,
                MediaFormat.MIMETYPE_AUDIO_AAC
        );
        encodeThread.start();
    }

    public void stop() {
        if (encodeThread == null) {
            return;
        }
        encodeThread.stopEncode();
        encodeThread = null;
    }

    private static class EncodeThread extends Thread {
        private static final long TIMEOUT_MS = 2000L;

        private AudioRecord audioRecord;

        private MediaCodec mediaCodec;

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
         * 比特率
         */
        private final int bitRate;

        /**
         * 最大输入size
         */
        private final int maxInputSize;

        /**
         * 编码类型
         */
        private final String mine;

        /**
         * 声道数
         */
        private int channelCount;

        /**
         * 音频缓存buffer
         */
        private int bufferSizeInByte;

        /**
         * 是否停止编码
         */
        private boolean isStopEncode = false;

        /**
         * 构造方法（传入必要的参数）
         */
        public EncodeThread(String path,
                            int audioSource,
                            int sampleRateInHz,
                            int channelConfig,
                            int audioFormat,
                            int bitRate,
                            int maxInputSize,
                            String mime
        ) {
            this.path = path;
            this.audioSource = audioSource;
            this.sampleRateInHz = sampleRateInHz;
            this.channelConfig = channelConfig;
            this.audioFormat = audioFormat;
            this.bitRate = bitRate;
            this.maxInputSize = maxInputSize;
            this.mine = mime;
        }

        @Override
        public void run() {
            super.run();
            initIo();
            initAudioRecord();
            initMediaCodec();
            encode();
        }

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

        private void initAudioRecord() {
            bufferSizeInByte = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
            audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInByte);
        }

        private void initMediaCodec() {
            channelCount = 1;
            if (channelConfig == AudioFormat.CHANNEL_IN_MONO) {
                channelCount = 1;
            } else if (channelConfig == AudioFormat.CHANNEL_IN_STEREO) {
                channelCount = 2;
            }

            MediaFormat format = MediaFormat.createAudioFormat(
                    mine, sampleRateInHz, channelCount);
            format.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
            format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, maxInputSize);
            try {
                mediaCodec = MediaCodec.createEncoderByType(mine);
                mediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            } catch (IOException e) {
                e.printStackTrace();
                mediaCodec = null;
            }
        }

        private void encode() {
            if (audioRecord == null || fos == null || mediaCodec == null) {
                return;
            }

            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            audioRecord.startRecording();
            mediaCodec.start();

            for (; ; ) {
                if (isStopEncode) {
                    release();
                    break;
                }

                int inputBufferId = mediaCodec.dequeueInputBuffer(TIMEOUT_MS);
                if (inputBufferId >= 0) {
                    ByteBuffer inputBuffer = mediaCodec.getInputBuffer(inputBufferId);
                    int readSize = -1;
                    if (inputBuffer != null) {
                        readSize = audioRecord.read(inputBuffer, bufferSizeInByte);
                    }
                    if (readSize <= 0) {
                        mediaCodec.queueInputBuffer(
                                inputBufferId,
                                0,
                                0,
                                0,
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        isStopEncode = true;
                    } else {
                        mediaCodec.queueInputBuffer(
                                inputBufferId,
                                0,
                                readSize,
                                System.nanoTime() / 1000,
                                0);
                    }
                }

                int outputBufferId = mediaCodec.dequeueOutputBuffer(info, TIMEOUT_MS);
                if (outputBufferId >= 0) {
                    ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(outputBufferId);
                    int size = info.size;
                    if (outputBuffer != null && size > 0) {
                        byte[] data = new byte[size + 7];
                        addADTSHeader(data, size + 7);
                        outputBuffer.get(data, 7, size);
                        outputBuffer.clear();

                        try {
                            fos.write(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mediaCodec.releaseOutputBuffer(outputBufferId, false);
                    }
                }
            }
        }

        void stopEncode() {
            isStopEncode = true;
            try {
                join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void release() {
            if (audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
            }

            if (mediaCodec != null) {
                mediaCodec.stop();
                mediaCodec.release();
                mediaCodec = null;
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

        /**
         * 添加AAC帧文件头
         *
         * @param packet    packet
         * @param packetLen packetLen
         */
        private void addADTSHeader(byte[] packet, int packetLen) {
            int profile = 2; // AAC
            int freqIdx = 4; // 44.1kHz
            packet[0] = (byte) 0xFF;
            packet[1] = (byte) 0xF9;
            packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (channelCount >> 2));
            packet[3] = (byte) (((channelCount & 3) << 6) + (packetLen >> 11));
            packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
            packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
            packet[6] = (byte) 0xFC;
        }
    }

}
