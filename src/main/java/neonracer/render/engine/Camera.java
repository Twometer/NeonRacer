package neonracer.render.engine;

import neonracer.core.GameContext;
import neonracer.model.entity.EntityCar;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {

    private static final float ROTATION_SPEED = 0.01f;

    private GameContext gameContext;

    private Vector2f centerPoint = new Vector2f();

    private float rotation = 0.0f;

    private float zoomFactor = 1.0f;

    private Matrix4f matrix = new Matrix4f();

    public Camera(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    private void setCenterPoint(Vector2f centerPoint) {
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

    public float getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(float zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public void zoom(float zoomFactor) {
        this.zoomFactor += zoomFactor;
        this.zoomFactor = Math.max(0.0f, this.zoomFactor);
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

    public void smoothFollow(EntityCar car) {
        setCenterPoint(car.getPosition());

        float rot = car.getRotation();

        float diff = Math.abs(rot - rotation);
        if (rot > rotation)
            rotation += ROTATION_SPEED * diff;
        else if (rot < rotation)
            rotation -= ROTATION_SPEED * diff;
    }

}
