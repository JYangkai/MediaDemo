package com.yk.media.opengles.view;

import android.content.Context;
import android.util.AttributeSet;

import com.yk.media.opengles.render.ImageRender;
import com.yk.media.opengles.view.base.EGLTextureView;

public class ImageView extends EGLTextureView {
    private ImageRender render;

    public ImageView(Context context) {
        this(context, null);
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        render = new ImageRender(context);
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
}
