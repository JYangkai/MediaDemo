package com.yk.media.core.record.video;

import android.content.Context;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.Renderer;
import com.yk.media.opengles.render.base.BaseRender;
import com.yk.media.opengles.render.manager.RenderManager;

public class VideoRender implements Renderer {
    private final int process = RenderConstants.Process.RECORD_VIDEO;

    private final Context context;

    private final BaseRender inputRender;
    private final BaseRender outputRender;

    private int width;
    private int height;

    private int textureId;

    public VideoRender(Context context) {
        this.context = context;
        inputRender = new BaseRender(context);
        outputRender = new BaseRender(context);

        inputRender.setBindFbo(true);
    }

    @Override
    public void onCreate() {
        inputRender.onCreate();
        RenderManager.getInstance(context).onCreate(process);
        outputRender.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        this.width = width;
        this.height = height;
        inputRender.onChange(width, height);
        RenderManager.getInstance(context).onChange(process);
        outputRender.onChange(width, height);
    }

    @Override
    public void onDraw() {
        inputRender.onDraw(textureId);
        int fboTextureId = inputRender.getFboTextureId();
        fboTextureId = RenderManager.getInstance(context).onDraw(process, fboTextureId, width, height);
        outputRender.onDraw(fboTextureId);
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }
}
