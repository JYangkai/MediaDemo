package com.yk.mediademo.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.mediademo.R;
import com.yk.mediademo.data.adapter.VideoAdapter;
import com.yk.mediademo.data.bean.Video;
import com.yk.mediademo.ui.base.IActivityInit;
import com.yk.mediademo.utils.LocalMediaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoListActivity extends AppCompatActivity implements IActivityInit {
    private RecyclerView rvVideo;

    private final List<Video> videoList = new ArrayList<>();
    private VideoAdapter videoAdapter;

    private ExecutorService executorService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        findView();
        initData();
        bindEvent();
    }

    @Override
    public void findView() {
        rvVideo = findViewById(R.id.rvVideo);
    }

    @Override
    public void initData() {
        executorService = Executors.newSingleThreadExecutor();
        initRvVideo();
    }

    private void initRvVideo() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(VideoListActivity.this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        videoAdapter = new VideoAdapter(videoList);
        rvVideo.setLayoutManager(gridLayoutManager);
        rvVideo.setAdapter(videoAdapter);
    }

    @Override
    public void bindEvent() {
        videoAdapter.setOnItemClickVideoListener(new VideoAdapter.OnItemClickVideoListener() {
            @Override
            public void onItemClickVideo(Video video) {
                Intent intent = new Intent(VideoListActivity.this, VideoShowActivity.class);
                intent.putExtra(VideoShowActivity.EXTRA_VIDEO, video);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVideo();
    }

    private void loadVideo() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Video> list = LocalMediaUtils.getVideoList(VideoListActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateVideo(list);
                    }
                });
            }
        });
    }

    private void updateVideo(List<Video> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        videoList.clear();
        videoList.addAll(list);
        videoAdapter.notifyDataSetChanged();
    }
}
