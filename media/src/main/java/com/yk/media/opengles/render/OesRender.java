package com.yk.media.opengles.render;

import android.content.Context;
import android.graphics.SurfaceTexture;

import com.yk.media.opengles.render.base.BaseOesRender;
import com.yk.media.opengles.render.base.BaseRender;
import com.yk.media.opengles.render.base.OnSurfaceTextureListener;
import com.yk.media.opengles.render.manager.RenderManager;

public class OesRender implements Renderer {
    private Context context;
    private int process;

    /**
     * 输入（FBO保存数据）
     */
    private BaseOesRender inputRender;

    /**
     * 输出（屏幕显示）
     */
    private BaseRender outputRender;

    private int width;
    private int height;

    public OesRender(Context context, int process) {
        this.context = context;
        this.process = process;
        inputRender = new BaseOesRender(context);
        outputRender = new BaseRender(context);
    }

    @Override
    public void onCreate() {
        inputRender.onCreate();
        RenderManager.getInstance(context).onCreate(process);
        outputRender.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        this.width = width;
        this.height = height;
        inputRender.onChange(width, height);
        RenderManager.getInstance(context).onChange(process);
        outputRender.onChange(width, height);
    }

    @Override
    public void onDraw() {
        inputRender.onDrawSelf();
        int fboTextureId = inputRender.getFboTextureId();
        fboTextureId = RenderManager.getInstance(context).onDraw(process, fboTextureId, width, height);
        outputRender.onDraw(fboTextureId);
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
