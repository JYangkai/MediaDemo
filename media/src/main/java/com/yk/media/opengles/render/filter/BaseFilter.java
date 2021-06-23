package com.yk.media.opengles.render.filter;

import android.content.Context;

import com.yk.media.opengles.render.base.BaseRender;

public class BaseFilter extends BaseRender {
    private long duration;
    private long startTime = -1;

    public BaseFilter(Context context) {
        super(context);
    }

    public BaseFilter(Context context, String vertexFilename, String fragFilename) {
        super(context, vertexFilename, fragFilename);
    }

    public void timeStart(long duration) {
        startTime = System.currentTimeMillis();
        this.duration = duration;
    }

    public float getProgress() {
        if (startTime == -1) {
            return 0;
        }
        long time = System.currentTimeMillis() - startTime;

        time %= duration;

        return (float) time / duration;
    }
}
