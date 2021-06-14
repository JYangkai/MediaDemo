package com.yk.mediademo.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.manager.RenderManager;
import com.yk.media.opengles.view.ImageView;
import com.yk.mediademo.R;
import com.yk.mediademo.data.adapter.FilterAdapter;
import com.yk.mediademo.ui.base.IActivityInit;
import com.yk.mediademo.ui.widget.FilterView;

public class ImageShowActivity extends AppCompatActivity implements IActivityInit {

    private ImageView imageView;
    private FilterView filterView;

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
        filterView = findViewById(R.id.filterView);
    }

    @Override
    public void initData() {
        fileName = "image/Eason.jpg";
    }

    @Override
    public void bindEvent() {
        filterView.setOnClickFilterListener(new FilterAdapter.OnClickFilterListener() {
            @Override
            public void onClickFilter(BaseRenderBean filter) {
                imageView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        RenderManager.getInstance(ImageShowActivity.this).setFilter(RenderConstants.Process.IMAGE,
                                filter);
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
