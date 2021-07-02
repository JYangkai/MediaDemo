package com.yk.media.opengles.render.transition2;

import android.content.Context;

import com.yk.media.opengles.render.transition2.base.BaseTransition;

public class WipeRightDownTransition extends BaseTransition {
    public WipeRightDownTransition(Context context) {
        super(
                context,
                "render/transition2/wipe_right_down/vertex.frag",
                "render/transition2/wipe_right_down/frag.frag"
        );
    }
}
