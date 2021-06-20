package com.yk.media.opengles.render.transition.manager;

import android.content.Context;

import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.transition.base.BaseTransition;
import com.yk.media.utils.TransitionUtils;

public class TransitionManager {
    private static volatile TransitionManager instance;

    private final Context context;

    private BaseTransition transition;

    private TransitionManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static TransitionManager getInstance(Context context) {
        if (instance == null) {
            synchronized (TransitionManager.class) {
                if (instance == null) {
                    instance = new TransitionManager(context);
                }
            }
        }
        return instance;
    }

    public void init() {
        release();
    }

    public void release() {
        if (transition == null) {
            return;
        }
        transition.onRelease();
        transition.releaseTextureId();
        transition = null;
    }

    public void onCreate() {
        if (transition == null) {
            return;
        }
        transition.setCreate(false);
    }

    public void onChange() {
        if (transition == null) {
            return;
        }
        transition.setChange(false);
    }

    public int onDraw(int textureId, int textureId2, int width, int height) {
        if (transition == null) {
            return textureId;
        }
        transition.onCreate();
        transition.onChange(width, height);
        transition.onDrawTransition(textureId, textureId2);
        return transition.getFboTextureId();
    }

    public void setTransition(BaseRenderBean bean) {
        if (bean == null) {
            transition = null;
            return;
        }
        if (transition != null) {
            transition.onRelease();
        }
        transition = TransitionUtils.getTransition(context, bean);
    }

    public void setProgress(float progress) {
        if (transition == null) {
            return;
        }
        transition.setProgress(progress);
    }
}
