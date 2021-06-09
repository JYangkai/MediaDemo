package com.yk.media.opengles.render;

public interface Renderer {
    void onCreate();

    void onChange(int width, int height);

    void onDraw();
}
