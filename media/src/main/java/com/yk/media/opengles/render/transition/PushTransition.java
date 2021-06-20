package com.yk.media.opengles.render.transition;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.bean.filter.MotionBlurBean;
import com.yk.media.opengles.render.bean.filter.ScaleBean;
import com.yk.media.opengles.render.filter.MotionBlurFilter;
import com.yk.media.opengles.render.filter.ScaleFilter;
import com.yk.media.opengles.render.transition.base.BaseTransition;
import com.yk.media.utils.FilterUtils;

public class PushTransition extends BaseTransition {
    private int uAlphaLocation;

    private ScaleFilter scaleFilter;
    private MotionBlurFilter motionBlurFilter;

    public PushTransition(Context context) {
        super(
                context,
                "render/transition/push/vertex.frag",
                "render/transition/push/frag.frag"
        );
        scaleFilter = (ScaleFilter) FilterUtils.getFilter(
                context, new ScaleBean("缩放", 1)
        );
        motionBlurFilter = (MotionBlurFilter) FilterUtils.getFilter(
                context, new MotionBlurBean(1, 1)
        );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        scaleFilter.onCreate();
        motionBlurFilter.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        super.onChange(width, height);
        scaleFilter.onChange(width, height);
        motionBlurFilter.onChange(width, height);
    }

    @Override
    public void onDrawTransition(int textureId, int textureId2) {
        super.onDrawTransition(textureId, textureId2);
        scaleFilter.onDraw(super.getFboTextureId());
        motionBlurFilter.onDraw(scaleFilter.getFboTextureId());
    }

    @Override
    public int getFboTextureId() {
        return motionBlurFilter.getFboTextureId();
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uAlphaLocation = GLES20.glGetUniformLocation(getProgram(), "uAlpha");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        float progress = getAccelerateDecelerateProgress();

        float alpha;
        float scaleRatio;
        int blurRadius;
        int blurOffset;
        if (progress < 0.5) {
            alpha = 0;
            scaleRatio = 1 + progress * 3;
            blurRadius = (int) (progress * 2 * 30);
            blurOffset = (int) (progress * 2 * 30);
        } else {
            alpha = 1;
            scaleRatio = 1 - (1 - progress) * 2 * 0.2f;
            blurRadius = (int) ((1 - progress) * 2 * 30);
            blurOffset = (int) ((1 - progress) * 2 * 30);
        }

        GLES20.glUniform1f(uAlphaLocation, alpha);

        ScaleBean scaleBean = new ScaleBean("缩放", scaleRatio);
        scaleFilter.updateRenderBean(scaleBean);

        MotionBlurBean motionBlurBean = new MotionBlurBean(blurRadius, blurOffset);
        motionBlurFilter.updateRenderBean(motionBlurBean);
    }
}
