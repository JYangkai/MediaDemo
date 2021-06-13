package com.yk.media.utils;

import android.content.Context;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.bean.filter.GaussianBlurBean;
import com.yk.media.opengles.render.filter.BaseFilter;
import com.yk.media.opengles.render.filter.GaussianBlurFilter;
import com.yk.media.opengles.render.filter.GrayFilter;

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {
    public static final List<BaseRenderBean> FILTER_LIST = new ArrayList<>();

    static {
        FILTER_LIST.add(new GaussianBlurBean(1, 3));
        FILTER_LIST.add(new BaseRenderBean(RenderConstants.Filter.GRAY, "灰度滤镜"));
    }

    public static BaseFilter getFilter(Context context, BaseRenderBean bean) {
        BaseFilter filter;
        switch (bean.getType()) {
            case RenderConstants.Filter.GAUSSIAN_BLUR:
                filter = new GaussianBlurFilter(context);
                break;
            case RenderConstants.Filter.GRAY:
                filter = new GrayFilter(context);
                break;
            default:
                filter = new BaseFilter(context);
        }
        filter.setRenderBean(bean);
        filter.setBindFbo(true);
        return filter;
    }
}