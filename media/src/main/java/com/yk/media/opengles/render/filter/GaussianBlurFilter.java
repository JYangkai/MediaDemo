package com.yk.media.opengles.render.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.base.BaseRender;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.bean.filter.GaussianBlurBean;

public class GaussianBlurFilter extends BaseFilter {
    private final Blur horizontal;
    private final Blur vertical;
    private final BaseRender output;

    public GaussianBlurFilter(Context context) {
        super(context);
        horizontal = new Blur(context);
        vertical = new Blur(context);
        output = new BaseRender(context);

        horizontal.setBindFbo(true);
        vertical.setBindFbo(true);
        output.setBindFbo(true);
    }

    @Override
    public void setRenderBean(BaseRenderBean renderBean) {
        super.setRenderBean(renderBean);
        if (!(renderBean instanceof GaussianBlurBean)) {
            return;
        }
        GaussianBlurBean bean = (GaussianBlurBean) renderBean;

        horizontal.setScaleRatio(bean.getScaleRatio());
        vertical.setScaleRatio(bean.getScaleRatio());

        horizontal.setBlurRadius(bean.getBlurRadius(), 0);
        vertical.setBlurRadius(0, bean.getBlurRadius());
    }

    @Override
    public void onCreate() {
        horizontal.onCreate();
        vertical.onCreate();
        output.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        horizontal.onChange(width, height);
        vertical.onChange(width, height);
        output.onChange(width, height);
    }

    @Override
    public void onDraw(int textureId) {
        horizontal.onDraw(textureId);
        vertical.onDraw(horizontal.getFboTextureId());
        output.onDraw(vertical.getFboTextureId());
    }

    @Override
    public int getFboTextureId() {
        return output.getFboTextureId();
    }

    private static class Blur extends BaseRender {
        private static final int BLUR_CORE = 30;

        private int uBlurRadiusLocation;
        private int uWeightLocation;

        private int scaleRatio;

        private float blurRadiusW;
        private float blurRadiusH;

        private final float[] weights = new float[BLUR_CORE];

        public Blur(Context context) {
            super(
                    context,
                    "render/filter/gaussian_blur/vertex.frag",
                    "render/filter/gaussian_blur/frag.frag"
            );
            initData();
        }

        private void initData() {
            // 设置缩放因子
            setScaleRatio(1);
            // 设置模糊半径
            setBlurRadius(0, 0);

            // 计算权重
            float sigma = 10f; // 标准差
            float sumWeight = 0; // 权重和
            for (int i = 0; i < BLUR_CORE; i++) {
                float weight = (float) ((1 / Math.sqrt(2 * Math.PI) * sigma) * Math.pow(Math.E, -Math.pow(i, 2) / (2 * Math.pow(sigma, 2))));
                sumWeight += weight;
                if (i != 0) {
                    sumWeight += weight;
                }
                weights[i] = weight;
            }
            // 权重归一化
            for (int i = 0; i < BLUR_CORE; i++) {
                weights[i] /= sumWeight;
            }
        }

        @Override
        public void onChange(int width, int height) {
            super.onChange(width / scaleRatio, height / scaleRatio);
        }

        @Override
        public void onInitLocation() {
            super.onInitLocation();
            uBlurRadiusLocation = GLES20.glGetUniformLocation(getProgram(), "uBlurRadius");
            uWeightLocation = GLES20.glGetUniformLocation(getProgram(), "uWeight");
        }

        @Override
        public void onSetOtherData() {
            super.onSetOtherData();
            GLES20.glUniform2f(uBlurRadiusLocation, blurRadiusW / getWidth(), blurRadiusH / getHeight());
            GLES20.glUniform1fv(uWeightLocation, weights.length, weights, 0);
        }

        public void setScaleRatio(int scaleRatio) {
            this.scaleRatio = scaleRatio;
        }

        public void setBlurRadius(int width, int height) {
            blurRadiusW = width;
            blurRadiusH = height;
        }
    }
}
