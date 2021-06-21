package com.yk.media.opengles.render.transition;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.filter.SkewFilter;
import com.yk.media.opengles.render.transition.base.BaseTransition;

public class VortexTransition extends BaseTransition {
    private int uAlphaLocation;

    private SkewFilter skewFilter;

    public VortexTransition(Context context) {
        super(
                context,
                "render/transition/vortex/vertex.frag",
                "render/transition/vortex/frag.frag"
        );
        skewFilter = new SkewFilter(context);

        skewFilter.setBindFbo(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        skewFilter.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        super.onChange(width, height);
        skewFilter.onChange(width, height);
    }

    @Override
    public void onDrawTransition(int textureId, int textureId2) {
        super.onDrawTransition(textureId, textureId2);
        skewFilter.onDraw(super.getFboTextureId());
    }

    @Override
    public int getFboTextureId() {
        return skewFilter.getFboTextureId();
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uAlphaLocation = GLES20.glGetUniformLocation(getProgram(), "uAlpha");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        float progress = getProgress();

        float alpha = progress;
        float rotate;
        if (progress < 0.5) {
            rotate = (float) (progress * 2 * Math.PI / 2);
        } else {
            rotate = (float) ((1 - progress) * 2 * Math.PI / 2);
        }

        skewFilter.setRotate(rotate);

        GLES20.glUniform1f(uAlphaLocation, alpha);
    }
}
