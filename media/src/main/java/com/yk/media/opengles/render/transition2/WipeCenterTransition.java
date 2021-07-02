package com.yk.media.opengles.render.transition2;

import android.content.Context;

import com.yk.media.opengles.render.transition2.base.BaseTransition;

public class WipeCenterTransition extends BaseTransition {
    public WipeCenterTransition(Context context) {
        super(
                context,
                "render/transition2/wipe_center/vertex.frag",
                "render/transition2/wipe_center/frag.frag"
        );
    }
}
