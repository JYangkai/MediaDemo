package com.yk.media.opengles.render.base;

import android.content.Context;
import android.opengl.GLES20;

import com.yk.media.opengles.render.bean.base.BaseRenderBean;
import com.yk.media.utils.OpenGLESUtils;

import java.nio.FloatBuffer;

public class BaseRender implements IRender {
    /**
     * Context
     */
    private Context context;

    /**
     * 渲染数据
     */
    private BaseRenderBean renderBean;

    /**
     * 顶点坐标
     */
    private FloatBuffer vertexBuffer;

    /**
     * 纹理坐标
     */
    private FloatBuffer coordinateBuffer;

    /**
     * 顶点坐标维数（即x, y, z）
     */
    private int vertexSize = 2;

    /**
     * 纹理坐标维数（即x, y, z）
     */
    private int coordinateSize = 2;

    /**
     * 顶点坐标步长（即维数 * 字节数）
     */
    private int vertexStride = vertexSize * 4;

    /**
     * 纹理坐标步长（即维数 * 字节数）
     */
    private int coordinateStride = coordinateSize * 4;

    /**
     * 顶点个数
     */
    private int vertexCount = 4;

    /**
     * 纹理点个数
     */
    private int coordinateCount = 4;

    /**
     * vertex shader
     */
    private int vertexShader;

    /**
     * frag shader
     */
    private int fragShader;

    /**
     * program
     */
    private int program;

    /**
     * 纹理 id
     */
    private int textureId;

    /**
     * fbo纹理id
     */
    private int fboTextureId;

    /**
     * fbo id
     */
    private int fboId;

    /**
     * vbo id
     */
    private int vboId;

    /**
     * 顶点着色器代码路径
     */
    private String vertexFilename;

    /**
     * 片元着色器代码路径
     */
    private String fragFilename;

    /**
     * 尺寸
     */
    private int width;
    private int height;

    /**
     * 是否绑定Fbo
     */
    private boolean isBindFbo = false;

    /**
     * 着色器顶点坐标位置
     */
    private int aPosLocation;

    /**
     * 着色器纹理坐标位置
     */
    private int aCoordinateLocation;

    /**
     * 着色器纹理位置
     */
    private int uSamplerLocation;

    /**
     * 是否执行了onCreate
     */
    private boolean isCreate = false;

    /**
     * 是否执行了onChange
     */
    private boolean isChange = false;

    public BaseRender(Context context) {
        this(context, "render/base/base/vertex.frag", "render/base/base/frag.frag");
    }

    public BaseRender(Context context, String vertexFilename, String fragFilename) {
        this.context = context;
        this.vertexFilename = vertexFilename;
        this.fragFilename = fragFilename;
    }

    @Override
    public void onCreate() {
        if (isCreate) {
            return;
        }
        onCreatePre();
        onClearColor();
        onInitBlend();
        onInitVertexBuffer();
        onInitCoordinateBuffer();
        onInitVbo();
        onInitProgram();
        onCreateAfter();
        isCreate = true;
    }

    @Override
    public void onChange(int width, int height) {
        if (isChange) {
            return;
        }
        onChangePre();
        setWidth(width);
        setHeight(height);
        onViewport();
        onInitFbo();
        onChangeAfter();
        isChange = true;
    }

    @Override
    public void onDraw(int textureId) {
        if (!onReadyToDraw()) {
            return;
        }
        onDrawPre();
        onClear();
        onUseProgram();
        onInitLocation();
        onBindFbo();
        onBindVbo();
        onActiveTexture(textureId);
        onEnableVertexAttributeArray();
        onSetVertexData();
        onSetCoordinateData();
        onSetOtherData();
        onDraw();
        onDisableVertexAttributeArray();
        onUnBind();
        onDrawAfter();
    }

    @Override
    public void onRelease() {
        onDeleteProgram(program);
        onDeleteShader(vertexShader);
        onDeleteShader(fragShader);
        onDeleteTexture(textureId);
        onDeleteTexture(fboTextureId);
        onDeleteFbo(fboId);
        onDeleteVbo(vboId);
    }

    /**
     * 创建之前
     */
    public void onCreatePre() {

    }

    /**
     * 设置背景颜色
     */
    public void onClearColor() {
        GLES20.glClearColor(0, 0, 0, 1);
    }

    /**
     * 是否启用混色
     */
    public boolean onEnableBlend() {
        return false;
    }

    /**
     * 初始化混色
     */
    private void onInitBlend() {
        if (!onEnableBlend()) {
            return;
        }
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * 初始化顶点坐标
     */
    public void onInitVertexBuffer() {
        vertexBuffer = OpenGLESUtils.getSquareVertexBuffer();
    }

    /**
     * 初始化纹理坐标
     */
    public void onInitCoordinateBuffer() {
        if (isBindFbo) {
            coordinateBuffer = OpenGLESUtils.getSquareCoordinateReverseBuffer();
        } else {
            coordinateBuffer = OpenGLESUtils.getSquareCoordinateBuffer();
        }
    }

    /**
     * 初始化Vbo
     */
    public void onInitVbo() {
        vboId = OpenGLESUtils.getVbo(vertexBuffer, coordinateBuffer);
    }

    /**
     * 初始化Program
     */
    public void onInitProgram() {
        String vertexShaderCode = OpenGLESUtils.getShaderCode(context, vertexFilename);
        String fragShaderCode = OpenGLESUtils.getShaderCode(context, fragFilename);

        vertexShader = OpenGLESUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        fragShader = OpenGLESUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragShaderCode);

        program = OpenGLESUtils.linkProgram(vertexShader, fragShader);
    }

    /**
     * 创建之后
     */
    public void onCreateAfter() {

    }

    /**
     * 设置尺寸之前
     */
    public void onChangePre() {

    }

    /**
     * 设置窗口尺寸
     */
    public void onViewport() {
        GLES20.glViewport(0, 0, width, height);
    }

    /**
     * 初始化Fbo
     */
    public void onInitFbo() {
        if (!isBindFbo) {
            return;
        }
        int[] fboData = OpenGLESUtils.getFbo(width, height);
        fboId = fboData[0];
        fboTextureId = fboData[1];
    }

    /**
     * 设置尺寸之后
     */
    public void onChangeAfter() {

    }

    /**
     * 绘制之前的准备
     */
    public boolean onReadyToDraw() {
        return true;
    }

    /**
     * 绘制之前
     */
    public void onDrawPre() {

    }

    /**
     * 清屏
     */
    public void onClear() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * 使用Program
     */
    public void onUseProgram() {
        GLES20.glUseProgram(program);
    }

    /**
     * 初始化着色器各个位置
     */
    public void onInitLocation() {
        aPosLocation = GLES20.glGetAttribLocation(program, "aPos");
        aCoordinateLocation = GLES20.glGetAttribLocation(program, "aCoordinate");
        uSamplerLocation = GLES20.glGetUniformLocation(program, "uSampler");
    }

    /**
     * 绑定Fbo
     */
    public void onBindFbo() {
        if (!isBindFbo) {
            return;
        }
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, fboTextureId, 0);
        GLES20.glViewport(0, 0, width, height);
    }

    /**
     * 绑定Vbo
     */
    public void onBindVbo() {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId);
    }

    /**
     * 激活并绑定纹理
     */
    public void onActiveTexture(int textureId) {
        this.textureId = textureId;
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uSamplerLocation, 0);
    }

    /**
     * 启用顶点坐标
     */
    public void onEnableVertexAttributeArray() {
        GLES20.glEnableVertexAttribArray(aPosLocation);
        GLES20.glEnableVertexAttribArray(aCoordinateLocation);
    }

    /**
     * 设置顶点坐标
     */
    public void onSetVertexData() {
        GLES20.glVertexAttribPointer(aPosLocation, vertexSize, GLES20.GL_FLOAT, false, vertexStride, 0);
    }

    /**
     * 设置纹理坐标
     */
    public void onSetCoordinateData() {
        GLES20.glVertexAttribPointer(aCoordinateLocation, coordinateSize, GLES20.GL_FLOAT, false, coordinateStride, vertexBuffer.limit() * 4);
    }

    /**
     * 设置其他数据
     */
    public void onSetOtherData() {

    }

    /**
     * 绘制
     */
    public void onDraw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);
    }

    /**
     * 禁用顶点坐标
     */
    public void onDisableVertexAttributeArray() {
        GLES20.glDisableVertexAttribArray(aPosLocation);
        GLES20.glDisableVertexAttribArray(aCoordinateLocation);
    }

    /**
     * 解除绑定
     */
    public void onUnBind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    /**
     * 绘制之后
     */
    public void onDrawAfter() {

    }

    /**
     * 删除Program
     */
    public void onDeleteProgram(int program) {
        GLES20.glDeleteProgram(program);
    }

    /**
     * 删除Shader
     */
    public void onDeleteShader(int shader) {
        GLES20.glDeleteShader(shader);
    }

    /**
     * 删除纹理
     */
    public void onDeleteTexture(int textureId) {
        GLES20.glDeleteTextures(1, new int[]{textureId}, 0);
    }

    /**
     * 删除Fbo
     */
    public void onDeleteFbo(int fboId) {
        GLES20.glDeleteFramebuffers(1, new int[]{fboId}, 0);
    }

    /**
     * 删除Vbo
     */
    public void onDeleteVbo(int vboId) {
        GLES20.glDeleteBuffers(1, new int[]{vboId}, 0);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public FloatBuffer getVertexBuffer() {
        return vertexBuffer;
    }

    public void setVertexBuffer(FloatBuffer vertexBuffer) {
        this.vertexBuffer = vertexBuffer;
    }

    public FloatBuffer getCoordinateBuffer() {
        return coordinateBuffer;
    }

    public void setCoordinateBuffer(FloatBuffer coordinateBuffer) {
        this.coordinateBuffer = coordinateBuffer;
    }

    public int getVertexSize() {
        return vertexSize;
    }

    public void setVertexSize(int vertexSize) {
        this.vertexSize = vertexSize;
    }

    public int getCoordinateSize() {
        return coordinateSize;
    }

    public void setCoordinateSize(int coordinateSize) {
        this.coordinateSize = coordinateSize;
    }

    public int getVertexStride() {
        return vertexStride;
    }

    public void setVertexStride(int vertexStride) {
        this.vertexStride = vertexStride;
    }

    public int getCoordinateStride() {
        return coordinateStride;
    }

    public void setCoordinateStride(int coordinateStride) {
        this.coordinateStride = coordinateStride;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public int getCoordinateCount() {
        return coordinateCount;
    }

    public void setCoordinateCount(int coordinateCount) {
        this.coordinateCount = coordinateCount;
    }

    public int getProgram() {
        return program;
    }

    public void setProgram(int program) {
        this.program = program;
    }

    public int getFboTextureId() {
        return fboTextureId;
    }

    public void setFboTextureId(int fboTextureId) {
        this.fboTextureId = fboTextureId;
    }

    public int getFboId() {
        return fboId;
    }

    public void setFboId(int fboId) {
        this.fboId = fboId;
    }

    public int getVboId() {
        return vboId;
    }

    public void setVboId(int vboId) {
        this.vboId = vboId;
    }

    public String getVertexFilename() {
        return vertexFilename;
    }

    public void setVertexFilename(String vertexFilename) {
        this.vertexFilename = vertexFilename;
    }

    public String getFragFilename() {
        return fragFilename;
    }

    public void setFragFilename(String fragFilename) {
        this.fragFilename = fragFilename;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isBindFbo() {
        return isBindFbo;
    }

    public void setBindFbo(boolean bindFbo) {
        isBindFbo = bindFbo;
    }

    public int getPosLocation() {
        return aPosLocation;
    }

    public void setPosLocation(int aPosLocation) {
        this.aPosLocation = aPosLocation;
    }

    public int getCoordinateLocation() {
        return aCoordinateLocation;
    }

    public void setCoordinateLocation(int aCoordinateLocation) {
        this.aCoordinateLocation = aCoordinateLocation;
    }

    public int getSamplerLocation() {
        return uSamplerLocation;
    }

    public void setSamplerLocation(int uSamplerLocation) {
        this.uSamplerLocation = uSamplerLocation;
    }

    public boolean isCreate() {
        return isCreate;
    }

    public void setCreate(boolean create) {
        isCreate = create;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }

    public BaseRenderBean getRenderBean() {
        return renderBean;
    }

    public void setRenderBean(BaseRenderBean renderBean) {
        this.renderBean = renderBean;
    }

    public void updateRenderBean(BaseRenderBean renderBean) {
        setRenderBean(renderBean);
    }
}
