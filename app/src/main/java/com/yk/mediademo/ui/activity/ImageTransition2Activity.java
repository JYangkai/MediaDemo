package com.yk.mediademo.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.view.Transition2View;
import com.yk.mediademo.R;
import com.yk.mediademo.data.adapter.TransitionAdapter;
import com.yk.mediademo.ui.base.IActivityInit;
import com.yk.mediademo.ui.widget.Transition2ListView;

public class ImageTransition2Activity extends AppCompatActivity implements IActivityInit {
    private Transition2View transitionView;
    private Transition2ListView transitionListView;

    private String fileName;
    private String fileName2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_transition_2);
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
        fileName = "image/Eason3.jpg";
        fileName2 = "image/Eason4.jpg";
    }

    @Override
    public void bindEvent() {
        transitionListView.setOnClickTransitionListener(new TransitionAdapter.OnClickTransitionListener() {
            @Override
            public void onClickTransition(BaseRenderBean bean) {
                transitionView.start(bean);
            }
        });
        transitionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transitionListView.getVisibility() == View.VISIBLE) {
                    transitionListView.setVisibility(View.GONE);
                } else {
                    transitionListView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        transitionView.setAssetsFileName(fileName, fileName2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        transitionView.stop();
    }
}
