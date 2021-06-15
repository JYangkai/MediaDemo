package com.yk.media.opengles.render.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.bean.filter.GaussianBlurBean;
import com.yk.media.utils.FilterUtils;

public class PipFilter extends BaseFilter {
    private int uSampler2Location;

    private int blurTextureId;

    private GaussianBlurFilter gaussianBlurFilter;

    public PipFilter(Context context) {
        super(
                context,
                "render/filter/pip/vertex.frag",
                "render/filter/pip/frag.frag"
        );

        gaussianBlurFilter = (GaussianBlurFilter) FilterUtils.getFilter(
                context, new GaussianBlurBean(2, 5)
        );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gaussianBlurFilter.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        super.onChange(width, height);
        gaussianBlurFilter.onChange(width, height);
    }

    @Override
    public void onDraw(int textureId) {
        gaussianBlurFilter.onDraw(textureId);
        blurTextureId = gaussianBlurFilter.getFboTextureId();
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
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, blurTextureId);
        GLES20.glUniform1i(uSampler2Location, 1);
    }
}
