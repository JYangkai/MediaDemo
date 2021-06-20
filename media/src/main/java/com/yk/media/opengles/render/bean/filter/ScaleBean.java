package com.yk.media.opengles.render.bean.filter;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;

public class ScaleBean extends BaseRenderBean {
    private float scaleRatio;

    public ScaleBean(String name, float scaleRatio) {
        super(RenderConstants.Filter.SCALE, name);
        this.scaleRatio = scaleRatio;
    }

    public float getScaleRatio() {
        return scaleRatio;
    }

    public void setScaleRatio(float scaleRatio) {
        this.scaleRatio = scaleRatio;
    }

    @Override
    public String toString() {
        return super.toString() +
                "ScaleBean{" +
                "scaleRatio=" + scaleRatio +
                '}';
    }
}
