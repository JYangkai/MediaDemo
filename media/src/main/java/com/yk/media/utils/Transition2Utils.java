package com.yk.media.utils;

import android.content.Context;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.bean.transition.BaseTransitionBean;
import com.yk.media.opengles.render.transition2.base.BaseTransition;

import java.util.ArrayList;
import java.util.List;

public class Transition2Utils {
    public static final List<BaseRenderBean> TRANSITION_LIST = new ArrayList<>();

    static {
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.NORMAL, "普通", 1000));
    }

    public static BaseTransition getTransition(Context context, BaseRenderBean bean) {
        BaseTransition transition;
        switch (bean.getType()) {
            default:
                transition = new BaseTransition(context);
                break;
        }
        transition.setRenderBean(bean);
        transition.setBindFbo(true);
        return transition;
    }
}
