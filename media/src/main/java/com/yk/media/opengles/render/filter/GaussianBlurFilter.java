package com.yk.media.opengles.render.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.base.BaseRender;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.bean.filter.GaussianBlurBean;

public class GaussianBlurFilter extends BaseFilter {
    private final Scale scale;
    private final Blur horizontal;
    private final Blur vertical;

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
    public void setRenderBean(BaseRenderBean renderBean) {
        super.setRenderBean(renderBean);
        if (!(renderBean instanceof GaussianBlurBean)) {
            return;
        }
        GaussianBlurBean bean = (GaussianBlurBean) renderBean;
        scale.setScaleRatio(bean.getScaleRatio());
        horizontal.setBlurRadius(bean.getBlurRadius(), 0);
        vertical.setBlurRadius(0, bean.getBlurRadius());
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
    public int getFboTextureId() {
        return vertical.getFboTextureId();
    }

    private static class Scale extends BaseRender {
        private int scaleRatio;

        public Scale(Context context) {
            super(context);
            setScaleRatio(1);
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
        private static final int BLUR_CORE = 5;

        private int uBlurRadiusLocation;
        private int uWeightLocation;

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
            // 设置模糊半径
            setBlurRadius(0, 0);

            // 计算权重
            float sigma = 3f; // 标准差
            float sumWeight = 0; // 权重和
            for (int i = 0; i < BLUR_CORE; i++) {
                float weight = (float) ((1 / Math.sqrt(2 * Math.PI) * sigma) * Math.pow(Math.E, -Math.pow(i, 2) / (2 * Math.pow(sigma, 2))));
                sumWeight += weight;
                weights[i] = weight;
            }
            // 权重归一化
            for (int i = 0; i < BLUR_CORE; i++) {
                weights[i] /= sumWeight;
            }
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

        public void setBlurRadius(int width, int height) {
            blurRadiusW = width;
            blurRadiusH = height;
        }
    }
}
