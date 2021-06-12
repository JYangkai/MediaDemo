package com.yk.mediademo.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yk.media.opengles.view.ImageView;
import com.yk.mediademo.R;

public class ImageShowActivity extends AppCompatActivity implements IActivityInit {

    private ImageView imageView;

    private String fileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        findView();
        initData();
        bindEvent();
    }

    @Override
    public void findView() {
        imageView = findViewById(R.id.imageView);
    }

    @Override
    public void initData() {
        fileName = "image/Eason.jpg";
    }

    @Override
    public void bindEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        imageView.setAssetsFileName(fileName);
    }
}
