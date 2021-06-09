package com.yk.media.opengles.view.base;

import android.content.Context;
import android.util.AttributeSet;

import com.yk.media.opengles.render.OesRender;

public class OesView extends EGLTextureView {
    public OesRender render;

    public OesView(Context context) {
        this(context, null);
    }

    public OesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        render = new OesRender(context);
        setRenderer(render);
        setRenderMode(EGLTextureView.RENDERMODE_WHEN_DIRTY);
    }

    public int getFboTextureId() {
        return render.getFboTextureId();
    }
}
