package com.yk.media.opengles.render.bean.filter;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;

public class MeanBlurBean extends BaseRenderBean {
    /**
     * 缩放比例
     */
    private int scaleRatio;

    /**
     * 模糊半径
     */
    private int blurRadius;

    /**
     * 模糊步长
     */
    private int blurOffsetW;
    private int blurOffsetH;

    public MeanBlurBean(String name, int scaleRatio, int blurRadius) {
        this(name, scaleRatio, blurRadius, 1, 1);
    }

    public MeanBlurBean(String name, int scaleRatio, int blurRadius, int blurOffsetW, int blurOffsetH) {
        super(RenderConstants.Filter.MEAN_BLUR, name);
        this.scaleRatio = scaleRatio;
        this.blurRadius = blurRadius;
        this.blurOffsetW = blurOffsetW;
        this.blurOffsetH = blurOffsetH;
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

    public int getBlurOffsetW() {
        return blurOffsetW;
    }

    public void setBlurOffsetW(int blurOffsetW) {
        this.blurOffsetW = blurOffsetW;
    }

    public int getBlurOffsetH() {
        return blurOffsetH;
    }

    public void setBlurOffsetH(int blurOffsetH) {
        this.blurOffsetH = blurOffsetH;
    }

    @Override
    public String toString() {
        return super.toString() +
                "MeanBlurBean{" +
                "scaleRatio=" + scaleRatio +
                ", blurRadius=" + blurRadius +
                ", blurOffsetW=" + blurOffsetW +
                ", blurOffsetH=" + blurOffsetH +
                '}';
    }
}
