package com.yk.mediademo.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.media.core.photo.OnPhotoListener;
import com.yk.media.core.photo.PhotoHelper;
import com.yk.media.opengles.view.CameraView;
import com.yk.media.utils.OpenGLESUtils;
import com.yk.mediademo.R;

import java.io.File;
import java.io.FileNotFoundException;

public class TakePhotoActivity extends AppCompatActivity implements IActivityInit {

    private CameraView cameraView;
    private AppCompatButton btnTakePhoto;

    private final PhotoHelper photoHelper = new PhotoHelper();

    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        findView();
        initData();
        bindEvent();
    }


    @Override
    public void findView() {
        cameraView = findViewById(R.id.cameraView);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
    }

    @Override
    public void initData() {
        initPath();
        photoHelper.attachCameraView(cameraView);
    }

    private void initPath() {
        path = getExternalFilesDir("photo").getPath() + File.separator + System.currentTimeMillis() + "_photo.jpeg";

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void bindEvent() {
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoHelper.takePhoto(
                        new OnPhotoListener() {
                            @Override
                            public void onPhoto(Bitmap bitmap) {
                                boolean save = OpenGLESUtils.saveBitmapForJpeg(path, bitmap);
                                showSaveState(save);
                            }
                        }
                );
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.closeCamera();
    }

    private void showSaveState(boolean save) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(
                        TakePhotoActivity.this,
                        "Save:" + save + " path:" + path,
                        Toast.LENGTH_SHORT
                ).show();
                notifyAlbum();
                initPath();
            }
        });
    }

    private void notifyAlbum() {
        File file = new File(path);

        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), null);

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
