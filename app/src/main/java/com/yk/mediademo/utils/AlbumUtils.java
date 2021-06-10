package com.yk.mediademo.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * 相册工具类（未完善，暂时不能用）
 */
public class AlbumUtils {

    public static void notifyAlbumUpdateImage(Context context, String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }

        ContentResolver contentResolver = context.getContentResolver();

        ContentValues contentValues = getImageContentValues(file);

        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public static void notifyAlbumUpdateVideo(Context context, String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }

        ContentResolver contentResolver = context.getContentResolver();

        ContentValues contentValues = getVideoContentValues(file);

        contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    private static ContentValues getImageContentValues(File file) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, file.getName());
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        contentValues.put(MediaStore.Images.Media.SIZE, file.length());

        return contentValues;
    }

    private static ContentValues getVideoContentValues(File file) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, file.getName());
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/mp4");
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        contentValues.put(MediaStore.Images.Media.SIZE, file.length());

        return contentValues;
    }

}
