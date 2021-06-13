package com.yk.media.utils;

import android.content.Context;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.bean.filter.GaussianBlurBean;
import com.yk.media.opengles.render.filter.BaseFilter;
import com.yk.media.opengles.render.filter.GaussianBlurFilter;
import com.yk.media.opengles.render.filter.GrayFilter;
import com.yk.media.opengles.render.filter.MotionBlurFilter;
import com.yk.media.opengles.render.filter.PipFilter;

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {
    public static final List<BaseRenderBean> FILTER_LIST = new ArrayList<>();

    static {
        FILTER_LIST.add(new BaseRenderBean(RenderConstants.Filter.NORMAL, "原画"));
        FILTER_LIST.add(new GaussianBlurBean(1, 3));
        FILTER_LIST.add(new GaussianBlurBean(300, 20));
        FILTER_LIST.add(new BaseRenderBean(RenderConstants.Filter.GRAY, "灰度滤镜"));
        FILTER_LIST.add(new BaseRenderBean(RenderConstants.Filter.PIP, "画中画"));
        FILTER_LIST.add(new BaseRenderBean(RenderConstants.Filter.MOTION_BLUR, "运动模糊"));
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
            case RenderConstants.Filter.PIP:
                filter = new PipFilter(context);
                break;
            case RenderConstants.Filter.MOTION_BLUR:
                filter = new MotionBlurFilter(context);
                break;
            default:
                filter = new BaseFilter(context);
        }
        filter.setRenderBean(bean);
        filter.setBindFbo(true);
        return filter;
    }
}
