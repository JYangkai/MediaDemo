package com.yk.media.opengles.render.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.base.BaseRender;

public class RetainFrameFilter extends BaseFilter {
    private final BaseRender lastRender;

    private int uSampler2Location;

    private int lastTextureId = -1;

    public RetainFrameFilter(Context context) {
        super(
                context,
                "render/filter/retain_frame/vertex.frag",
                "render/filter/retain_frame/frag.frag"
        );

        lastRender = new BaseRender(context);
        lastRender.setBindFbo(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lastRender.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        super.onChange(width, height);
        lastRender.onChange(width, height);
    }

    @Override
    public void onDraw(int textureId) {
        super.onDraw(textureId);
        lastRender.onDraw(getFboTextureId());
        lastTextureId = lastRender.getFboTextureId();
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uSampler2Location = GLES20.glGetUniformLocation(getProgram(), "uSampler2");
    }

    @Override
    public void onActiveTexture(int textureId) {
        super.onActiveTexture(textureId);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, lastTextureId);
        GLES20.glUniform1i(uSampler2Location, 1);
    }
}
