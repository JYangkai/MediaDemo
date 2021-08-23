package com.yk.mediademo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.mediademo.data.adapter.FunctionAdapter;
import com.yk.mediademo.data.bean.Function;
import com.yk.mediademo.ui.base.IActivityInit;
import com.yk.mediademo.utils.FunctionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity implements IActivityInit {

    private RecyclerView rvFunction;
    private final List<Function> functionList = new ArrayList<>();
    private FunctionAdapter functionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initData();
        bindEvent();
    }

    @Override
    public void findView() {
        rvFunction = findViewById(R.id.rvFunction);
    }

    @Override
    public void initData() {
        initRvFunction();
        checkPermission();
    }

    private void initRvFunction() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        functionAdapter = new FunctionAdapter(functionList);
        rvFunction.setLayoutManager(linearLayoutManager);
        rvFunction.setAdapter(functionAdapter);
    }

    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (permissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[0]), 1);
        } else {
            updateRvFunction();
        }
    }

    private void updateRvFunction() {
        functionList.clear();
        functionList.addAll(FunctionUtils.FUNCTION_LIST);
        functionAdapter.notifyDataSetChanged();
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MainActivity.this, "need permissions", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                }
                updateRvFunction();
        }
    }
}