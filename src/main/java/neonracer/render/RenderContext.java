package neonracer.render;

import neonracer.core.GameContext;
import neonracer.gui.font.Fonts;
import neonracer.gui.util.PrimitiveRenderer;
import neonracer.render.engine.Camera;
import neonracer.render.gl.shaders.Shader;
import org.joml.Matrix4f;

public class RenderContext {

    private GameContext gameContext;

    private Camera camera;

    private Fonts fonts;

    private PrimitiveRenderer primitiveRenderer;

    private Matrix4f worldMatrix = new Matrix4f();

    private Matrix4f guiMatrix = new Matrix4f();

    public RenderContext(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    public void initialize() {
        camera = new Camera(gameContext);

        fonts = new Fonts();
        fonts.initialize(this, gameContext);

        primitiveRenderer = new PrimitiveRenderer(this);
        primitiveRenderer.initialize();
    }

    public void updateMatrices() {
        GameWindow gameWindow = gameContext.getGameWindow();
        worldMatrix = camera.calculateMatrix();
        guiMatrix.setOrtho2D(0.0f, gameWindow.getWidth(), gameWindow.getHeight(), 0.0f);
    }

    public GameContext getGameContext() {
        return gameContext;
    }

    public Camera getCamera() {
        return camera;
    }

    public Fonts getFonts() {
        return fonts;
    }

    public PrimitiveRenderer getPrimitiveRenderer() {
        return primitiveRenderer;
    }

    public Matrix4f getWorldMatrix() {
        return worldMatrix;
    }

    public Matrix4f getGuiMatrix() {
        return guiMatrix;
    }

    public <T extends Shader> T getShader(Class<T> shaderClass) {
        return gameContext.getShaderProvider().getShader(shaderClass);
    }

}
