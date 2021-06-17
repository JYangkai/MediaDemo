package com.yk.media.opengles.render.bean.filter;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;

public class MotionBlurBean extends BaseRenderBean {
    /**
     * 模糊半径
     */
    private int blurRadius;

    public MotionBlurBean(int blurRadius) {
        super(RenderConstants.Filter.MOTION_BLUR, "运动模糊");
        this.blurRadius = blurRadius;
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
                "MotionBlurBean{" +
                "blurRadius=" + blurRadius +
                '}';
    }
}
