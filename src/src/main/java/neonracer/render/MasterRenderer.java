package neonracer.render;

import neonracer.core.ControlState;
import neonracer.core.GameContext;
import neonracer.model.entity.EntityCar;
import neonracer.model.track.Track;
import neonracer.phys.entity.car.CarPhysicsFactory;
import neonracer.render.engine.Camera;
import neonracer.render.engine.PostProcessing;
import neonracer.render.engine.RenderPass;
import neonracer.render.engine.renderers.DebugRenderer;
import neonracer.render.engine.renderers.EntityRenderer;
import neonracer.render.engine.renderers.IRenderer;
import neonracer.render.engine.renderers.TrackRenderer;
import neonracer.render.gl.core.Fbo;
import neonracer.render.gl.shaders.HGaussShader;
import neonracer.render.gl.shaders.MixShader;
import neonracer.render.gl.shaders.VGaussShader;
import neonracer.util.Log;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class MasterRenderer {

    private RenderContext renderContext;

    private GameContext gameContext;

    private GameWindow gameWindow;

    private IRenderer[] renderers = new IRenderer[]{
            new TrackRenderer(),
            new EntityRenderer(),
            new DebugRenderer()
    };

    private PostProcessing postProcessing;

    private Fbo colorFbo;

    private Fbo glowFbo;

    private Fbo gaussFbo;

    private Fbo gaussFbo2;

    private HGaussShader hGaussShader;
    private VGaussShader vGaussShader;
    private MixShader mixShader;

    public MasterRenderer(GameContext context) {
        this.gameContext = context;
        this.gameWindow = gameContext.getGameWindow();
        this.renderContext = new RenderContext(new Camera(context));
    }

    public void startLoop() {
        setup();
        while (!gameContext.getGameWindow().shouldClose()) {
            render();
            gameContext.getTimer().update();
            for (int i = 0; i < gameContext.getTimer().getTicks(); i++)
                tick();
            gameContext.getGameWindow().update();
        }
        destroy();
    }

    private void tick() {
        gameContext.getPhysicsEngine().onTick();

        ControlState controlState = gameContext.getControlState();
        controlState.setForward(gameContext.getGameWindow().isKeyPressed(GLFW_KEY_W));
        controlState.setLeft(gameContext.getGameWindow().isKeyPressed(GLFW_KEY_A));
        controlState.setReverse(gameContext.getGameWindow().isKeyPressed(GLFW_KEY_S));
        controlState.setRight(gameContext.getGameWindow().isKeyPressed(GLFW_KEY_D));
        controlState.setSpacebar(gameContext.getGameWindow().isKeyPressed(GLFW_KEY_SPACE));

        renderContext.getCameraManager().smoothFollow(gameContext.getGameState().getPlayerEntity());
    }

    private void setup() {
        Log.i("Initializing renderer...");
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        postProcessing = new PostProcessing();
        postProcessing.initialize();

        gameWindow.setSizeChangedListener(this::onResize);

        initFBOs(gameWindow.getWidth(), gameWindow.getHeight());

        hGaussShader = new HGaussShader();
        vGaussShader = new VGaussShader();
        mixShader = new MixShader();

        renderContext.getCamera().setZoomFactor(0.02f);

        Track testTrack = gameContext.getDataManager().getTrack("test_track");
        gameContext.getGameState().setCurrentTrack(testTrack);
        EntityCar playerEntity = new EntityCar(0.0f, 0.0f, 0.0f, gameContext.getDataManager().getCars()[0]);
        playerEntity.setPhysics(CarPhysicsFactory.createDriveable(gameContext, playerEntity));
        gameContext.getGameState().setPlayerEntity(playerEntity);
        gameContext.getGameState().addEntity(playerEntity);

        for (int i = 0; i < 30; i += 2) {
            EntityCar e = new EntityCar(i, 0.0f, 3.0f, gameContext.getDataManager().getCars()[0]);
            gameContext.getGameState().addEntity(e);
        }

        renderContext.setGuiMatrix(new Matrix4f());

        for (IRenderer renderer : renderers)
            renderer.setup(gameContext);

        gameContext.getTimer().reset();

        Log.i("Initialization completed");
    }

    private void render() {
        renderContext.setWorldMatrix(renderContext.getCamera().calculateMatrix());
        renderContext.getGuiMatrix().setOrtho2D(0.0f, gameContext.getGameWindow().getWidth(), gameContext.getGameWindow().getHeight(), 0.0f);

        colorFbo.bind();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        for (IRenderer renderer : renderers)
            renderer.render(renderContext, gameContext, RenderPass.COLOR);

        glowFbo.bind();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        for (IRenderer renderer : renderers)
            renderer.render(renderContext, gameContext, RenderPass.GLOW);
        glowFbo.unbind();

        // Beautiful postproc pipeline in 3.. 2... 1..
        postProcessing.begin();
        hGaussShader.bind();
        hGaussShader.setTargetWidth(gaussFbo.getWidth());
        postProcessing.copyFbo(glowFbo, gaussFbo);
        hGaussShader.unbind();
        vGaussShader.bind();
        vGaussShader.setTargetHeight(gaussFbo2.getHeight());
        postProcessing.copyFbo(gaussFbo, gaussFbo2);
        vGaussShader.unbind();
        mixShader.bind();
        glClear(GL_COLOR_BUFFER_BIT);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, colorFbo.getColorTexture());
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, gaussFbo2.getColorTexture());
        postProcessing.fullscreenQuad();
        mixShader.unbind();
        postProcessing.end();
    }

    private void onResize(int width, int height) {
        initFBOs(width, height);
    }

    private void initFBOs(int width, int height) {
        colorFbo = new Fbo(gameWindow, width, height, Fbo.DepthBufferType.NONE);
        glowFbo = new Fbo(gameWindow, width, height, Fbo.DepthBufferType.NONE);
        gaussFbo = new Fbo(gameWindow, width / 2, height / 2, Fbo.DepthBufferType.NONE);
        gaussFbo2 = new Fbo(gameWindow, width / 2, height / 2, Fbo.DepthBufferType.NONE);
    }

    private void destroy() {
        for (IRenderer renderer : renderers)
            renderer.destroy(gameContext);
    }

}
