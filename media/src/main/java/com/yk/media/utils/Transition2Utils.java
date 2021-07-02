package com.yk.media.utils;

import android.content.Context;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.bean.transition.BaseTransitionBean;
import com.yk.media.opengles.render.transition2.MoveDownTransition;
import com.yk.media.opengles.render.transition2.MoveLeftDownTransition;
import com.yk.media.opengles.render.transition2.MoveLeftTransition;
import com.yk.media.opengles.render.transition2.MoveLeftUpTransition;
import com.yk.media.opengles.render.transition2.MoveRightDownTransition;
import com.yk.media.opengles.render.transition2.MoveRightTransition;
import com.yk.media.opengles.render.transition2.MoveRightUpTransition;
import com.yk.media.opengles.render.transition2.MoveUpTransition;
import com.yk.media.opengles.render.transition2.WipeCenterTransition;
import com.yk.media.opengles.render.transition2.WipeCircleTransition;
import com.yk.media.opengles.render.transition2.WipeDownTransition;
import com.yk.media.opengles.render.transition2.WipeLeftTransition;
import com.yk.media.opengles.render.transition2.WipeLeftUpTransition;
import com.yk.media.opengles.render.transition2.WipeRightDownTransition;
import com.yk.media.opengles.render.transition2.WipeRightTransition;
import com.yk.media.opengles.render.transition2.WipeUpTransition;
import com.yk.media.opengles.render.transition2.base.BaseTransition;

import java.util.ArrayList;
import java.util.List;

public class Transition2Utils {
    public static final List<BaseRenderBean> TRANSITION_LIST = new ArrayList<>();

    static {
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.NORMAL, "普通", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.MOVE_UP, "移动向上", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.MOVE_DOWN, "移动向下", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.MOVE_LEFT, "移动向左", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.MOVE_RIGHT, "移动向右", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.MOVE_LEFT_UP, "移动向左上", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.MOVE_RIGHT_UP, "移动向右上", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.MOVE_LEFT_DOWN, "移动向左下", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.MOVE_RIGHT_DOWN, "移动向右下", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.WIPE_LEFT, "抹掉向左", 5000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.WIPE_RIGHT, "抹掉向右", 5000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.WIPE_UP, "抹掉向上", 5000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.WIPE_DOWN, "抹掉向下", 5000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.WIPE_LEFT_UP, "抹掉向左上", 5000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.WIPE_RIGHT_DOWN, "抹掉向右下", 5000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.WIPE_CENTER, "抹掉中心", 5000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition2.WIPE_CIRCLE, "抹掉圆形", 5000));
    }

    public static BaseTransition getTransition(Context context, BaseRenderBean bean) {
        BaseTransition transition;
        switch (bean.getType()) {
            case RenderConstants.Transition2.MOVE_UP:
                transition = new MoveUpTransition(context);
                break;
            case RenderConstants.Transition2.MOVE_DOWN:
                transition = new MoveDownTransition(context);
                break;
            case RenderConstants.Transition2.MOVE_LEFT:
                transition = new MoveLeftTransition(context);
                break;
            case RenderConstants.Transition2.MOVE_RIGHT:
                transition = new MoveRightTransition(context);
                break;
            case RenderConstants.Transition2.MOVE_LEFT_UP:
                transition = new MoveLeftUpTransition(context);
                break;
            case RenderConstants.Transition2.MOVE_RIGHT_UP:
                transition = new MoveRightUpTransition(context);
                break;
            case RenderConstants.Transition2.MOVE_LEFT_DOWN:
                transition = new MoveLeftDownTransition(context);
                break;
            case RenderConstants.Transition2.MOVE_RIGHT_DOWN:
                transition = new MoveRightDownTransition(context);
                break;
            case RenderConstants.Transition2.WIPE_LEFT:
                transition = new WipeLeftTransition(context);
                break;
            case RenderConstants.Transition2.WIPE_RIGHT:
                transition = new WipeRightTransition(context);
                break;
            case RenderConstants.Transition2.WIPE_UP:
                transition = new WipeUpTransition(context);
                break;
            case RenderConstants.Transition2.WIPE_DOWN:
                transition = new WipeDownTransition(context);
                break;
            case RenderConstants.Transition2.WIPE_LEFT_UP:
                transition = new WipeLeftUpTransition(context);
                break;
            case RenderConstants.Transition2.WIPE_RIGHT_DOWN:
                transition = new WipeRightDownTransition(context);
                break;
            case RenderConstants.Transition2.WIPE_CENTER:
                transition = new WipeCenterTransition(context);
                break;
            case RenderConstants.Transition2.WIPE_CIRCLE:
                transition = new WipeCircleTransition(context);
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
