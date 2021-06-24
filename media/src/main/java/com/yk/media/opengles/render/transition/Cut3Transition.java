package com.yk.media.opengles.render.transition;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.transition.base.BaseTransition;

public class Cut3Transition extends BaseTransition {
    private int uOffsetLocation;

    public Cut3Transition(Context context) {
        super(
                context,
                "render/transition/cut_3/vertex.frag",
                "render/transition/cut_3/frag.frag"
        );
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "uOffset");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        float progress = getAccelerateProgress();

        GLES20.glUniform1f(uOffsetLocation, progress);
    }
}
