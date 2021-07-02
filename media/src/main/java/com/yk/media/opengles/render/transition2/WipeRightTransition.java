package com.yk.media.opengles.render.transition2;

import android.content.Context;

import com.yk.media.opengles.render.transition2.base.BaseTransition;

public class WipeRightTransition extends BaseTransition {
    public WipeRightTransition(Context context) {
        super(
                context,
                "render/transition2/wipe_right/vertex.frag",
                "render/transition2/wipe_right/frag.frag"
        );
    }
}
