package com.yk.mediademo.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yk.media.core.pcm.PcmDecoder;
import com.yk.media.core.pcm.PcmEncoder;
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
    private AppCompatButton btnEncode;
    private AppCompatButton btnStopEncode;
    private AppCompatButton btnDecode;
    private AppCompatButton btnStopDecode;

    private String path;
    private String aacPath;

    private final PcmRecorder pcmRecorder = new PcmRecorder();
    private final PcmPlayer pcmPlayer = new PcmPlayer();
    private final PcmEncoder pcmEncoder = new PcmEncoder();
    private final PcmDecoder pcmDecoder = new PcmDecoder();

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
        btnEncode = findViewById(R.id.btnEncode);
        btnStopEncode = findViewById(R.id.btnStopEncode);
        btnDecode = findViewById(R.id.btnDecode);
        btnStopDecode = findViewById(R.id.btnStopDecode);
    }

    @Override
    public void initData() {
        path = FolderUtils.getAudioFolderPath(this) + "record.pcm";
        aacPath = FolderUtils.getAudioFolderPath(this) + "encode.aac";
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

        btnEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcmEncoder.start(aacPath);
            }
        });

        btnStopEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcmEncoder.stop();
            }
        });

        btnDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcmDecoder.start(aacPath);
            }
        });

        btnStopDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcmDecoder.stop();
            }
        });
    }
}
