package com.yk.mediademo.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yk.media.opengles.view.TransitionView;
import com.yk.mediademo.R;
import com.yk.mediademo.ui.base.IActivityInit;

public class ImageTransitionActivity extends AppCompatActivity implements IActivityInit {

    private TransitionView transitionView;

    private String fileName;
    private String fileName2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_transition);
        findView();
        initData();
        bindEvent();
    }

    @Override
    public void findView() {
        transitionView = findViewById(R.id.transitionView);
    }

    @Override
    public void initData() {
        fileName = "image/Eason.jpg";
        fileName2 = "image/Eason2.jpg";
    }

    @Override
    public void bindEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        transitionView.setAssetsFileName(fileName,fileName2);
    }
}
