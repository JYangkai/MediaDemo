package com.yk.media.opengles.render.transition2;

import android.content.Context;

import com.yk.media.opengles.render.transition2.base.BaseTransition;

public class WipeUpTransition extends BaseTransition {
    public WipeUpTransition(Context context) {
        super(
                context,
                "render/transition2/wipe_up/vertex.frag",
                "render/transition2/wipe_up/frag.frag"
        );
    }
}
