package com.yk.media.opengles.render.base;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.utils.OpenGLESUtils;

public class BaseImageRender extends BaseRender {
    private int uMatrixLocation;

    private float[] matrix;

    private int imageW;
    private int imageH;

    public BaseImageRender(Context context) {
        super(
                context,
                "render/base/image/vertex.frag",
                "render/base/image/frag.frag"
        );
    }

    @Override
    public void onCreatePre() {
        super.onCreatePre();
        setBindFbo(true);
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uMatrixLocation = GLES20.glGetUniformLocation(getProgram(), "uMatrix");
    }

    @Override
    public boolean onReadyToDraw() {
        return imageW != 0 && imageH != 0;
    }

    @Override
    public void onDrawPre() {
        super.onDrawPre();
        matrix = OpenGLESUtils.getMatrix(getWidth(), getHeight(), imageW, imageH);
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public void setImageSize(int width, int height) {
        imageW = width;
        imageH = height;
    }
}
