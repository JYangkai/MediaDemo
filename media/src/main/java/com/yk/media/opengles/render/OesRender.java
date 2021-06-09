package com.yk.media.opengles.render;

import android.content.Context;
import android.graphics.SurfaceTexture;

import com.yk.media.opengles.render.base.BaseOesRender;
import com.yk.media.opengles.render.base.BaseRender;
import com.yk.media.opengles.render.base.OnSurfaceTextureListener;

public class OesRender implements Renderer {
    /**
     * 输入（FBO保存数据）
     */
    private BaseOesRender inputRender;

    /**
     * 输出（屏幕显示）
     */
    private BaseRender outputRender;

    public OesRender(Context context) {
        inputRender = new BaseOesRender(context);
        outputRender = new BaseRender(context);
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
    public void onDraw() {
        inputRender.onDrawSelf();
        outputRender.onDraw(inputRender.getFboTextureId());
    }

    public void setOesSize(int width, int height) {
        inputRender.setOesSize(width, height);
    }

    public void setOnSurfaceTextureListener(OnSurfaceTextureListener onSurfaceTextureListener) {
        inputRender.setOnSurfaceTextureListener(onSurfaceTextureListener);
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        inputRender.setSurfaceTexture(surfaceTexture);
    }

    public int getFboTextureId() {
        return inputRender.getFboTextureId();
    }
}
