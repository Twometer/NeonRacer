package neonracer.model.entities;

import neonracer.model.car.CarModel;
import neonracer.render.gl.Texture;

public class EntityCar extends Entity {

    private CarModel carModel;

    @Override
    public Texture getColorTexture() {
        return carModel.getColorTexture();
    }

    @Override
    public Texture getGlowTexture() {
        return carModel.getGlowTexture();
    }
}
