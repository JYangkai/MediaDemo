package com.yk.media.opengles.render.base;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.yk.media.utils.OpenGLESUtils;

public class BaseOesRender extends BaseRender {
    /**
     * oes纹理id
     */
    private int oesTextureId;

    /**
     * 顶点变换矩阵位置
     */
    private int uMatrixLocation;

    /**
     * 纹理变换矩阵位置
     */
    private int uOesMatrixLocation;

    /**
     * oes尺寸
     */
    private int oesW = -1;
    private int oesH = -1;

    /**
     * 顶点变换矩阵
     */
    private float[] mMVPMatrix = new float[16];

    /**
     * 纹理变换矩阵
     */
    private float[] mOesMatrix = {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    };

    /**
     * 是否准备好绘制
     */
    private boolean isReadyToDraw = false;

    /**
     * SurfaceTexture
     */
    private SurfaceTexture surfaceTexture;

    /**
     * SurfaceTexture回调
     */
    private OnSurfaceTextureListener onSurfaceTextureListener;

    public BaseOesRender(Context context) {
        super(context, "render/base/oes/vertex.frag", "render/base/oes/frag.frag");
        setBindFbo(true);
        oesTextureId = OpenGLESUtils.getOesTexture();
    }

    @Override
    public void onInitCoordinateBuffer() {
        setCoordinateBuffer(OpenGLESUtils.getSquareCoordinateBuffer());
    }

    @Override
    public boolean onReadyToDraw() {
        if (!isReadyToDraw) {
            if (onSurfaceTextureListener != null) {
                if (surfaceTexture != null) {
                    surfaceTexture.release();
                    surfaceTexture = null;
                }
                surfaceTexture = new SurfaceTexture(oesTextureId);
                onSurfaceTextureListener.onSurfaceTexture(surfaceTexture);
                isReadyToDraw = true;
            } else if (surfaceTexture != null) {
                surfaceTexture.attachToGLContext(oesTextureId);
                isReadyToDraw = true;
            } else {
                return false;
            }
        }
        return oesW != -1 && oesH != -1;
    }

    @Override
    public void onDrawPre() {
        super.onDrawPre();
        mMVPMatrix = OpenGLESUtils.getMatrix(getWidth(), getHeight(), oesW, oesH);

        surfaceTexture.updateTexImage();

        float[] oesMatrix = new float[16];
        surfaceTexture.getTransformMatrix(oesMatrix);
        if (!OpenGLESUtils.isIdentityM(oesMatrix)) {
            mOesMatrix = oesMatrix;
        }
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uMatrixLocation = GLES20.glGetUniformLocation(getProgram(), "uMatrix");
        uOesMatrixLocation = GLES20.glGetUniformLocation(getProgram(), "uOesMatrix");
    }

    @Override
    public void onActiveTexture(int textureId) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        GLES20.glUniform1i(getSamplerLocation(), 0);
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(uOesMatrixLocation, 1, false, mOesMatrix, 0);
    }

    @Override
    public void onRelease() {
        super.onRelease();
        onDeleteTexture(oesTextureId);
    }

    /**
     * 绘制
     */
    public void onDrawSelf() {
        super.onDraw(oesTextureId);
    }

    /**
     * 设置oes尺寸
     */
    public void setOesSize(int width, int height) {
        oesW = width;
        oesH = height;
    }

    /**
     * 设置SurfaceTexture
     */
    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
        isReadyToDraw = false;
    }

    /**
     * 设置SurfaceTexture回调
     */
    public void setOnSurfaceTextureListener(OnSurfaceTextureListener onSurfaceTextureListener) {
        this.onSurfaceTextureListener = onSurfaceTextureListener;
        isReadyToDraw = false;
    }
}
