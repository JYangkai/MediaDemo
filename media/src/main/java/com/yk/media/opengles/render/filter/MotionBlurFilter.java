package com.yk.media.opengles.render.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.bean.filter.MotionBlurBean;

public class MotionBlurFilter extends BaseFilter {
    private int uBlurRadiusLocation;
    private int uSumWeightLocation;
    private int uBlurOffsetLocation;

    private int blurRadius;
    private float blurOffset;
    private float sumWeight;

    public MotionBlurFilter(Context context) {
        super(
                context,
                "render/filter/motion_blur/vertex.frag",
                "render/filter/motion_blur/frag.frag"
        );
    }

    @Override
    public void setRenderBean(BaseRenderBean renderBean) {
        super.setRenderBean(renderBean);
        if (!(renderBean instanceof MotionBlurBean)) {
            return;
        }
        MotionBlurBean bean = (MotionBlurBean) renderBean;

        setBlurRadius(bean.getBlurRadius());
        setBlurOffset(bean.getBlurOffset());
    }

    /**
     * 计算总权重
     */
    private void calculateSumWeight() {
        if (blurRadius < 1) {
            setSumWeight(0);
            return;
        }

        float sumWeight = 0;
        float sigma = blurRadius / 3f;
        for (int i = 0; i < blurRadius; i++) {
            float weight = (float) ((1 / Math.sqrt(2 * Math.PI * sigma * sigma)) * Math.exp(-(i * i) / (2 * sigma * sigma)));
            sumWeight += weight;
        }

        setSumWeight(sumWeight);
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uBlurRadiusLocation = GLES20.glGetUniformLocation(getProgram(), "uBlurRadius");
        uBlurOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "uBlurOffset");
        uSumWeightLocation = GLES20.glGetUniformLocation(getProgram(), "uSumWeight");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        // 计算总权重
        calculateSumWeight();

        GLES20.glUniform1i(uBlurRadiusLocation, blurRadius);
        GLES20.glUniform1f(uBlurOffsetLocation, blurOffset / getWidth());
        GLES20.glUniform1f(uSumWeightLocation, sumWeight);
    }

    public void setBlurRadius(int blurRadius) {
        this.blurRadius = blurRadius;
    }

    public void setBlurOffset(float blurOffset) {
        this.blurOffset = blurOffset;
    }

    public void setSumWeight(float sumWeight) {
        this.sumWeight = sumWeight;
    }
}
