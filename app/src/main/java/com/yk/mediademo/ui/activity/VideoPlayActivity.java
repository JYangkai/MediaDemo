package com.yk.mediademo.ui.activity;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yk.media.core.videoplay.VideoPlayer;
import com.yk.mediademo.R;
import com.yk.mediademo.ui.base.IActivityInit;
import com.yk.mediademo.utils.FolderUtils;

import java.io.File;

public class VideoPlayActivity extends AppCompatActivity implements IActivityInit {

    private TextureView preview;

    private final VideoPlayer videoPlayer = new VideoPlayer();

    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        findView();
        initData();
        bindEvent();
    }

    @Override
    public void findView() {
        preview = findViewById(R.id.preview);
    }

    @Override
    public void initData() {
        File file = new File(FolderUtils.getVideoFolderPath(this));
        if (!file.isDirectory()) {
            return;
        }
        File[] files = file.listFiles();
        if (files == null || files.length <= 0) {
            return;
        }
        path = files[0].getAbsolutePath();
    }

    @Override
    public void bindEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preview.isAvailable()) {
            startPlay(new Surface(preview.getSurfaceTexture()));
        } else {
            preview.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                    startPlay(new Surface(surface));
                }

                @Override
                public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlay();
    }

    private void startPlay(Surface surface) {
        videoPlayer.stat(path, surface);
    }

    private void stopPlay() {
        videoPlayer.stop();
    }
}
