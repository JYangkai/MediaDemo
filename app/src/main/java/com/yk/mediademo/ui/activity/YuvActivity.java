package com.yk.mediademo.ui.activity;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.media.core.yuv.YuvEncoder;
import com.yk.media.core.yuv.YuvRecord;
import com.yk.mediademo.R;
import com.yk.mediademo.ui.base.IActivityInit;
import com.yk.mediademo.utils.FolderUtils;

public class YuvActivity extends AppCompatActivity implements IActivityInit {

    private TextureView preview;

    private AppCompatButton btnRecord;
    private AppCompatButton btnStopRecord;

    private AppCompatButton btnEncode;
    private AppCompatButton btnStopEncode;

    private String path;
    private String encodePath;

    private final YuvRecord yuvRecord = new YuvRecord();
    private final YuvEncoder yuvEncoder = new YuvEncoder();

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
        preview = findViewById(R.id.preview);

        btnRecord = findViewById(R.id.btnRecord);
        btnStopRecord = findViewById(R.id.btnStopRecord);

        btnEncode = findViewById(R.id.btnEncode);
        btnStopEncode = findViewById(R.id.btnStopEncode);
    }

    @Override
    public void initData() {
        path = FolderUtils.getVideoFolderPath(this) + "record.yuv";
        encodePath = FolderUtils.getVideoFolderPath(this) + "encode.mp4";
    }

    @Override
    public void bindEvent() {
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuvRecord.start(path, YuvActivity.this);
            }
        });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuvRecord.stop();
            }
        });

        btnEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preview.isAvailable()) {
                    yuvEncoder.start(YuvActivity.this, preview.getSurfaceTexture(), encodePath);
                } else {
                    preview.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                        @Override
                        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                            yuvEncoder.start(YuvActivity.this, surface, encodePath);
                        }

                        @Override
                        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

                        }

                        @Override
                        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                            return false;
                        }

                        @Override
                        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

                        }
                    });
                }
            }
        });

        btnStopEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuvEncoder.stop();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        yuvEncoder.release();
    }
}
