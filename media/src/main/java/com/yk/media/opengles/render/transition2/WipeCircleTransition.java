package com.yk.media.opengles.render.transition2;

import android.content.Context;

import com.yk.media.opengles.render.transition2.base.BaseTransition;

public class WipeCircleTransition extends BaseTransition {
    public WipeCircleTransition(Context context) {
        super(
                context,
                "render/transition2/wipe_circle/vertex.frag",
                "render/transition2/wipe_circle/frag.frag"
        );
    }
}
