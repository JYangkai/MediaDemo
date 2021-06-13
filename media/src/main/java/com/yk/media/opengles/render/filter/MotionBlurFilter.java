package com.yk.media.opengles.render.filter;

import android.content.Context;
import android.opengl.GLES20;

public class MotionBlurFilter extends BaseFilter {
    private static final int BLUR_CORE = 5;

    private int uWeightLocation;

    private final float[] weights = new float[BLUR_CORE];

    public MotionBlurFilter(Context context) {
        super(
                context,
                "render/filter/motion_blur/vertex.frag",
                "render/filter/motion_blur/frag.frag"
        );
        initData();
    }

    private void initData() {
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
        uWeightLocation = GLES20.glGetUniformLocation(getProgram(), "uWeight");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glUniform1fv(uWeightLocation, weights.length, weights, 0);
    }
}
