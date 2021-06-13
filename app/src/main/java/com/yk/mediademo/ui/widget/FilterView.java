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
import com.yk.media.utils.FilterUtils;
import com.yk.mediademo.R;
import com.yk.mediademo.data.adapter.FilterAdapter;

import java.util.ArrayList;
import java.util.List;

public class FilterView extends FrameLayout {
    private RecyclerView rvFilter;

    private List<BaseRenderBean> filterList = new ArrayList<>();
    private FilterAdapter filterAdapter;

    private FilterAdapter.OnClickFilterListener onClickFilterListener;

    public FilterView(@NonNull Context context) {
        this(context, null);
    }

    public FilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_filter, this);
        findView();
        initData();
        bindEvent();
    }

    private void findView() {
        rvFilter = findViewById(R.id.rvFilter);
    }

    private void initData() {
        filterList = FilterUtils.FILTER_LIST;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        filterAdapter = new FilterAdapter(filterList);
        rvFilter.setLayoutManager(linearLayoutManager);
        rvFilter.setAdapter(filterAdapter);
    }

    private void bindEvent() {
        filterAdapter.setOnClickFilterListener(new FilterAdapter.OnClickFilterListener() {
            @Override
            public void onClickFilter(BaseRenderBean filter) {
                if (onClickFilterListener != null) {
                    onClickFilterListener.onClickFilter(filter);
                }
            }
        });
    }

    public void setOnClickFilterListener(FilterAdapter.OnClickFilterListener onClickFilterListener) {
        this.onClickFilterListener = onClickFilterListener;
    }
}
