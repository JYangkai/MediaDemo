package com.yk.media.opengles.render.transition2;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.transition2.base.BaseTransition;

public class MoveLeftDownTransition extends BaseTransition {
    private int uDirectionalLocation;

    public MoveLeftDownTransition(Context context) {
        super(
                context,
                "render/transition2/move/vertex.frag",
                "render/transition2/move/frag.frag"
        );
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uDirectionalLocation = GLES20.glGetUniformLocation(getProgram(), "uDirectional");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glUniform2f(uDirectionalLocation, 1, -1);
    }
}
