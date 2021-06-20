package com.yk.media.opengles.view;

import android.content.Context;
import android.util.AttributeSet;

import com.yk.media.opengles.render.TransitionRender;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.transition.manager.TransitionManager;
import com.yk.media.opengles.view.base.EGLTextureView;

import java.util.Timer;
import java.util.TimerTask;

public class TransitionView extends EGLTextureView {
    private static final long DURATION = 1000;

    private TransitionRender render;

    public TransitionView(Context context) {
        this(context, null);
    }

    public TransitionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        render = new TransitionRender(context);
        setRenderer(render);
        setRenderMode(EGLTextureView.RENDERMODE_WHEN_DIRTY);
    }

    public void setPath(String path, String path2) {
        render.setPath(path, path2);
        requestRender();
    }

    public void setAssetsFileName(String fileName, String fileName2) {
        render.setAssetsFileName(fileName, fileName2);
        requestRender();
    }

    private Timer timer;

    public void start(BaseRenderBean bean) {
        stop();
        queueEvent(new Runnable() {
            @Override
            public void run() {
                TransitionManager.getInstance(getContext()).setTransition(bean);
            }
        });
        requestRender();
        long startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long time = System.currentTimeMillis() - startTime;

                if (time >= DURATION) {
                    stop();
                    return;
                }

                time = time % DURATION;

                float progress = (float) time / DURATION;

                TransitionManager.getInstance(getContext()).setProgress(progress);
                requestRender();
            }
        }, 0, 1000 / 60);
    }

    public void stop() {
        if (timer == null) {
            return;
        }
        timer.cancel();
        timer = null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        TransitionManager.getInstance(getContext()).init();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        TransitionManager.getInstance(getContext()).release();
    }
}
