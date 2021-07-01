package com.yk.mediademo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.utils.Transition2Utils;
import com.yk.media.utils.TransitionUtils;
import com.yk.mediademo.R;
import com.yk.mediademo.data.adapter.TransitionAdapter;

import java.util.ArrayList;
import java.util.List;

public class Transition2ListView extends FrameLayout {
    private RecyclerView rvTransition;

    private List<BaseRenderBean> list = new ArrayList<>();
    private TransitionAdapter transitionAdapter;

    private TransitionAdapter.OnClickTransitionListener onClickTransitionListener;

    public Transition2ListView(@NonNull Context context) {
        this(context, null);
    }

    public Transition2ListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Transition2ListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_transition, this);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        rvTransition = findViewById(R.id.rvTransition);
    }

    private void initData() {
        list = Transition2Utils.TRANSITION_LIST;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        transitionAdapter = new TransitionAdapter(list);
        rvTransition.setLayoutManager(linearLayoutManager);
        rvTransition.setAdapter(transitionAdapter);
    }

    private void bindEvent() {
        transitionAdapter.setOnClickTransitionListener(new TransitionAdapter.OnClickTransitionListener() {
            @Override
            public void onClickTransition(BaseRenderBean bean) {
                if (onClickTransitionListener != null) {
                    onClickTransitionListener.onClickTransition(bean);
                }
            }
        });
    }

    public void setOnClickTransitionListener(TransitionAdapter.OnClickTransitionListener onClickTransitionListener) {
        this.onClickTransitionListener = onClickTransitionListener;
    }
}
