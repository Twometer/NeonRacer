package neonracer.render.engine.postproc;

import neonracer.core.GameContext;
import neonracer.render.GameWindow;
import neonracer.render.engine.RenderPass;
import neonracer.render.gl.core.Fbo;
import neonracer.render.gl.shaders.HGaussShader;
import neonracer.render.gl.shaders.MixShader;
import neonracer.render.gl.shaders.VGaussShader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class PostProcessing {

    private GameWindow gameWindow;

    private FboManager fboManager;

    private Fbo glowFbo;
    private Fbo colorFbo;
    private Fbo gaussFbo;
    private Fbo gaussFbo2;

    private HGaussShader hGaussShader;
    private VGaussShader vGaussShader;
    private MixShader mixShader;

    public PostProcessing(GameContext gameContext) {
        this.fboManager = new FboManager();
        this.gameWindow = gameContext.getGameWindow();
        this.hGaussShader = gameContext.getShaderProvider().getShader(HGaussShader.class);
        this.vGaussShader = gameContext.getShaderProvider().getShader(VGaussShader.class);
        this.mixShader = gameContext.getShaderProvider().getShader(MixShader.class);
        initFramebuffers(gameWindow.getWidth(), gameWindow.getHeight());
    }

    public void beginPass(RenderPass renderPass) {
        if (renderPass == RenderPass.COLOR)
            colorFbo.bind();
        else if (renderPass == RenderPass.GLOW)
            glowFbo.bind();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void draw() {
        // Beautiful postprocessing pipeline in 3.. 2.. 1..
        fboManager.begin();

        // Apply horizontal gaussian blur to the glowing parts of the scene
        hGaussShader.bind();
        hGaussShader.setTargetWidth(gaussFbo.getWidth());
        fboManager.copyFbo(glowFbo, gaussFbo);
        hGaussShader.unbind();

        // Apply vertical gaussian blur to the horizontally blurred image
        vGaussShader.bind();
        vGaussShader.setTargetHeight(gaussFbo2.getHeight());
        fboManager.copyFbo(gaussFbo, gaussFbo2);
        vGaussShader.unbind();

        // Overlay the regularly textured scene with the glowing blurred parts
        // using the mix shader
        mixShader.bind();
        glClear(GL_COLOR_BUFFER_BIT);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, colorFbo.getColorTexture());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, gaussFbo2.getColorTexture());
        fboManager.fullscreenQuad();
        mixShader.unbind();

        fboManager.end();
    }

    public void onResize(int width, int height) {
        initFramebuffers(width, height);
    }

    private void initFramebuffers(int width, int height) {
        colorFbo = new Fbo(gameWindow, width, height, Fbo.DepthBufferType.NONE);
        glowFbo = new Fbo(gameWindow, width, height, Fbo.DepthBufferType.NONE);
        gaussFbo = new Fbo(gameWindow, width / 2, height / 2, Fbo.DepthBufferType.NONE);
        gaussFbo2 = new Fbo(gameWindow, width / 2, height / 2, Fbo.DepthBufferType.NONE);
    }

}
