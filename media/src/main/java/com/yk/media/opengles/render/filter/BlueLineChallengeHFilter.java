package com.yk.media.opengles.render.filter;

import android.content.Context;

import com.yk.media.opengles.render.other.MoveLineHorizontalRender;
import com.yk.media.opengles.render.other.RetainFrameHorizontalRender;

public class BlueLineChallengeHFilter extends BaseFilter {
    private final RetainFrameHorizontalRender inputRender;

    private final MoveLineHorizontalRender outputRender;

    public BlueLineChallengeHFilter(Context context) {
        super(context);

        inputRender = new RetainFrameHorizontalRender(context);
        inputRender.setBindFbo(true);

        outputRender = new MoveLineHorizontalRender(context);
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
