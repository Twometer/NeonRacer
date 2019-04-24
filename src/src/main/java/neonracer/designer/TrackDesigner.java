package neonracer.designer;

import neonracer.core.GameContext;
import neonracer.core.GameContextFactory;
import neonracer.render.GameWindow;
import neonracer.render.RenderContext;
import neonracer.render.engine.Camera;
import neonracer.render.engine.font.FontRenderer;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

class TrackDesigner {

    private GameContext gameContext = GameContextFactory.createForDesigner();

    private RenderContext renderContext = new RenderContext(new Camera(gameContext));

    private FontRenderer fontRenderer = new FontRenderer("lucida");

    private BasicButton testButton = new BasicButton(fontRenderer, 10, 10, "New Node");

    void start() throws IOException {
        gameContext.initialize();
        fontRenderer.setup(gameContext);
        setup();
        startRenderLoop();
    }

    private void setup() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        testButton.setOnClickListener(() -> System.out.println("Button pressed"));
    }

    private void startRenderLoop() {
        GameWindow gameWindow = gameContext.getGameWindow();
        while (!gameWindow.shouldClose()) {
            render();
            gameWindow.update();
        }
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        renderContext.updateMatrices(gameContext);

        GameWindow gameWindow = gameContext.getGameWindow();
        gameContext.getKeyboardState().update(gameWindow);
        gameContext.getMouseState().update(gameWindow);

        testButton.draw(renderContext, gameContext);
    }

}
