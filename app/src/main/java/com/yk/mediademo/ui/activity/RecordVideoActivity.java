package com.yk.mediademo.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.media.core.record.video.OnRecordListener;
import com.yk.media.core.record.video.VideoRecorder;
import com.yk.media.opengles.render.RenderConstants;
import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.opengles.render.manager.RenderManager;
import com.yk.media.opengles.view.CameraView;
import com.yk.mediademo.R;
import com.yk.mediademo.data.adapter.FilterAdapter;
import com.yk.mediademo.ui.base.IActivityInit;
import com.yk.mediademo.ui.widget.FilterView;
import com.yk.mediademo.utils.FolderUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class RecordVideoActivity extends AppCompatActivity implements IActivityInit {

    private CameraView cameraView;
    private FilterView filterView;
    private AppCompatButton btnSwitch;
    private AppCompatButton btnRecord;

    private final VideoRecorder videoRecorder = new VideoRecorder();

    private String path;

    private boolean isRecord = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        findView();
        initData();
        bindEvent();
    }

    @Override
    public void findView() {
        cameraView = findViewById(R.id.cameraView);
        filterView = findViewById(R.id.filterView);
        btnSwitch = findViewById(R.id.btnSwitch);
        btnRecord = findViewById(R.id.btnRecord);
    }

    @Override
    public void initData() {
        initPath();
        videoRecorder.attachCameraView(cameraView);
    }

    private void initPath() {
        path = FolderUtils.getVideoFolderPath(this) + System.currentTimeMillis() + "_video.mp4";

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void bindEvent() {
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecord) {
                    stopRecord();
                    btnRecord.setText("Record");
                } else {
                    startRecord();
                    btnRecord.setText("Stop");
                }
                isRecord = !isRecord;
            }
        });
        videoRecorder.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onRecordStart() {

            }

            @Override
            public void onRecordTime(long time) {

            }

            @Override
            public void onRecordComplete(String path) {
                showSaveState(true);
            }

            @Override
            public void onRecordCancel() {

            }

            @Override
            public void onRecordError(Exception e) {

            }
        });
        filterView.setOnClickFilterListener(new FilterAdapter.OnClickFilterListener() {
            @Override
            public void onClickFilter(BaseRenderBean filter) {
                cameraView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        RenderManager.getInstance(RecordVideoActivity.this).setFilter(RenderConstants.Process.CAMERA, filter);
                        RenderManager.getInstance(RecordVideoActivity.this).setFilter(RenderConstants.Process.RECORD_VIDEO, filter);
                    }
                });
                cameraView.requestRender();
            }
        });
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.switchCamera();
            }
        });
    }

    private void stopRecord() {
        videoRecorder.stop();
    }

    private void startRecord() {
        videoRecorder.startWithDefaultParams(path);
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
        videoRecorder.cancel();
    }

    private void showSaveState(boolean save) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(
                        RecordVideoActivity.this,
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
