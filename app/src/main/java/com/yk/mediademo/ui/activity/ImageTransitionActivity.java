package com.yk.mediademo.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.view.TransitionView;
import com.yk.mediademo.R;
import com.yk.mediademo.data.adapter.TransitionAdapter;
import com.yk.mediademo.ui.base.IActivityInit;
import com.yk.mediademo.ui.widget.TransitionListView;

public class ImageTransitionActivity extends AppCompatActivity implements IActivityInit {
    private TransitionView transitionView;
    private TransitionListView transitionListView;

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
        transitionListView = findViewById(R.id.transitionListView);
    }

    @Override
    public void initData() {
        fileName = "image/Eason.jpg";
        fileName2 = "image/Eason2.jpg";
    }

    @Override
    public void bindEvent() {
        transitionListView.setOnClickTransitionListener(new TransitionAdapter.OnClickTransitionListener() {
            @Override
            public void onClickTransition(BaseRenderBean bean) {
                transitionView.start(bean);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        transitionView.setAssetsFileName(fileName, fileName2);
    }
}
