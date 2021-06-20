package com.yk.media.opengles.render.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.bean.filter.GaussianBlurBean;
import com.yk.media.opengles.render.bean.filter.ScaleBean;
import com.yk.media.utils.FilterUtils;

public class PipFilter extends BaseFilter {
    private int uSampler2Location;

    private int scaleBlurTextureId;

    private GaussianBlurFilter gaussianBlurFilter;
    private ScaleFilter scaleFilter;

    public PipFilter(Context context) {
        super(
                context,
                "render/filter/pip/vertex.frag",
                "render/filter/pip/frag.frag"
        );

        gaussianBlurFilter = (GaussianBlurFilter) FilterUtils.getFilter(
                context, new GaussianBlurBean(2, 30, 3, 3)
        );
        scaleFilter = (ScaleFilter) FilterUtils.getFilter(
                context, new ScaleBean("放大", 2)
        );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gaussianBlurFilter.onCreate();
        scaleFilter.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        super.onChange(width, height);
        gaussianBlurFilter.onChange(width, height);
        scaleFilter.onChange(width, height);
    }

    @Override
    public void onDraw(int textureId) {
        gaussianBlurFilter.onDraw(textureId);
        scaleFilter.onDraw(gaussianBlurFilter.getFboTextureId());
        scaleBlurTextureId = scaleFilter.getFboTextureId();
        super.onDraw(textureId);
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uSampler2Location = GLES20.glGetUniformLocation(getProgram(), "uSampler2");
    }

    @Override
    public void onActiveTexture(int textureId) {
        super.onActiveTexture(textureId);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, scaleBlurTextureId);
        GLES20.glUniform1i(uSampler2Location, 1);
    }

    @Override
    public void onRelease() {
        super.onRelease();
        scaleFilter.onRelease();
        gaussianBlurFilter.onRelease();
    }
}
