package com.yk.media.opengles.render.other;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.base.BaseRender;

public class RetainFrameVerticalRender extends BaseRender {
    private final BaseRender lastRender;

    private int uSampler2Location;
    private int uOffsetLocation;

    private int lastTextureId = -1;

    private float offset;

    public RetainFrameVerticalRender(Context context) {
        super(
                context,
                "render/other/retain_frame_vertical/vertex.frag",
                "render/other/retain_frame_vertical/frag.frag"
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
        uOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "uOffset");
    }

    @Override
    public void onActiveTexture(int textureId) {
        super.onActiveTexture(textureId);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, lastTextureId);
        GLES20.glUniform1i(uSampler2Location, 1);
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glUniform1f(uOffsetLocation, offset);
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }
}
