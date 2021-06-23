package com.yk.media.opengles.render.other;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.base.BaseRender;

public class MoveLineHorizontalRender extends BaseRender {
    private int uOffsetLocation;

    private float offset;

    public MoveLineHorizontalRender(Context context) {
        super(
                context,
                "render/other/move_line_horizontal/vertex.frag",
                "render/other/move_line_horizontal/frag.frag"
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
