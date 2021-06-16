package com.yk.mediademo.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yk.media.opengles.view.VideoView;
import com.yk.mediademo.R;
import com.yk.mediademo.data.bean.Video;
import com.yk.mediademo.ui.base.IActivityInit;

public class VideoShowActivity extends AppCompatActivity implements IActivityInit {
    public static final String EXTRA_VIDEO = "extra_video";

    private VideoView videoView;

    private Video video;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_show);
        findView();
        initData();
        bindEvent();
    }

    @Override
    public void findView() {
        videoView = findViewById(R.id.videoView);
    }

    @Override
    public void initData() {
        video = (Video) getIntent().getSerializableExtra(EXTRA_VIDEO);
    }

    @Override
    public void bindEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (video != null) {
            videoView.play(video.getPath());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.stop();
    }
}
