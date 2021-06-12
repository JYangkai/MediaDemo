package com.yk.media.utils;

import android.content.Context;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.filter.BaseFilter;
import com.yk.media.opengles.render.filter.GaussianBlurFilter;

public class FilterUtils {
    public static BaseFilter getFilter(Context context, BaseRenderBean bean) {
        BaseFilter filter;
        switch (bean.getType()) {
            case RenderConstants.Filter.GAUSSIAN_BLUR:
                filter = new GaussianBlurFilter(context);
                break;
            default:
                filter = new BaseFilter(context);
        }
        filter.setRenderBean(bean);
        filter.setBindFbo(true);
        return filter;
    }
}
