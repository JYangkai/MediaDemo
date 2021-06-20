package com.yk.media.opengles.render.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.bean.filter.ScaleBean;

public class ScaleFilter extends BaseFilter {
    private int uScaleLocation;

    private float scaleRatio;

    public ScaleFilter(Context context) {
        super(
                context,
                "render/filter/scale/vertex.frag",
                "render/filter/scale/frag.frag"
        );
    }

    @Override
    public void setRenderBean(BaseRenderBean renderBean) {
        super.setRenderBean(renderBean);
        if (!(renderBean instanceof ScaleBean)) {
            return;
        }
        ScaleBean bean = (ScaleBean) renderBean;
        setScaleRatio(bean.getScaleRatio());
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uScaleLocation = GLES20.glGetUniformLocation(getProgram(), "uScale");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glUniform1f(uScaleLocation, scaleRatio);
    }

    public void setScaleRatio(float scaleRatio) {
        this.scaleRatio = scaleRatio;
    }
}
