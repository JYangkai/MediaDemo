package com.yk.media.opengles.render.filter;

import android.content.Context;

public class NinePartFilter extends BaseFilter {
    public NinePartFilter(Context context) {
        super(
                context,
                "render/filter/nine_part/vertex.frag",
                "render/filter/nine_part/frag.frag"
        );
    }
}
