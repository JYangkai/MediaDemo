package com.yk.mediademo.ui.activity;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.media.core.camera.CameraManager;
import com.yk.mediademo.R;
import com.yk.mediademo.ui.base.IActivityInit;

public class TextureViewCameraActivity extends AppCompatActivity implements IActivityInit {
    private TextureView textureView;
    private AppCompatButton btnSwitch;

    private final CameraManager cameraManager = new CameraManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view_camera);
        findView();
        initData();
        bindEvent();
    }

    @Override
    public void findView() {
        textureView = findViewById(R.id.textureView);
        btnSwitch = findViewById(R.id.btnSwitch);
    }

    @Override
    public void initData() {

    }

    @Override
    public void bindEvent() {
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
    }

    private void openCamera() {
        if (textureView.isAvailable()) {
            cameraManager.openCamera(TextureViewCameraActivity.this, textureView.getSurfaceTexture());
        } else {
            textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                    cameraManager.openCamera(TextureViewCameraActivity.this, surface);
                }

                @Override
                public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

                }
            });
        }
    }

    private void closeCamera() {
        cameraManager.closeCamera();
    }

    private void switchCamera() {
        cameraManager.switchCamera();
        openCamera();
    }
}
