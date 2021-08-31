package com.yk.media.opengles.render.filter;

import android.content.Context;

public class TwoPartFilter extends BaseFilter {
    public TwoPartFilter(Context context) {
        super(
                context,
                "render/filter/two_part/vertex.frag",
                "render/filter/two_part/frag.frag"
        );
    }
}
