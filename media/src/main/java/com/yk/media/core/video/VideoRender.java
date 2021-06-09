package com.yk.media.core.video;

import android.content.Context;

import com.yk.media.opengles.render.Renderer;
import com.yk.media.opengles.render.base.BaseRender;

public class VideoRender implements Renderer {
    private BaseRender output;

    private int textureId;

    public VideoRender(Context context) {
        output = new BaseRender(context);
    }

    @Override
    public void onCreate() {
        output.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        output.onChange(width, height);
    }

    @Override
    public void onDraw() {
        output.onDraw(textureId);
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }
}
