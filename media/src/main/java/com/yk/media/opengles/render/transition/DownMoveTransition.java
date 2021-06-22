package com.yk.media.opengles.render.transition;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.yk.media.opengles.render.transition.base.BaseTransition;

public class DownMoveTransition extends BaseTransition {
    private static final String TAG = "LeftMoveTransition";

    private int uOffsetLocation;
    private int uUseSamplerLocation;

    public DownMoveTransition(Context context) {
        super(
                context,
                "render/transition/down_move/vertex.frag",
                "render/transition/down_move/frag.frag"
        );
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "uOffset");
        uUseSamplerLocation = GLES20.glGetUniformLocation(getProgram(), "uUseSampler");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        float progress = getAccelerateProgress();

        float offset;
        int useSampler;
        if (progress < 0.5) {
            offset = progress * 2;
            useSampler = 0;
        } else {
            offset = 1 + (progress - 0.5f) * 2;
            useSampler = 1;
        }

        Log.d(TAG, "onSetOtherData: " + offset);

        GLES20.glUniform1f(uOffsetLocation, offset);
        GLES20.glUniform1i(uUseSamplerLocation, useSampler);
    }
}
