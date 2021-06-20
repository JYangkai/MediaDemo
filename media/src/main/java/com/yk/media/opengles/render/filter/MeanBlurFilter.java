package com.yk.media.opengles.render.filter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.yk.media.opengles.render.base.BaseRender;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.bean.filter.MeanBlurBean;

public class MeanBlurFilter extends BaseFilter {
    private static final String TAG = "MeanBlurFilter";

    private final Blur horizontal;
    private final Blur vertical;
    private final BaseRender output;

    public MeanBlurFilter(Context context) {
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
        if (!(renderBean instanceof MeanBlurBean)) {
            return;
        }
        MeanBlurBean bean = (MeanBlurBean) renderBean;

        horizontal.setScaleRatio(bean.getScaleRatio());
        vertical.setScaleRatio(bean.getScaleRatio());

        horizontal.setBlurRadius(bean.getBlurRadius());
        vertical.setBlurRadius(bean.getBlurRadius());

        horizontal.setBlurOffset(bean.getBlurOffsetW(), 0);
        vertical.setBlurOffset(0, bean.getBlurOffsetH());
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
        private int uBlurRadiusLocation;
        private int uBlurOffsetLocation;

        private int scaleRatio;
        private int blurRadius;
        private float blurOffsetW;
        private float blurOffsetH;

        public Blur(Context context) {
            super(
                    context,
                    "render/filter/mean_blur/vertex.frag",
                    "render/filter/mean_blur/frag.frag"
            );
            initData();
        }

        private void initData() {
            // 设置缩放因子
            setScaleRatio(1);
            // 设置模糊半径
            setBlurRadius(0);
            // 设置模糊步长
            setBlurOffset(0, 0);
        }

        @Override
        public void onChange(int width, int height) {
            super.onChange(width / scaleRatio, height / scaleRatio);
        }

        @Override
        public void onInitLocation() {
            super.onInitLocation();
            uBlurRadiusLocation = GLES20.glGetUniformLocation(getProgram(), "uBlurRadius");
            uBlurOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "uBlurOffset");
        }

        @Override
        public void onSetOtherData() {
            super.onSetOtherData();
            Log.d(TAG, "onSetOtherData: blurRadius:" + blurRadius + " w:" + blurOffsetW + " h:" + blurOffsetH);
            GLES20.glUniform1i(uBlurRadiusLocation, blurRadius);
            GLES20.glUniform2f(uBlurOffsetLocation, blurOffsetW / getWidth(), blurOffsetH / getHeight());
        }

        public void setScaleRatio(int scaleRatio) {
            this.scaleRatio = scaleRatio;
        }

        public void setBlurRadius(int blurRadius) {
            this.blurRadius = blurRadius;
        }

        public void setBlurOffset(float width, float height) {
            this.blurOffsetW = width;
            this.blurOffsetH = height;
        }
    }
}
