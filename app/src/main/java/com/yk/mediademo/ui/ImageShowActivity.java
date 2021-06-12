package com.yk.mediademo.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.filter.GaussianBlurBean;
import com.yk.media.opengles.render.manager.RenderManager;
import com.yk.media.opengles.view.ImageView;
import com.yk.mediademo.R;

public class ImageShowActivity extends AppCompatActivity implements IActivityInit {

    private ImageView imageView;
    private AppCompatButton btnFilter;

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
        btnFilter = findViewById(R.id.btnFilter);
    }

    @Override
    public void initData() {
        fileName = "image/Eason.jpg";
    }

    @Override
    public void bindEvent() {
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        RenderManager.getInstance(ImageShowActivity.this).setFilter(RenderConstants.Process.IMAGE,
                                new GaussianBlurBean(1, 3));
                    }
                });
                imageView.requestRender();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageView.setAssetsFileName(fileName);
    }
}
