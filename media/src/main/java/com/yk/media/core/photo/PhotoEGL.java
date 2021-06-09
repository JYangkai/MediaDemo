package com.yk.media.core.photo;

import android.opengl.EGL14;
import android.util.Log;
import android.view.Surface;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class PhotoEGL {
    private static final String TAG = "PhotoEGL";

    private final int mRedSize = 8;
    private final int mGreenSize = 8;
    private final int mBlueSize = 8;
    private final int mAlphaSize = 8;
    private final int mDepthSize = 8;
    private final int mStencilSize = 8;

    private EGL10 egl;
    private EGLDisplay eglDisplay;
    private EGLContext eglContext;
    private EGLSurface eglSurface;

    public void init(EGLContext eglContext, Surface surface) {
        // 获取egl
        egl = (EGL10) EGLContext.getEGL();

        // 获取eglDisplay
        eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (eglDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("egl get display fail");
        }

        // 初始化display
        int[] version = new int[2];
        if (!egl.eglInitialize(eglDisplay, version)) {
            throw new RuntimeException("egl init display fail");
        }

        // 设置display属性
        int[] attrib_list = new int[]{
                EGL10.EGL_RED_SIZE, mRedSize,
                EGL10.EGL_GREEN_SIZE, mGreenSize,
                EGL10.EGL_BLUE_SIZE, mBlueSize,
                EGL10.EGL_ALPHA_SIZE, mAlphaSize,
                EGL10.EGL_DEPTH_SIZE, mDepthSize,
                EGL10.EGL_STENCIL_SIZE, mStencilSize,
                EGL10.EGL_RENDERABLE_TYPE, 4,//egl版本  2.0
                EGL10.EGL_NONE
        };
        int[] num_config = new int[1];
        if (!egl.eglChooseConfig(eglDisplay, attrib_list, null, 1, num_config)) {
            throw new RuntimeException("egl choose config fail");
        }
        int numConfigs = num_config[0];
        if (numConfigs <= 0) {
            throw new IllegalArgumentException("no configs match configSpec");
        }

        // 从系统中获取对应属性的配置
        EGLConfig[] configs = new EGLConfig[numConfigs];
        if (!egl.eglChooseConfig(eglDisplay, attrib_list, configs, numConfigs, num_config)) {
            throw new IllegalArgumentException("egl choose config fail#2");
        }
        EGLConfig eglConfig = chooseConfig(egl, eglDisplay, configs);
        if (eglConfig == null) {
            eglConfig = configs[0];
        }

        // 创建EGLContext
        int[] contextAttr = new int[]{
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL10.EGL_NONE
        };
        if (eglContext == null) {
            this.eglContext = egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, contextAttr);
        } else {
            this.eglContext = egl.eglCreateContext(eglDisplay, eglConfig, eglContext, contextAttr);
        }

        // 创建EGLSurface
        eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, surface, null);

        // 绑定EGLContext和EGLSurface到显示设备中
        if (!egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, this.eglContext)) {
            throw new RuntimeException("egl make current fail");
        }
    }

    public boolean swapBuffers() {
        if (egl == null) {
            throw new RuntimeException("egl is null");
        }
        return egl.eglSwapBuffers(eglDisplay, eglSurface);
    }

    /**
     * 销毁EGL环境
     */
    public void destroyEgl() {
        Log.d(TAG, "destroyEgl: ");
        if (egl == null) {
            return;
        }
        if (eglSurface != null && eglSurface != EGL10.EGL_NO_SURFACE) {
            egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
            egl.eglDestroySurface(eglDisplay, eglSurface);
            eglSurface = null;
        }

        if (eglContext != null) {
            egl.eglDestroyContext(eglDisplay, eglContext);
            eglContext = null;
        }

        if (eglDisplay != null) {
            egl.eglTerminate(eglDisplay);
            eglDisplay = null;
        }

        egl = null;
    }

    private EGLConfig chooseConfig(EGL10 egl, EGLDisplay display,
                                   EGLConfig[] configs) {
        for (EGLConfig config : configs) {
            int d = findConfigAttrib(egl, display, config,
                    EGL10.EGL_DEPTH_SIZE, 0);
            int s = findConfigAttrib(egl, display, config,
                    EGL10.EGL_STENCIL_SIZE, 0);
            if ((d >= mDepthSize) && (s >= mStencilSize)) {
                int r = findConfigAttrib(egl, display, config,
                        EGL10.EGL_RED_SIZE, 0);
                int g = findConfigAttrib(egl, display, config,
                        EGL10.EGL_GREEN_SIZE, 0);
                int b = findConfigAttrib(egl, display, config,
                        EGL10.EGL_BLUE_SIZE, 0);
                int a = findConfigAttrib(egl, display, config,
                        EGL10.EGL_ALPHA_SIZE, 0);
                if ((r == mRedSize) && (g == mGreenSize)
                        && (b == mBlueSize) && (a == mAlphaSize)) {
                    return config;
                }
            }
        }
        return null;
    }

    private int findConfigAttrib(EGL10 egl, EGLDisplay display,
                                 EGLConfig config, int attribute, int defaultValue) {
        int[] value = new int[1];
        if (egl.eglGetConfigAttrib(display, config, attribute, value)) {
            return value[0];
        }
        return defaultValue;
    }
}
