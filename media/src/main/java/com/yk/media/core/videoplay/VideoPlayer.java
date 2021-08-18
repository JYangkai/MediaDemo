package com.yk.media.core.videoplay;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.text.TextUtils;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

public class VideoPlayer {
    private AudioPlayThread audioPlayThread;
    private VideoPlayThread videoPlayThread;

    public void stat(String path, Surface surface) {
        stop();
        audioPlayThread = new AudioPlayThread(path);
        videoPlayThread = new VideoPlayThread(path, surface);

        audioPlayThread.start();
        videoPlayThread.start();
    }

    public void stop() {
        if (audioPlayThread != null) {
            audioPlayThread.stopPlay();
            audioPlayThread = null;
        }
        if (videoPlayThread != null) {
            videoPlayThread.stopPlay();
            videoPlayThread = null;
        }
    }

    private static class AudioPlayThread extends Thread {
        private static final long TIMEOUT_MS = 2000L;

        private String path;

        private MediaExtractor mediaExtractor;
        private MediaCodec mediaCodec;
        private AudioTrack audioTrack;

        private MediaFormat format;
        private String mime;

        private boolean isStopPlay = false;

        public AudioPlayThread(String path) {
            this.path = path;
        }

        @Override
        public void run() {
            super.run();
            initMediaExtractor();
            initMediaCodec();
            initAudioTrack();
            play();
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
                    format = mediaExtractor.getTrackFormat(i);
                    mime = format.getString(MediaFormat.KEY_MIME);
                    if (!TextUtils.isEmpty(mime) && mime.startsWith("audio/")) {
                        mediaExtractor.selectTrack(i);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                mediaExtractor = null;
                format = null;
                mime = null;
            }
        }

        private void initMediaCodec() {
            if (format == null || TextUtils.isEmpty(mime)) {
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
            if (format == null) {
                return;
            }
            int sampleRateInHz = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
            int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            int bufferSizeInBytes = AudioTrack.getMinBufferSize(
                    sampleRateInHz,
                    channelConfig, audioFormat
            );
            audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleRateInHz,
                    channelConfig,
                    audioFormat,
                    bufferSizeInBytes,
                    AudioTrack.MODE_STREAM
            );
        }

        private void play() {
            if (mediaExtractor == null || mediaCodec == null || audioTrack == null) {
                return;
            }

            long startMs = System.currentTimeMillis();
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            mediaCodec.start();
            audioTrack.play();

            for (; ; ) {
                if (isStopPlay) {
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
                        isStopPlay = true;
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

        void stopPlay() {
            isStopPlay = true;
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

    private static class VideoPlayThread extends Thread {
        private static final long TIMEOUT_MS = 2000L;

        private String path;
        private Surface surface;

        private MediaExtractor mediaExtractor;
        private MediaCodec mediaCodec;

        private MediaFormat format;
        private String mime;

        private boolean isStopPlay = false;

        public VideoPlayThread(String path, Surface surface) {
            this.path = path;
            this.surface = surface;
        }

        @Override
        public void run() {
            super.run();
            initMediaExtractor();
            initMediaCodec();
            play();
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
                    format = mediaExtractor.getTrackFormat(i);
                    mime = format.getString(MediaFormat.KEY_MIME);
                    if (!TextUtils.isEmpty(mime) && mime.startsWith("video/")) {
                        mediaExtractor.selectTrack(i);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                mediaExtractor = null;
                format = null;
                mime = null;
            }
        }

        private void initMediaCodec() {
            if (format == null || TextUtils.isEmpty(mime) || surface == null) {
                return;
            }
            try {
                mediaCodec = MediaCodec.createDecoderByType(mime);
                mediaCodec.configure(format, surface, null, 0);
            } catch (IOException e) {
                e.printStackTrace();
                mediaCodec = null;
            }
        }

        private void play() {
            if (mediaExtractor == null || mediaCodec == null) {
                return;
            }

            long startMs = System.currentTimeMillis();
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            mediaCodec.start();

            for (; ; ) {
                if (isStopPlay) {
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
                        isStopPlay = true;
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

                        // 得到的data数据就是YUV数据，可以拿去做对应的业务
                    }
                    mediaCodec.releaseOutputBuffer(outputBufferId, true);
                }
            }
        }

        void stopPlay() {
            isStopPlay = true;
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

            surface = null;
        }
    }
}
