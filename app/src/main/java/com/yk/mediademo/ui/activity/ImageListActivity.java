package com.yk.mediademo.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.mediademo.R;
import com.yk.mediademo.data.adapter.ImageAdapter;
import com.yk.mediademo.data.bean.Image;
import com.yk.mediademo.ui.base.IActivityInit;
import com.yk.mediademo.utils.LocalMediaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageListActivity extends AppCompatActivity implements IActivityInit {
    private RecyclerView rvImage;

    private final List<Image> imageList = new ArrayList<>();
    private ImageAdapter imageAdapter;

    private ExecutorService executorService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        findView();
        initData();
        bindEvent();
    }

    @Override
    public void findView() {
        rvImage = findViewById(R.id.rvImage);
    }

    @Override
    public void initData() {
        executorService = Executors.newSingleThreadExecutor();
        initRvImage();
    }

    private void initRvImage() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ImageListActivity.this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        imageAdapter = new ImageAdapter(imageList);
        rvImage.setLayoutManager(gridLayoutManager);
        rvImage.setAdapter(imageAdapter);
    }

    @Override
    public void bindEvent() {
        imageAdapter.setOnItemClickImageListener(new ImageAdapter.OnItemClickImageListener() {
            @Override
            public void onItemClickImage(Image image) {
                Intent intent = new Intent(ImageListActivity.this, ImageShowActivity.class);
                intent.putExtra(ImageShowActivity.EXTRA_IMAGE, image);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImage();
    }

    private void loadImage() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Image> list = LocalMediaUtils.getImageList(ImageListActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateImage(list);
                    }
                });
            }
        });
    }

    private void updateImage(List<Image> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        imageList.clear();
        imageList.addAll(list);
        imageAdapter.notifyDataSetChanged();
    }
}
