package com.yk.media.opengles.render.transition;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.transition.base.BaseTransition;

public class MixTransition extends BaseTransition {
    private int uAlphaLocation;

    public MixTransition(Context context) {
        super(
                context,
                "render/transition/mix/vertex.frag",
                "render/transition/mix/frag.frag"
        );
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uAlphaLocation = GLES20.glGetUniformLocation(getProgram(), "uAlpha");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        float alpha = getProgress();
        GLES20.glUniform1f(uAlphaLocation, alpha);
    }
}
