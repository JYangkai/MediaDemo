package com.yk.media.opengles.render.bean.filter;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;

public class GaussianBlurBean extends BaseRenderBean {
    /**
     * 缩放比例
     */
    private int scaleRatio;

    /**
     * 模糊半径
     */
    private int blurRadius;

    public GaussianBlurBean(int scaleRatio, int blurRadius) {
        super(RenderConstants.Filter.GAUSSIAN_BLUR, "高斯模糊");
        this.scaleRatio = scaleRatio;
        this.blurRadius = blurRadius;
    }

    public int getScaleRatio() {
        return scaleRatio;
    }

    public void setScaleRatio(int scaleRatio) {
        this.scaleRatio = scaleRatio;
    }

    public int getBlurRadius() {
        return blurRadius;
    }

    public void setBlurRadius(int blurRadius) {
        this.blurRadius = blurRadius;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Filter{" +
                "scaleRatio=" + scaleRatio +
                ", blurRadius=" + blurRadius +
                '}';
    }
}
