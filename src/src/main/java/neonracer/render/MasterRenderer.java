package neonracer.render;

import neonracer.core.GameContext;
import neonracer.model.track.Track;
import neonracer.render.engine.Camera;
import neonracer.render.engine.renderers.DebugRenderer;
import neonracer.render.engine.renderers.IRenderer;
import neonracer.render.engine.renderers.TrackRenderer;
import neonracer.util.Log;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    private RenderContext renderContext;

    private GameContext gameContext;

    private IRenderer[] renderers = new IRenderer[]{
            new TrackRenderer(),
            new DebugRenderer()
    };

    public MasterRenderer(GameContext context) {
        this.gameContext = context;
        Camera camera = new Camera(context);
        this.renderContext = new RenderContext(camera);
    }

    public void startLoop() {
        setup();
        while (!gameContext.getGameWindow().shouldClose()) {
            render();
            gameContext.getGameWindow().update();
        }
        destroy();
    }

    private void setup() {
        Log.i("Initializing renderer...");
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        renderContext.getCamera().setZoomFactor(0.05f);

        Track testTrack = gameContext.getDataManager().getTrack("test_track");
        gameContext.getGameState().setCurrentTrack(testTrack);

        renderContext.setGuiMatrix(new Matrix4f());

        for (IRenderer renderer : renderers)
            renderer.setup(gameContext);
        Log.i("Initialization completed");
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, gameContext.getGameWindow().getWidth(), gameContext.getGameWindow().getHeight());

        renderContext.setWorldMatrix(renderContext.getCamera().calculateMatrix());
        renderContext.getGuiMatrix().setOrtho2D(0.0f, gameContext.getGameWindow().getWidth(), gameContext.getGameWindow().getHeight(), 0.0f);

        for (IRenderer renderer : renderers)
            renderer.render(renderContext, gameContext);
    }

    private void destroy() {
        for (IRenderer renderer : renderers)
            renderer.destroy(gameContext);
    }

}
