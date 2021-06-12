package com.yk.media.opengles.view;

import android.content.Context;
import android.util.AttributeSet;

import com.yk.media.opengles.render.ImageRender;
import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.manager.RenderManager;
import com.yk.media.opengles.view.base.EGLTextureView;

public class ImageView extends EGLTextureView {
    private ImageRender render;

    private final int process = RenderConstants.Process.IMAGE;

    public ImageView(Context context) {
        this(context, null);
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        render = new ImageRender(context, process);
        setRenderer(render);
        setRenderMode(EGLTextureView.RENDERMODE_WHEN_DIRTY);
    }

    public void setPath(String path) {
        render.setPath(path);
        requestRender();
    }

    public void setAssetsFileName(String fileName) {
        render.setAssetsFileName(fileName);
        requestRender();
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
