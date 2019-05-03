package neonracer.render.engine;

import neonracer.core.GameContext;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {

    private GameContext gameContext;

    private Vector2f centerPoint = new Vector2f();

    private float rotation = 0.0f;

    private float zoomFactor = 1.0f;

    private Matrix4f matrix = new Matrix4f();

    public Camera(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    void setCenterPoint(Vector2f centerPoint) {
        this.centerPoint = centerPoint;
    }

    public Vector2f getCenterPoint() {
        return centerPoint;
    }

    public float getRotation() {
        return rotation;
    }

    public void translate(float x, float y) {
        this.centerPoint.add(x, y);
    }

    public void setZoomFactor(float zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Matrix4f calculateMatrix() {
        float aspectRatio = gameContext.getGameWindow().getHeight() / (float) gameContext.getGameWindow().getWidth();
        float cameraWidth = 1 / zoomFactor;
        float cameraHeight = cameraWidth * aspectRatio;
        matrix.setOrtho2D(-cameraWidth / 2, +cameraWidth / 2, -cameraHeight / 2, +cameraHeight / 2);
        matrix = matrix.mul(new Matrix4f().setRotationXYZ(0, 0, -rotation));
        matrix = matrix.mul(new Matrix4f().setTranslation(-centerPoint.x, -centerPoint.y, 0));
        return matrix;
    }

}