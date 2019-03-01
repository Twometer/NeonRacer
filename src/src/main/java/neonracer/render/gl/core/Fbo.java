package neonracer.render.gl.core;

import neonracer.render.GameWindow;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Wrapper class for OpenGL
 * frame buffer objects
 */
public class Fbo {

    private GameWindow gameWindow;

    private int width;
    private int height;

    private int framebuffer;
    private int depthBuffer;

    private int depthTexture;
    private int colorTexture;

    public Fbo(GameWindow gameWindow, int width, int height, DepthBufferType depthBufferType) {
        this.gameWindow = gameWindow;
        this.width = width;
        this.height = height;
        initialize(depthBufferType);
    }

    public void bind() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, framebuffer);
        glViewport(0, 0, width, height);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, gameWindow.getWidth(), gameWindow.getHeight());
    }

    public int getDepthTexture() {
        return depthTexture;
    }

    public int getColorTexture() {
        return colorTexture;
    }

    public void destroy() {
        glDeleteFramebuffers(framebuffer);
        glDeleteTextures(colorTexture);
        glDeleteTextures(depthTexture);
        glDeleteRenderbuffers(depthBuffer);
    }

    private void initialize(DepthBufferType type) {
        createFrameBuffer();
        createTextureAttachment();
        if(type == DepthBufferType.DEPTH_RENDER_BUFFER)
            createDepthBufferAttachment();
        else if(type == DepthBufferType.DEPTH_TEXTURE)
            createDepthTextureAttachment();
        unbind();
    }

    private void createFrameBuffer() {
        framebuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
    }

    private void createTextureAttachment() {
        colorTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, colorTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorTexture, 0);
    }

    private void createDepthTextureAttachment() {
        depthTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture, 0);
    }

    private void createDepthBufferAttachment() {
        depthBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
    }

    public enum DepthBufferType {
        NONE,
        DEPTH_TEXTURE,
        DEPTH_RENDER_BUFFER
    }

}
