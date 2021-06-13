package com.yk.media.opengles.render.filter;

import android.content.Context;

public class GrayFilter extends BaseFilter {
    public GrayFilter(Context context) {
        super(
                context,
                "render/filter/gray/vertex.frag",
                "render/filter/gray/frag.frag"
        );
    }
}
