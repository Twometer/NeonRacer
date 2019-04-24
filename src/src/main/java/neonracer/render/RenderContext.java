package neonracer.render;

import neonracer.core.GameContext;
import neonracer.render.engine.Camera;
import neonracer.render.engine.CameraManager;
import org.joml.Matrix4f;

public class RenderContext {

    private Camera camera;

    private CameraManager cameraManager;

    private Matrix4f worldMatrix = new Matrix4f();

    private Matrix4f guiMatrix = new Matrix4f();

    public RenderContext(Camera camera) {
        this.camera = camera;
        this.cameraManager = new CameraManager(camera);
    }

    public void updateMatrices(GameContext gameContext) {
        GameWindow gameWindow = gameContext.getGameWindow();
        worldMatrix = camera.calculateMatrix();
        guiMatrix.setOrtho2D(0.0f, gameWindow.getWidth(), gameWindow.getHeight(), 0.0f);
    }

    public Camera getCamera() {
        return camera;
    }

    CameraManager getCameraManager() {
        return cameraManager;
    }

    public Matrix4f getWorldMatrix() {
        return worldMatrix;
    }

    public Matrix4f getGuiMatrix() {
        return guiMatrix;
    }

}
