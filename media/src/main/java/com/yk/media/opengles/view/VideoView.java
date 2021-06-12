package com.yk.media.opengles.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;

import com.yk.media.core.play.OnPlayListener;
import com.yk.media.core.play.PlayManager;
import com.yk.media.data.base.Size;
import com.yk.media.opengles.render.OesRender;
import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.base.OnSurfaceTextureListener;
import com.yk.media.opengles.render.manager.RenderManager;
import com.yk.media.opengles.view.base.EGLTextureView;

public class VideoView extends EGLTextureView implements OnPlayListener {
    private final PlayManager playManager = new PlayManager();

    public OesRender render;

    private final int process = RenderConstants.Process.VIDEO;

    public VideoView(Context context) {
        this(context, null);
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        render = new OesRender(context, process);
        setRenderer(render);
        setRenderMode(EGLTextureView.RENDERMODE_WHEN_DIRTY);

        playManager.addOnPlayListener(this);
    }

    public void play(String path) {
        render.setOnSurfaceTextureListener(new OnSurfaceTextureListener() {
            @Override
            public void onSurfaceTexture(SurfaceTexture surfaceTexture) {
                surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                    @Override
                    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                        requestRender();
                    }
                });
                playManager.play(path, surfaceTexture);
            }
        });
        requestRender();
    }

    public void pause() {
        playManager.pause();
    }

    public void stop() {
        playManager.stop();
    }

    public void continuePlay() {
        playManager.continuePlay();
    }

    public void seekTo(int time) {
        playManager.seekTo(time);
    }

    public void addOnPlayListener(OnPlayListener onPlayListener) {
        playManager.addOnPlayListener(onPlayListener);
    }

    @Override
    public void onStartPlay(Size videoSize, long duration) {
        render.setOesSize(videoSize.getWidth(), videoSize.getHeight());
        requestRender();
    }

    @Override
    public void onPausePlay(long currentPos) {

    }

    @Override
    public void onStopPlay() {

    }

    @Override
    public void onPlayCompletion() {

    }

    @Override
    public void onErrorPlay(Exception e) {

    }

    public PlayManager getPlayManager() {
        return playManager;
    }

    public int getFboTextureId() {
        return render.getFboTextureId();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        RenderManager.getInstance(getContext()).init(process);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RenderManager.getInstance(getContext()).release(process);
    }
}
