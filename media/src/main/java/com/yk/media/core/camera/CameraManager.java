package com.yk.media.core.camera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;

import com.yk.media.data.base.Size;

public class CameraManager implements ICamera {
    /**
     * Camera实现
     */
    private final ICamera camera = new Camera1();

    /**
     * 后台线程
     */
    private Handler handler;
    private HandlerThread thread;

    @Override
    public void openCamera(Activity activity, SurfaceTexture surfaceTexture) {
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.openCamera(activity, surfaceTexture);
            }
        });
    }

    @Override
    public void openCamera(int facing, Activity activity, SurfaceTexture surfaceTexture) {
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.openCamera(facing, activity, surfaceTexture);
            }
        });
    }

    @Override
    public void closeCamera() {
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.closeCamera();
            }
        });
    }

    @Override
    public void switchCamera() {
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.switchCamera();
            }
        });
    }

    @Override
    public void switchCamera(int facing) {
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.switchCamera(facing);
            }
        });
    }

    @Override
    public void setCameraFacing(int facing) {
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.setCameraFacing(facing);
            }
        });
    }

    @Override
    public int getCameraFacing() {
        return camera.getCameraFacing();
    }

    @Override
    public void setPreviewSize(Size cameraSize) {
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.setPreviewSize(cameraSize);
            }
        });
    }

    @Override
    public Size getPreviewSize() {
        return camera.getPreviewSize();
    }

    @Override
    public void setDisplayOrientation(int displayOrientation) {
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.setDisplayOrientation(displayOrientation);
            }
        });
    }

    @Override
    public int getDisplayOrientation() {
        return camera.getDisplayOrientation();
    }

    @Override
    public void releaseCamera() {
        if (handler == null) {
            return;
        }
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.releaseCamera();
                stopBackground();
            }
        });
    }

    @Override
    public void addOnCameraListener(OnCameraListener onCameraListener) {
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.addOnCameraListener(onCameraListener);
            }
        });
    }

    @Override
    public void removeOnCameraListener(OnCameraListener onCameraListener) {
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.removeOnCameraListener(onCameraListener);
            }
        });
    }

    @Override
    public void removeAllOnCameraListener() {
        postTask(new Runnable() {
            @Override
            public void run() {
                camera.removeAllOnCameraListener();
            }
        });
    }

    /**
     * 获取Handler
     */
    private Handler getHandler() {
        if (thread == null || handler == null) {
            startBackground();
        }
        return handler;
    }

    /**
     * 开启线程
     */
    private void startBackground() {
        stopBackground();
        thread = new HandlerThread("camera_manager");
        thread.start();
        handler = new Handler(thread.getLooper());
    }

    /**
     * 关闭线程
     */
    private void stopBackground() {
        if (thread != null) {
            thread.quitSafely();
            thread = null;
        }
        if (handler != null) {
            handler = null;
        }
    }

    /**
     * 执行任务
     */
    private void postTask(Runnable runnable) {
        getHandler().post(runnable);
    }
}
