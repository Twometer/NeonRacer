package neonracer.render.engine;

import neonracer.model.entity.EntityCar;

public class CameraManager {

    private static final float ROTATION_SPEED = 0.01f;

    private Camera camera;

    private float currentRotation;

    public CameraManager(Camera camera) {
        this.camera = camera;
    }

    public void smoothFollow(EntityCar car) {
        camera.setCenterPoint(car.getPosition());

        float rot = car.getRotation();

        float diff = Math.abs(rot - currentRotation);
        if (rot > currentRotation)
            currentRotation += ROTATION_SPEED * diff;
        else if (rot < currentRotation)
            currentRotation -= ROTATION_SPEED * diff;

        camera.setRotation(currentRotation);
    }

}
