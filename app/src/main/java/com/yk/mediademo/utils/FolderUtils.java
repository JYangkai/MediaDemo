package com.yk.mediademo.utils;

import android.content.Context;

import java.io.File;

public class FolderUtils {
    private static final String FOLDER_IMAGE = "folder_image";
    private static final String FOLDER_VIDEO = "folder_video";

    public static String getImageFolderPath(Context context) {
        return getFolderPath(context, FOLDER_IMAGE);
    }

    public static String getVideoFolderPath(Context context) {
        return getFolderPath(context, FOLDER_VIDEO);
    }

    public static String getFolderPath(Context context, String folder) {
        return context.getExternalFilesDir(folder).getPath() + File.separator;
    }

}
