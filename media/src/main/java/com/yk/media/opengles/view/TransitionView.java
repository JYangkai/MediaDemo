package com.yk.media.opengles.view;

import android.content.Context;
import android.util.AttributeSet;

import com.yk.media.opengles.render.TransitionRender;
import com.yk.media.opengles.render.transition.TransitionManager;
import com.yk.media.opengles.view.base.EGLTextureView;

public class TransitionView extends EGLTextureView {
    private TransitionRender render;

    public TransitionView(Context context) {
        this(context, null);
    }

    public TransitionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        render = new TransitionRender(context);
        setRenderer(render);
        setRenderMode(EGLTextureView.RENDERMODE_WHEN_DIRTY);
    }

    public void setPath(String path, String path2) {
        render.setPath(path, path2);
        requestRender();
    }

    public void setAssetsFileName(String fileName, String fileName2) {
        render.setAssetsFileName(fileName, fileName2);
        requestRender();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        TransitionManager.getInstance(getContext()).init();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        TransitionManager.getInstance(getContext()).release();
    }
}
