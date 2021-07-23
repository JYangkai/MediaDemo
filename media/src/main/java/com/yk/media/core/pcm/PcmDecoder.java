package com.yk.media.core.pcm;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

public class PcmDecoder {
    private static final String TAG = "PcmDecoder";

    private DecodeThread decodeThread;

    public void start(String path) {
        stop();
        decodeThread = new DecodeThread(
                path,
                AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioTrack.MODE_STREAM
        );
        decodeThread.start();
    }

    public void stop() {
        if (decodeThread == null) {
            return;
        }
        decodeThread.stopDecode();
        decodeThread = null;
    }

    private static class DecodeThread extends Thread {
        private static final long TIMEOUT_MS = 2000L;

        private MediaExtractor mediaExtractor;

        private MediaCodec mediaCodec;

        private AudioTrack audioTrack;

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
         * 音频格式
         */
        private MediaFormat format;

        private String mime;

        /**
         * 是否停止解码
         */
        private boolean isStopDecode = false;

        /**
         * 构造方法（传入必要的参数）
         */
        public DecodeThread(String path,
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
            initMediaExtractor();
            initMediaCodec();
            initAudioTrack();
            decode();
        }

        private void initMediaExtractor() {
            if (TextUtils.isEmpty(path)) {
                return;
            }

            try {
                mediaExtractor = new MediaExtractor();
                mediaExtractor.setDataSource(path);

                int trackCount = mediaExtractor.getTrackCount();
                for (int i = 0; i < trackCount; i++) {
                    MediaFormat format = mediaExtractor.getTrackFormat(i);
                    String mime = format.getString(MediaFormat.KEY_MIME);
                    if (!TextUtils.isEmpty(mime) && mime.startsWith("audio/")) {
                        mediaExtractor.selectTrack(i);
                        this.format = format;
                        this.mime = mime;
                        break;
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "initMediaExtractor: ", e);
                mediaExtractor = null;
                format = null;
                mime = null;
            }
        }

        private void initMediaCodec() {
            if (TextUtils.isEmpty(mime) || format == null) {
                return;
            }
            try {
                mediaCodec = MediaCodec.createDecoderByType(mime);
                mediaCodec.configure(format, null, null, 0);
            } catch (IOException e) {
                e.printStackTrace();
                mediaCodec = null;
            }
        }

        private void initAudioTrack() {
            bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
            audioTrack = new AudioTrack(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mod);
        }

        private void decode() {
            if (mediaExtractor == null || mediaCodec == null || audioTrack == null) {
                return;
            }

            long startMs = System.currentTimeMillis();
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            mediaCodec.start();
            audioTrack.play();

            for (; ; ) {
                if (isStopDecode) {
                    release();
                    break;
                }

                int inputBufferId = mediaCodec.dequeueInputBuffer(TIMEOUT_MS);
                if (inputBufferId >= 0) {
                    ByteBuffer inputBuffer = mediaCodec.getInputBuffer(inputBufferId);
                    int readSize = -1;
                    if (inputBuffer != null) {
                        readSize = mediaExtractor.readSampleData(inputBuffer, 0);
                    }

                    if (readSize <= 0) {
                        mediaCodec.queueInputBuffer(
                                inputBufferId,
                                0,
                                0,
                                0,
                                MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        isStopDecode = true;
                    } else {
                        mediaCodec.queueInputBuffer(inputBufferId, 0, readSize, mediaExtractor.getSampleTime(), 0);
                        mediaExtractor.advance();
                    }
                }

                int outputBufferId = mediaCodec.dequeueOutputBuffer(info, TIMEOUT_MS);
                if (outputBufferId >= 0) {
                    ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(outputBufferId);
                    if (outputBuffer != null && info.size > 0) {
                        while (info.presentationTimeUs / 1000 > System.currentTimeMillis() - startMs) {
                            try {
                                sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                        byte[] data = new byte[info.size];
                        outputBuffer.get(data);
                        outputBuffer.clear();
                        audioTrack.write(data, 0, info.size);
                    }
                    mediaCodec.releaseOutputBuffer(outputBufferId, false);
                }
            }
        }

        void stopDecode() {
            isStopDecode = true;
            try {
                join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void release() {
            if (mediaExtractor != null) {
                mediaExtractor.release();
                mediaExtractor = null;
            }

            if (mediaCodec != null) {
                mediaCodec.stop();
                mediaCodec.release();
                mediaCodec = null;
            }

            if (audioTrack != null) {
                audioTrack.stop();
                audioTrack.release();
                audioTrack = null;
            }
        }
    }

}
