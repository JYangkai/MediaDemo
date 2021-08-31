package com.yk.media.opengles.render.filter;

import android.content.Context;

public class ThreePartFilter extends BaseFilter {
    public ThreePartFilter(Context context) {
        super(
                context,
                "render/filter/three_part/vertex.frag",
                "render/filter/three_part/frag.frag"
        );
    }
}
