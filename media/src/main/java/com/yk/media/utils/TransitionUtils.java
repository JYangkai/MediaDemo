package com.yk.media.utils;

import android.content.Context;

import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.transition.BaseTransition;

public class TransitionUtils {

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
