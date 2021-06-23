package com.yk.media.opengles.render.other;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.base.BaseRender;

public class MoveLineVerticalRender extends BaseRender {
    private int uOffsetLocation;

    private float offset;

    public MoveLineVerticalRender(Context context) {
        super(
                context,
                "render/other/move_line_vertical/vertex.frag",
                "render/other/move_line_vertical/frag.frag"
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
        GLES20.glUniform1f(uOffsetLocation, offset);
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }
}
