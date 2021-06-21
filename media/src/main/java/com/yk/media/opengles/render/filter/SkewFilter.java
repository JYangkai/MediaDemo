package com.yk.media.opengles.render.filter;

import android.content.Context;
import android.opengl.GLES20;

public class SkewFilter extends BaseFilter {
    private int uSizeLocation;
    private int uRotateLocation;

    private float rotate;

    public SkewFilter(Context context) {
        super(
                context,
                "render/filter/skew/vertex.frag",
                "render/filter/skew/frag.frag"
        );
        setRotate(0);
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uSizeLocation = GLES20.glGetUniformLocation(getProgram(), "uSize");
        uRotateLocation = GLES20.glGetUniformLocation(getProgram(), "uRotate");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glUniform2f(uSizeLocation, getWidth(), getHeight());
        GLES20.glUniform1f(uRotateLocation, rotate);
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }
}
