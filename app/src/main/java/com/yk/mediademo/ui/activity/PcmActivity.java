package com.yk.mediademo.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.media.core.pcm.PcmPlayer;
import com.yk.media.core.pcm.PcmRecorder;
import com.yk.mediademo.R;
import com.yk.mediademo.ui.base.IActivityInit;
import com.yk.mediademo.utils.FolderUtils;

public class PcmActivity extends AppCompatActivity implements IActivityInit {

    private AppCompatButton btnRecord;
    private AppCompatButton btnStopRecord;
    private AppCompatButton btnPlay;
    private AppCompatButton btnStopPlay;

    private String path;

    private final PcmRecorder pcmRecorder = new PcmRecorder();
    private final PcmPlayer pcmPlayer = new PcmPlayer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcm);
        findView();
        initData();
        bindEvent();
    }

    @Override
    public void findView() {
        btnRecord = findViewById(R.id.btnRecord);
        btnStopRecord = findViewById(R.id.btnStopRecord);
        btnPlay = findViewById(R.id.btnPlay);
        btnStopPlay = findViewById(R.id.btnStopPlay);
    }

    @Override
    public void initData() {
        path = FolderUtils.getAudioFolderPath(this) + "record.pcm";
    }

    @Override
    public void bindEvent() {
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcmRecorder.start(path);
            }
        });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcmRecorder.stop();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcmPlayer.start(path);
            }
        });

        btnStopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcmPlayer.stop();
            }
        });
    }
}
