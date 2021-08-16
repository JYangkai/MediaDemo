package com.yk.media.core.yuv;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.text.TextUtils;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

public class YuvDecoder {

    private DecodeThread decodeThread;

    public void start(String path, Surface surface) {
        stop();
        decodeThread = new DecodeThread(path, surface);
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

        private String path;
        private Surface surface;

        private String mime;
        private MediaFormat format;

        private boolean isStopDecode = false;

        public DecodeThread(String path, Surface surface) {
            this.path = path;
            this.surface = surface;
        }

        @Override
        public void run() {
            super.run();
            initMediaExtractor();
            initMediaCodec();
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
                    if (!TextUtils.isEmpty(mime) && mime.startsWith("video/")) {
                        mediaExtractor.selectTrack(i);
                        this.mime = mime;
                        this.format = format;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                mediaExtractor = null;
                mime = null;
                format = null;
            }
        }

        private void initMediaCodec() {
            if (TextUtils.isEmpty(mime) || format == null || surface == null) {
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

        private void decode() {
            if (mediaExtractor == null || mediaCodec == null) {
                return;
            }

            long startMs = System.currentTimeMillis();
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

            mediaCodec.start();

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

                        // 得到的data数据就是YUV数据，可以拿去做对应的业务
                    }
                    mediaCodec.releaseOutputBuffer(outputBufferId, true);
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
        }
    }
}
