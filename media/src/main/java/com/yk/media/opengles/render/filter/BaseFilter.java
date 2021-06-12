package com.yk.media.opengles.render.filter;

import android.content.Context;

import com.yk.media.opengles.render.base.BaseRender;

public class BaseFilter extends BaseRender {
    public BaseFilter(Context context) {
        super(context);
    }

    public BaseFilter(Context context, String vertexFilename, String fragFilename) {
        super(context, vertexFilename, fragFilename);
    }
}
