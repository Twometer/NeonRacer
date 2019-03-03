package neonracer.render;

import neonracer.core.GameContext;
import neonracer.model.track.Track;
import neonracer.render.engine.Camera;
import neonracer.render.engine.renderers.IRenderer;
import neonracer.render.engine.renderers.TrackRenderer;
import neonracer.util.Log;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    private RenderContext renderContext;

    private GameContext gameContext;

    private Camera camera;

    private IRenderer[] renderers = new IRenderer[]{
            new TrackRenderer()
    };

    public MasterRenderer(GameContext context) {
        this.renderContext = new RenderContext();
        this.gameContext = context;
        this.camera = new Camera(context);
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

        this.camera.setZoomFactor(0.05f);

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

        renderContext.setWorldMatrix(camera.calculateMatrix());

        for (IRenderer renderer : renderers)
            renderer.render(renderContext, gameContext);
    }

    private void destroy() {
        for (IRenderer renderer : renderers)
            renderer.destroy(gameContext);
    }

}
