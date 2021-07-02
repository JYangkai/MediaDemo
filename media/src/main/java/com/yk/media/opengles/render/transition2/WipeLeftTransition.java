package com.yk.media.opengles.render.transition2;

import android.content.Context;

import com.yk.media.opengles.render.transition2.base.BaseTransition;

public class WipeLeftTransition extends BaseTransition {
    public WipeLeftTransition(Context context) {
        super(
                context,
                "render/transition2/wipe_left/vertex.frag",
                "render/transition2/wipe_left/frag.frag"
        );
    }
}
