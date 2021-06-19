package com.yk.media.opengles.render.bean.filter;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;

public class MotionBlurBean extends BaseRenderBean {
    /**
     * 模糊半径
     */
    private int blurRadius;

    /**
     * 模糊步长
     */
    private int blurOffset;

    public MotionBlurBean(int blurRadius, int blurOffset) {
        super(RenderConstants.Filter.MOTION_BLUR, "运动模糊");
        this.blurRadius = blurRadius;
        this.blurOffset = blurOffset;
    }

    public int getBlurRadius() {
        return blurRadius;
    }

    public void setBlurRadius(int blurRadius) {
        this.blurRadius = blurRadius;
    }

    public int getBlurOffset() {
        return blurOffset;
    }

    public void setBlurOffset(int blurOffset) {
        this.blurOffset = blurOffset;
    }

    @Override
    public String toString() {
        return super.toString() +
                "MotionBlurBean{" +
                "blurRadius=" + blurRadius +
                ", blurOffset=" + blurOffset +
                '}';
    }
}
