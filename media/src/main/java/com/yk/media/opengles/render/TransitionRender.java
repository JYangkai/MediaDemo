package com.yk.media.opengles.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.yk.media.opengles.render.base.BaseImageRender;
import com.yk.media.opengles.render.base.BaseRender;
import com.yk.media.opengles.render.transition.manager.TransitionManager;
import com.yk.media.utils.OpenGLESUtils;

public class TransitionRender implements Renderer {
    private final Context context;

    private final BaseImageRender inputRender;
    private final BaseImageRender inputRender2;

    private final BaseRender outputRender;

    private String path;
    private String path2;

    private String fileName;
    private String fileName2;

    private int textureId = -1;
    private int textureId2 = -1;

    private int width;
    private int height;

    private boolean isUpdate = false;
    private boolean isUpdate2 = false;

    public TransitionRender(Context context) {
        this.context = context;
        inputRender = new BaseImageRender(context);
        inputRender2 = new BaseImageRender(context);
        outputRender = new BaseRender(context);
    }

    @Override
    public void onCreate() {
        inputRender.onCreate();
        inputRender2.onCreate();
        outputRender.onCreate();
        TransitionManager.getInstance(context).onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        this.width = width;
        this.height = height;
        inputRender.onChange(width, height);
        inputRender2.onChange(width, height);
        outputRender.onChange(width, height);
        TransitionManager.getInstance(context).onChange();
    }

    @Override
    public void onDraw() {
        if (!initTextureId()) {
            return;
        }
        if (!initTextureId2()) {
            return;
        }
        inputRender.onDraw(textureId);
        inputRender2.onDraw(textureId2);
        int fboTextureId = TransitionManager.getInstance(context).onDraw(
                inputRender.getFboTextureId(), inputRender2.getFboTextureId(),
                width, height);
        outputRender.onDraw(fboTextureId);
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
        isUpdate = false;
        return true;
    }

    private boolean initTextureId2() {
        if (textureId2 != -1 && !isUpdate2) {
            return true;
        }
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(path2)) {
            bitmap = OpenGLESUtils.getBitmapFromPath(path2);
        } else if (!TextUtils.isEmpty(fileName2)) {
            bitmap = OpenGLESUtils.getBitmapFromAssets(context, fileName2);
        }
        if (bitmap == null) {
            return false;
        }
        inputRender2.setImageSize(bitmap.getWidth(), bitmap.getHeight());
        textureId2 = OpenGLESUtils.getBitmapTexture(bitmap);
        isUpdate2 = false;
        return true;
    }

    public void setPath(String path, String path2) {
        this.path = path;
        this.path2 = path2;
        this.fileName = null;
        this.fileName2 = null;
        isUpdate = true;
        isUpdate2 = true;
    }

    public void setAssetsFileName(String fileName, String fileName2) {
        this.fileName = fileName;
        this.fileName2 = fileName2;
        this.path = null;
        this.path2 = null;
        isUpdate = true;
        isUpdate2 = true;
    }
}
