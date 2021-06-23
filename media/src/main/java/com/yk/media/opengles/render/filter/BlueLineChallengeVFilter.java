package com.yk.media.opengles.render.filter;

import android.content.Context;

import com.yk.media.opengles.render.other.MoveLineVerticalRender;
import com.yk.media.opengles.render.other.RetainFrameVerticalRender;

public class BlueLineChallengeVFilter extends BaseFilter {
    private final RetainFrameVerticalRender inputRender;

    private final MoveLineVerticalRender outputRender;

    public BlueLineChallengeVFilter(Context context) {
        super(context);

        inputRender = new RetainFrameVerticalRender(context);
        inputRender.setBindFbo(true);

        outputRender = new MoveLineVerticalRender(context);
        outputRender.setBindFbo(true);

        timeStart(15000);
    }

    @Override
    public void onCreate() {
        inputRender.onCreate();
        outputRender.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        inputRender.onChange(width, height);
        outputRender.onChange(width, height);
    }

    @Override
    public void onDraw(int textureId) {
        float progress = getProgress();

        inputRender.setOffset(progress);
        outputRender.setOffset(progress);

        inputRender.onDraw(textureId);
        outputRender.onDraw(inputRender.getFboTextureId());
    }

    @Override
    public int getFboTextureId() {
        return outputRender.getFboTextureId();
    }

    @Override
    public void onRelease() {
        super.onRelease();
        inputRender.onRelease();
        outputRender.onRelease();
    }
}
