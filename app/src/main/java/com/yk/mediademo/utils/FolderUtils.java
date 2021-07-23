package com.yk.mediademo.utils;

import android.content.Context;

import java.io.File;

public class FolderUtils {
    private static final String FOLDER_IMAGE = "folder_image";
    private static final String FOLDER_VIDEO = "folder_video";
    private static final String FOLDER_AUDIO = "folder_audio";

    public static String getImageFolderPath(Context context) {
        return getFolderPath(context, FOLDER_IMAGE);
    }

    public static String getVideoFolderPath(Context context) {
        return getFolderPath(context, FOLDER_VIDEO);
    }

    public static String getAudioFolderPath(Context context) {
        return getFolderPath(context, FOLDER_AUDIO);
    }

    public static String getFolderPath(Context context, String folder) {
        return context.getExternalFilesDir(folder).getPath() + File.separator;
    }

}
