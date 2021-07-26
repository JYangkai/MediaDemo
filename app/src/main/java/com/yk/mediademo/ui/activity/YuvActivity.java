package com.yk.mediademo.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.media.core.yuv.YuvRecord;
import com.yk.mediademo.R;
import com.yk.mediademo.ui.base.IActivityInit;
import com.yk.mediademo.utils.FolderUtils;

public class YuvActivity extends AppCompatActivity implements IActivityInit {

    private AppCompatButton btnRecord;
    private AppCompatButton btnStopRecord;

    private String path;

    private final YuvRecord yuvRecord = new YuvRecord();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuv);
        findView();
        initData();
        bindEvent();
    }

    @Override
    public void findView() {
        btnRecord = findViewById(R.id.btnRecord);
        btnStopRecord = findViewById(R.id.btnStopRecord);
    }

    @Override
    public void initData() {
        path = FolderUtils.getVideoFolderPath(this) + "record.yuv";
    }

    @Override
    public void bindEvent() {
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuvRecord.start(path, YuvActivity.this);
//                YuvRecord2.start(path, YuvActivity.this);
            }
        });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuvRecord.stop();
//                YuvRecord2.stop();
            }
        });
    }
}
