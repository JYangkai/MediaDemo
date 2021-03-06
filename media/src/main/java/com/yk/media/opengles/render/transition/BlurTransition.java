package com.yk.media.opengles.render.transition;

import android.content.Context;

import com.yk.media.opengles.render.bean.filter.GaussianBlurBean;
import com.yk.media.opengles.render.filter.GaussianBlurFilter;
import com.yk.media.opengles.render.transition.base.BaseTransition;

public class BlurTransition extends BaseTransition {
    private final MixTransition mixTransition;
    private final GaussianBlurFilter gaussianBlurFilter;

    public BlurTransition(Context context) {
        super(context);
        mixTransition = new MixTransition(context);
        gaussianBlurFilter = new GaussianBlurFilter(context);

        mixTransition.setBindFbo(true);
        gaussianBlurFilter.setBindFbo(true);
    }

    @Override
    public void onCreate() {
        mixTransition.onCreate();
        gaussianBlurFilter.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        mixTransition.onChange(width, height);
        gaussianBlurFilter.onChange(width, height);
    }

    @Override
    public void onDrawTransition(int textureId, int textureId2) {
        mixTransition.onDrawTransition(textureId, textureId2);

        float progress = getProgress();
        int scaleRatio = 4;
        int blurRadius;
        int blurOffset;
        if (progress < 0.5) {
            blurRadius = (int) (progress * 2 * 30);
            blurOffset = (int) (progress * 2 * 5);
        } else {
            blurRadius = (int) ((1 - progress) * 2 * 30);
            blurOffset = (int) ((1 - progress) * 2 * 5);
        }
        GaussianBlurBean gaussianBlurBean = new GaussianBlurBean("高斯模糊", scaleRatio, blurRadius, blurOffset, blurOffset);
        gaussianBlurFilter.updateRenderBean(gaussianBlurBean);

        gaussianBlurFilter.onDraw(mixTransition.getFboTextureId());
    }

    @Override
    public int getFboTextureId() {
        return gaussianBlurFilter.getFboTextureId();
    }

    @Override
    public void setProgress(float progress) {
        super.setProgress(progress);
        mixTransition.setProgress(progress);
    }

    @Override
    public void onRelease() {
        super.onRelease();
        mixTransition.onRelease();
        gaussianBlurFilter.onRelease();
    }
}
