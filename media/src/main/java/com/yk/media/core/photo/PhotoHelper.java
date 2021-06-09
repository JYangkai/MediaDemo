package com.yk.media.core.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLContext;

public class PhotoHelper {
    private PhotoRenderThread photoRenderThread;

    public void takePhoto(Context context,
                          EGLContext eglContext, int textureId,
                          int width, int height,
                          OnPhotoListener onPhotoListener) {
        cancel();
        photoRenderThread = new PhotoRenderThread(
                new WeakReference<>(context), eglContext, textureId,
                width, height, onPhotoListener);
        photoRenderThread.start();
    }

    public void cancel() {
        if (photoRenderThread == null) {
            return;
        }
        photoRenderThread.stopBackground();
        photoRenderThread = null;
    }

    private static class PhotoRenderThread extends Thread implements ImageReader.OnImageAvailableListener {
        private ImageReader reader;
        private PhotoEGL egl;
        private PhotoRender render;

        private WeakReference<Context> contextWeakReference;
        private EGLContext eglContext;
        private int textureId;
        private int width;
        private int height;

        private Handler handler;
        private HandlerThread thread;

        private OnPhotoListener onPhotoListener;

        public PhotoRenderThread(WeakReference<Context> contextWeakReference,
                                 EGLContext eglContext, int textureId,
                                 int width, int height,
                                 OnPhotoListener onPhotoListener) {
            this.contextWeakReference = contextWeakReference;
            this.eglContext = eglContext;
            this.textureId = textureId;
            this.width = width;
            this.height = height;
            this.onPhotoListener = onPhotoListener;
        }

        private void startBackground() {
            stopBackground();
            thread = new HandlerThread("photo_render");
            thread.start();
            handler = new Handler(thread.getLooper());
        }

        private void stopBackground() {
            if (thread != null) {
                thread.quitSafely();
            }
            thread = null;
            handler = null;
        }

        @Override
        public void run() {
            render();
        }

        private void render() {
            startBackground();

            reader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 1);
            reader.setOnImageAvailableListener(this, handler);

            egl = new PhotoEGL();
            egl.init(eglContext, reader.getSurface());

            render = new PhotoRender(contextWeakReference.get());
            render.setTextureId(textureId);
            render.onCreate();
            render.onChange(width, height);
            render.onDraw();

            egl.swapBuffers();
        }

        @Override
        public void onImageAvailable(ImageReader reader) {
            handler.post(new PhotoSaverRunnable(reader.acquireNextImage(), new OnPhotoListener() {
                @Override
                public void onPhoto(Bitmap bitmap) {
                    stopBackground();
                    if (onPhotoListener != null) {
                        onPhotoListener.onPhoto(bitmap);
                    }
                }
            }));
        }
    }

    private static class PhotoSaverRunnable implements Runnable {
        private Image image;
        private OnPhotoListener onPhotoListener;

        public PhotoSaverRunnable(Image image, OnPhotoListener onPhotoListener) {
            this.image = image;
            this.onPhotoListener = onPhotoListener;
        }

        @Override
        public void run() {
            save();
        }

        private void save() {
            if (image == null) {
                return;
            }

            int width = image.getWidth();
            int height = image.getHeight();
            Image.Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            image.close();

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            canvas.drawBitmap(bitmap, 0, 0, null);

            if (onPhotoListener != null) {
                onPhotoListener.onPhoto(bmp);
            }
        }
    }
}
