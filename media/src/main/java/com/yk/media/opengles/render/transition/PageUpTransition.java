package com.yk.media.opengles.render.transition;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.transition.base.BaseTransition;

public class PageUpTransition extends BaseTransition {
    private int uOffsetLocation;
    private int uDarkenLocation;

    public PageUpTransition(Context context) {
        super(
                context,
                "render/transition/page_up/vertex.frag",
                "render/transition/page_up/frag.frag"
        );
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "uOffset");
        uDarkenLocation = GLES20.glGetUniformLocation(getProgram(), "uDarken");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        float progress = getProgress();

        float offset = 1 - progress;
        float darken = progress != 1.0 ? 0.1f : 0;

        GLES20.glUniform1f(uOffsetLocation, offset);
        GLES20.glUniform1f(uDarkenLocation, darken);
    }
}
