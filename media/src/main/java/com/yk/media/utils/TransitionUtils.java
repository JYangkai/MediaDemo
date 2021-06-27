package com.yk.media.utils;

import android.content.Context;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.bean.transition.BaseTransitionBean;
import com.yk.media.opengles.render.transition.BlurTransition;
import com.yk.media.opengles.render.transition.Cut1Transition;
import com.yk.media.opengles.render.transition.Cut2Transition;
import com.yk.media.opengles.render.transition.Cut3Transition;
import com.yk.media.opengles.render.transition.Cut4Transition;
import com.yk.media.opengles.render.transition.FlipHorizontalTransition;
import com.yk.media.opengles.render.transition.FlipVerticalTransition;
import com.yk.media.opengles.render.transition.MixTransition;
import com.yk.media.opengles.render.transition.MoveDownTransition;
import com.yk.media.opengles.render.transition.MoveLeftDownTransition;
import com.yk.media.opengles.render.transition.MoveLeftTransition;
import com.yk.media.opengles.render.transition.MoveRightDownTransition;
import com.yk.media.opengles.render.transition.MoveRightTopTransition;
import com.yk.media.opengles.render.transition.MoveRightTransition;
import com.yk.media.opengles.render.transition.MoveTopMoveTransition;
import com.yk.media.opengles.render.transition.MoveTopTransition;
import com.yk.media.opengles.render.transition.PageUpTransition;
import com.yk.media.opengles.render.transition.PullTransition;
import com.yk.media.opengles.render.transition.PushTransition;
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
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.TOP_MOVE, "上移", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.DOWN_MOVE, "下移", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.LEFT_TOP_MOVE, "左上移", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.RIGHT_TOP_MOVE, "右上移", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.LEFT_DOWN_MOVE, "左下移", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.RIGHT_DOWN_MOVE, "右下移", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.PAGE_UP, "翻页", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.CUT_1, "分割一", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.CUT_2, "分割二", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.CUT_3, "分割三", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.CUT_4, "分割四", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.FLIP_HORIZONTAL, "水平翻转", 1000));
        TRANSITION_LIST.add(new BaseTransitionBean(RenderConstants.Transition.FLIP_VERTICAL, "垂直翻转", 1000));
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
                transition = new MoveLeftTransition(context);
                break;
            case RenderConstants.Transition.RIGHT_MOVE:
                transition = new MoveRightTransition(context);
                break;
            case RenderConstants.Transition.TOP_MOVE:
                transition = new MoveTopTransition(context);
                break;
            case RenderConstants.Transition.DOWN_MOVE:
                transition = new MoveDownTransition(context);
                break;
            case RenderConstants.Transition.LEFT_TOP_MOVE:
                transition = new MoveTopMoveTransition(context);
                break;
            case RenderConstants.Transition.RIGHT_TOP_MOVE:
                transition = new MoveRightTopTransition(context);
                break;
            case RenderConstants.Transition.LEFT_DOWN_MOVE:
                transition = new MoveLeftDownTransition(context);
                break;
            case RenderConstants.Transition.RIGHT_DOWN_MOVE:
                transition = new MoveRightDownTransition(context);
                break;
            case RenderConstants.Transition.PAGE_UP:
                transition = new PageUpTransition(context);
                break;
            case RenderConstants.Transition.CUT_1:
                transition = new Cut1Transition(context);
                break;
            case RenderConstants.Transition.CUT_2:
                transition = new Cut2Transition(context);
                break;
            case RenderConstants.Transition.CUT_3:
                transition = new Cut3Transition(context);
                break;
            case RenderConstants.Transition.CUT_4:
                transition = new Cut4Transition(context);
                break;
            case RenderConstants.Transition.FLIP_HORIZONTAL:
                transition = new FlipHorizontalTransition(context);
                break;
            case RenderConstants.Transition.FLIP_VERTICAL:
                transition = new FlipVerticalTransition(context);
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
