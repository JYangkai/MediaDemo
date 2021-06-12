package com.yk.media.opengles.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;

import com.yk.media.core.camera.CameraManager;
import com.yk.media.core.camera.OnCameraListener;
import com.yk.media.data.base.Size;
import com.yk.media.opengles.render.OesRender;
import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.base.OnSurfaceTextureListener;
import com.yk.media.opengles.render.manager.RenderManager;
import com.yk.media.opengles.view.base.EGLTextureView;

public class CameraView extends EGLTextureView implements OnCameraListener {
    private final CameraManager cameraManager = new CameraManager();

    private Activity activity;

    public OesRender render;

    private final int process = RenderConstants.Process.CAMERA;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        render = new OesRender(context,process);
        setRenderer(render);
        setRenderMode(EGLTextureView.RENDERMODE_WHEN_DIRTY);

        activity = (Activity) context;
        cameraManager.addOnCameraListener(this);
    }

    public void openCamera() {
        render.setOnSurfaceTextureListener(new OnSurfaceTextureListener() {
            @Override
            public void onSurfaceTexture(SurfaceTexture surfaceTexture) {
                surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                    @Override
                    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                        requestRender();
                    }
                });
                cameraManager.openCamera(activity, surfaceTexture);
            }
        });
        requestRender();
    }

    public void closeCamera() {
        cameraManager.closeCamera();
    }

    public void switchCamera() {
        cameraManager.switchCamera();
        openCamera();
    }

    public void switchCamera(int facing) {
        cameraManager.switchCamera(facing);
        openCamera();
    }

    public void addOnCameraListener(OnCameraListener onCameraListener) {
        cameraManager.addOnCameraListener(onCameraListener);
    }

    @Override
    public void onCameraOpened(Size cameraSize, int facing) {
        render.setOesSize(cameraSize.getHeight(), cameraSize.getWidth());
        requestRender();
    }

    @Override
    public void onCameraClosed() {

    }

    @Override
    public void onCameraError(Exception e) {

    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public int getFboTextureId() {
        return render.getFboTextureId();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        RenderManager.getInstance(getContext()).init(process);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RenderManager.getInstance(getContext()).release(process);
    }
}
