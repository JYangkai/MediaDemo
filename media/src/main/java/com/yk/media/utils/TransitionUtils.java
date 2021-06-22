package com.yk.media.utils;

import android.content.Context;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.bean.transition.BaseTransitionBean;
import com.yk.media.opengles.render.transition.BlurTransition;
import com.yk.media.opengles.render.transition.LeftMoveTransition;
import com.yk.media.opengles.render.transition.MixTransition;
import com.yk.media.opengles.render.transition.PullTransition;
import com.yk.media.opengles.render.transition.PushTransition;
import com.yk.media.opengles.render.transition.RightMoveTransition;
import com.yk.media.opengles.render.transition.VortexTransition;
import com.yk.media.opengles.render.transition.base.BaseTransition;

import java.util.ArrayList;
import java.util.List;

public class TransitionUtils {
    public static final List<BaseRenderBean> TRANSITION_LIST = new ArrayList<>();

    static {
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.MIX, "混合", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.BLUR, "模糊", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.PUSH, "推镜", 500));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.PULL, "拉镜", 500));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.VORTEX, "旋涡", 10000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.LEFT_MOVE, "左移", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.RIGHT_MOVE, "右移", 1000));
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
            case RenderConstants.Transition.PULL:
                transition = new PullTransition(context);
                break;
            case RenderConstants.Transition.VORTEX:
                transition = new VortexTransition(context);
                break;
            case RenderConstants.Transition.LEFT_MOVE:
                transition = new LeftMoveTransition(context);
                break;
            case RenderConstants.Transition.RIGHT_MOVE:
                transition = new RightMoveTransition(context);
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
