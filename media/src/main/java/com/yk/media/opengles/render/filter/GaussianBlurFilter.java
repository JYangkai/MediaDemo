package com.yk.media.opengles.render.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.base.BaseRender;
import com.yk.media.opengles.render.bean.BaseRenderBean;
import com.yk.media.opengles.render.bean.GaussianBlurBean;

public class GaussianBlurFilter extends BaseRender {
    private Scale scale;
    private Blur horizontal;
    private Blur vertical;

    public GaussianBlurFilter(Context context) {
        super(context);
        scale = new Scale(context);
        horizontal = new Blur(context);
        vertical = new Blur(context);

        scale.setBindFbo(true);
        horizontal.setBindFbo(true);
        vertical.setBindFbo(true);
    }

    @Override
    public void onCreate() {
        scale.onCreate();
        horizontal.onCreate();
        vertical.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        scale.onChange(width, height);
        horizontal.onChange(width, height);
        vertical.onChange(width, height);
    }

    @Override
    public void onDraw(int textureId) {
        scale.onDraw(textureId);
        horizontal.onDraw(scale.getFboTextureId());
        vertical.onDraw(horizontal.getFboTextureId());
    }

    @Override
    public void setRenderBean(BaseRenderBean renderBean) {
        super.setRenderBean(renderBean);
        GaussianBlurBean gaussianBlurBean = (GaussianBlurBean) renderBean;
        scale.setScaleRatio(gaussianBlurBean.getScaleRatio());
        horizontal.setBlurSize(gaussianBlurBean.getBlurRadius(), 0);
        vertical.setBlurSize(0, gaussianBlurBean.getBlurRadius());
    }

    @Override
    public int getFboTextureId() {
        return vertical.getFboTextureId();
    }

    private static class Scale extends BaseRender {
        private int scaleRatio = 1;

        public Scale(Context context) {
            super(context);
        }

        @Override
        public void onChange(int width, int height) {
            super.onChange(width / scaleRatio, height / scaleRatio);
        }

        public void setScaleRatio(int scaleRatio) {
            this.scaleRatio = scaleRatio;
        }
    }

    private static class Blur extends BaseRender {
        private int uWidthOffsetLocation;
        private int uHeightOffsetLocation;

        private float blurW;
        private float blurH;

        public Blur(Context context) {
            super(
                    context,
                    "render/filter/gaussian_blur/vertex.frag",
                    "render/filter/gaussian_blur/frag.frag"
            );
        }

        @Override
        public void onInitLocation() {
            super.onInitLocation();
            uWidthOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "uWidthOffset");
            uHeightOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "uHeightOffset");
        }

        @Override
        public void onSetOtherData() {
            super.onSetOtherData();
            GLES20.glUniform1f(uWidthOffsetLocation, blurW / getWidth());
            GLES20.glUniform1f(uHeightOffsetLocation, blurH / getHeight());
        }

        public void setBlurSize(int width, int height) {
            this.blurW = width;
            this.blurH = height;
        }
    }
}
