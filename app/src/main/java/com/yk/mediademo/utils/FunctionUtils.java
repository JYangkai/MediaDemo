package com.yk.mediademo.utils;

import com.yk.mediademo.constants.Constants;
import com.yk.mediademo.data.bean.Function;
import com.yk.mediademo.ui.ImageShowActivity;
import com.yk.mediademo.ui.RecordVideoActivity;
import com.yk.mediademo.ui.TakePhotoActivity;
import com.yk.mediademo.ui.TextureViewCameraActivity;

import java.util.ArrayList;
import java.util.List;

public class FunctionUtils {
    public static final List<Function> FUNCTION_LIST = new ArrayList<>();

    static {
        FUNCTION_LIST.add(new Function(Constants.Function.FUNCTION_TEXTURE_VIEW_CAMERA, TextureViewCameraActivity.class));
        FUNCTION_LIST.add(new Function(Constants.Function.FUNCTION_TAKE_PHOTO, TakePhotoActivity.class));
        FUNCTION_LIST.add(new Function(Constants.Function.FUNCTION_RECORD_VIDEO, RecordVideoActivity.class));
        FUNCTION_LIST.add(new Function(Constants.Function.FUNCTION_IMAGE_SHOW, ImageShowActivity.class));
    }
}
