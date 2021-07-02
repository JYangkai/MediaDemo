package com.yk.media.opengles.render.transition2;

import android.content.Context;

import com.yk.media.opengles.render.transition2.base.BaseTransition;

public class WipeLeftUpTransition extends BaseTransition {
    public WipeLeftUpTransition(Context context) {
        super(
                context,
                "render/transition2/wipe_left_up/vertex.frag",
                "render/transition2/wipe_left_up/frag.frag"
        );
    }
}
