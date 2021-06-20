package com.yk.media.opengles.render.transition.base;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.base.BaseRender;

public class BaseTransition extends BaseRender {
    private int uSampler2Location;

    private int textureId2;

    private float progress;

    public BaseTransition(Context context) {
        super(
                context,
                "render/base/transition/vertex.frag",
                "render/base/transition/frag.frag"
        );
    }

    public BaseTransition(Context context, String vertexFilename, String fragFilename) {
        super(context, vertexFilename, fragFilename);
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
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId2);
        GLES20.glUniform1i(uSampler2Location, 1);
    }

    public void onDrawTransition(int textureId, int textureId2) {
        this.textureId2 = textureId2;
        super.onDraw(textureId);
    }

    public float getProgress() {
        return progress;
    }

    public float getAccelerateProgress() {
        return (float) Math.pow(progress, 2 * 1.5);
    }

    public float getDecelerateProgress() {
        return (float) (1 - Math.pow(1 - progress, 2 * 1.5));
    }

    public float getAccelerateDecelerateProgress() {
        return (float) (Math.cos((progress + 1) * Math.PI) / 2 + 0.5);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public void onRelease() {
        onDeleteProgram(getProgram());
        onDeleteShader(getVertexShader());
        onDeleteShader(getFragShader());
        onDeleteTexture(getFboTextureId());
        onDeleteFbo(getFboId());
        onDeleteVbo(getVboId());
    }

    public void releaseTextureId() {
        onDeleteTexture(getTextureId());
        onDeleteTexture(textureId2);
    }
}