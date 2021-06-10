package com.yk.media.opengles.render.bean;

public class GaussianBlurBean extends BaseRenderBean {
    /**
     * 缩放比例
     */
    private int scaleRatio;

    /**
     * 模糊半径
     */
    private int blurRadius;

    public GaussianBlurBean(int type, String name, int scaleRatio, int blurRadius) {
        super(type, name);
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
