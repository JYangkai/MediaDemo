package com.yk.media.opengles.render.filter;

import android.content.Context;

public class FourPartFilter extends BaseFilter {
    public FourPartFilter(Context context) {
        super(
                context,
                "render/filter/four_part/vertex.frag",
                "render/filter/four_part/frag.frag"
        );
    }
}
