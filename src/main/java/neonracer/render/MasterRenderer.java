package neonracer.render;

import neonracer.core.GameContext;
import neonracer.gui.GuiManager;
import neonracer.gui.screen.ConnectScreen;
import neonracer.model.entity.EntityCar;
import neonracer.model.track.Track;
import neonracer.phys.entity.car.CarPhysicsFactory;
import neonracer.render.engine.RenderPass;
import neonracer.render.engine.postproc.PostProcessing;
import neonracer.render.engine.renderers.DebugRenderer;
import neonracer.render.engine.renderers.EntityRenderer;
import neonracer.render.engine.renderers.IRenderer;
import neonracer.render.engine.renderers.TrackRenderer;
import neonracer.util.Log;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    private GameContext gameContext;

    private RenderContext renderContext;

    private GameWindow gameWindow;

    private GuiManager guiManager;

    private PostProcessing postProcessing;

    private IRenderer[] renderers = new IRenderer[]{
            new TrackRenderer(),
            new EntityRenderer(),
            new DebugRenderer()
    };

    public MasterRenderer(GameContext context) {
        this.gameContext = context;
        this.gameWindow = gameContext.getGameWindow();
        this.renderContext = new RenderContext(gameContext);
        this.guiManager = new GuiManager(renderContext);
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

    private void setup() {
        Log.i("Initializing renderer...");
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        postProcessing = new PostProcessing(gameContext);

        gameWindow.setSizeChangedListener((width, height) -> {
            guiManager.resize(width, height);
            postProcessing.onResize(width, height);
        });

        renderContext.initialize();
        renderContext.getCamera().setZoomFactor(0.02f);

        Track testTrack = gameContext.getDataManager().getTrack("test_track");
        gameContext.getGameState().setCurrentTrack(testTrack);
        EntityCar playerEntity = new EntityCar(0.0f, 0.0f, 0.0f, gameContext.getDataManager().getCar("kart"));
        playerEntity.setPhysics(CarPhysicsFactory.createDriveable(gameContext, playerEntity));
        gameContext.getGameState().setPlayerEntity(playerEntity);
        gameContext.getGameState().addEntity(playerEntity);

        for (int i = 0; i < 30; i += 2) {
            EntityCar e = new EntityCar(i, 0.0f, 3.0f, gameContext.getDataManager().getCars()[i % 3]);
            gameContext.getGameState().addEntity(e);
        }

        for (IRenderer renderer : renderers)
            renderer.setup(renderContext, gameContext);

        guiManager.show(ConnectScreen.class);

        gameContext.getTimer().reset();

        Log.i("Initialization completed");
    }

    private void render() {
        renderContext.updateMatrices();

        gameContext.getKeyboardState().update(gameWindow);
        gameContext.getMouseState().update(gameWindow);

        postProcessing.beginPass(RenderPass.COLOR);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        for (IRenderer renderer : renderers)
            renderer.render(renderContext, gameContext, RenderPass.COLOR);
        guiManager.draw(RenderPass.COLOR);

        postProcessing.beginPass(RenderPass.GLOW);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        for (IRenderer renderer : renderers)
            renderer.render(renderContext, gameContext, RenderPass.GLOW);
        guiManager.draw(RenderPass.GLOW);

        postProcessing.draw();
    }

    private void tick() {
        GameWindow gameWindow = gameContext.getGameWindow();
        gameContext.getKeyboardState().update(gameWindow);
        gameContext.getMouseState().update(gameWindow);

        gameContext.getPhysicsEngine().onTick();

        renderContext.getCamera().smoothFollow(gameContext.getGameState().getPlayerEntity());
    }

    private void destroy() {
        for (IRenderer renderer : renderers)
            renderer.destroy(renderContext, gameContext);
    }

}
