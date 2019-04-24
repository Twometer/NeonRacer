package neonracer.designer;

import neonracer.core.GameContext;
import neonracer.core.GameContextFactory;
import neonracer.render.GameWindow;
import neonracer.render.RenderContext;
import neonracer.render.engine.font.FontRenderer;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

class TrackDesigner {

    private GameContext gameContext = GameContextFactory.createForDesigner();

    private RenderContext renderContext = new RenderContext();

    private FontRenderer fontRenderer = new FontRenderer("lucida");

    void start() throws IOException {
        gameContext.initialize();
        fontRenderer.setup(gameContext);
        setup();
        startRenderLoop();
    }

    private void setup() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    private void startRenderLoop() {
        GameWindow gameWindow = gameContext.getGameWindow();
        while (!gameWindow.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            renderContext.getGuiMatrix().setOrtho2D(0.0f, gameWindow.getWidth(), gameWindow.getHeight(), 0.0f);
            render();
            gameWindow.update();
        }
    }

    private void render() {
        fontRenderer.draw(renderContext, "NeonRacer Track Designer", 0, 0, 0.4f);


    }

}
