package com.yk.media.utils;

import android.content.Context;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.transition.BlurTransition;
import com.yk.media.opengles.render.transition.MixTransition;
import com.yk.media.opengles.render.transition.PushTransition;
import com.yk.media.opengles.render.transition.base.BaseTransition;

import java.util.ArrayList;
import java.util.List;

public class TransitionUtils {
    public static final List<BaseRenderBean> TRANSITION_LIST = new ArrayList<>();

    static {
        TRANSITION_LIST.add(new BaseRenderBean(RenderConstants.Transition.MIX, "混合"));
        TRANSITION_LIST.add(new BaseRenderBean(RenderConstants.Transition.BLUR, "模糊"));
        TRANSITION_LIST.add(new BaseRenderBean(RenderConstants.Transition.PUSH, "推镜"));
    }

    public static BaseTransition getTransition(Context context, BaseRenderBean bean) {
        BaseTransition transition;
        switch (bean.getType()) {
            case RenderConstants.Transition.MIX:
                transition = new MixTransition(context);
                break;
            case RenderConstants.Transition.BLUR:
                transition = new BlurTransition(context);
                break;
            case RenderConstants.Transition.PUSH:
                transition = new PushTransition(context);
                break;
            default:
                transition = new BaseTransition(context);
                break;
        }
        transition.setRenderBean(bean);
        transition.setBindFbo(true);
        return transition;
    }

}
