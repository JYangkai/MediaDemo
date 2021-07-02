package com.yk.media.opengles.render.transition2;

import android.content.Context;

import com.yk.media.opengles.render.transition2.base.BaseTransition;

public class WipeDownTransition extends BaseTransition {
    public WipeDownTransition(Context context) {
        super(
                context,
                "render/transition2/wipe_down/vertex.frag",
                "render/transition2/wipe_down/frag.frag"
        );
    }
}
