package com.yk.media.opengles.render.bean.transition;

import com.yk.media.opengles.render.bean.base.BaseRenderBean;

public class BaseTransitionBean extends BaseRenderBean {
    private long duration;

    public BaseTransitionBean(int type, String name, long duration) {
        super(type, name);
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return super.toString() +
                "BaseTransitionBean{" +
                "duration=" + duration +
                '}';
    }
}
