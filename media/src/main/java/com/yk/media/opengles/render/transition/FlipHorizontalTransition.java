package com.yk.media.opengles.render.transition;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.transition.base.BaseTransition;

public class FlipHorizontalTransition extends BaseTransition {
    private int uCompressLocation;
    private int uUseSamplerLocation;

    public FlipHorizontalTransition(Context context) {
        super(
                context,
                "render/transition/flip_horizontal/vertex.frag",
                "render/transition/flip_horizontal/frag.frag"
        );
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uCompressLocation = GLES20.glGetUniformLocation(getProgram(), "uCompress");
        uUseSamplerLocation = GLES20.glGetUniformLocation(getProgram(), "uUseSampler");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        float progress = getProgress();

        float compress;
        int useSampler;
        if (progress < 0.5) {
            compress = progress * 2;
            useSampler = 0;
        } else {
            compress = (1 - progress) * 2;
            useSampler = 1;
        }

        GLES20.glUniform1f(uCompressLocation, compress);
        GLES20.glUniform1i(uUseSamplerLocation, useSampler);
    }
}
