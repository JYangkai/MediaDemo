package com.yk.mediademo.utils;

import android.content.Context;
import android.text.TextUtils;

import com.yk.mediademo.data.bean.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalMediaUtils {

    public static List<Image> getImageList(Context context) {
        String folder = FolderUtils.getImageFolderPath(context);
        if (TextUtils.isEmpty(folder)) {
            return null;
        }
        File file = new File(folder);
        if (!file.exists()) {
            return null;
        }
        if (!file.isDirectory()) {
            return null;
        }
        File[] files = file.listFiles();
        if (files == null || files.length <= 0) {
            return null;
        }
        List<Image> list = new ArrayList<>();
        for (File file1 : files) {
            list.add(new Image(file1.getName(), file1.getAbsolutePath()));
        }
        return list;
    }

}
