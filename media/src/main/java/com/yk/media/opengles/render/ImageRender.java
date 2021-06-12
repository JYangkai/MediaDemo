package com.yk.media.opengles.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.yk.media.opengles.render.base.BaseImageRender;
import com.yk.media.opengles.render.base.BaseRender;
import com.yk.media.utils.OpenGLESUtils;

public class ImageRender implements Renderer {
    private final Context context;

    private final BaseImageRender inputRender;
    private final BaseRender outputRender;

    private String path;
    private String fileName;

    private int textureId = -1;

    private boolean isUpdate = false;

    public ImageRender(Context context) {
        this.context = context;
        inputRender = new BaseImageRender(context);
        outputRender = new BaseRender(context);
    }

    @Override
    public void onCreate() {
        inputRender.onCreate();
        outputRender.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        inputRender.onChange(width, height);
        outputRender.onChange(width, height);
    }

    @Override
    public void onDraw() {
        if (!initTextureId()) {
            return;
        }
        inputRender.onDraw(textureId);
        outputRender.onDraw(inputRender.getFboTextureId());
    }

    private boolean initTextureId() {
        if (textureId != -1 && !isUpdate) {
            return true;
        }
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(path)) {
            bitmap = OpenGLESUtils.getBitmapFromPath(path);
        } else if (!TextUtils.isEmpty(fileName)) {
            bitmap = OpenGLESUtils.getBitmapFromAssets(context, fileName);
        }
        if (bitmap == null) {
            return false;
        }
        inputRender.setImageSize(bitmap.getWidth(), bitmap.getHeight());
        textureId = OpenGLESUtils.getBitmapTexture(bitmap);
        isUpdate = true;
        return true;
    }

    public void setPath(String path) {
        this.path = path;
        this.fileName = null;
        isUpdate = true;
    }

    public void setAssetsFileName(String fileName) {
        this.fileName = fileName;
        this.path = null;
        isUpdate = true;
    }
}
