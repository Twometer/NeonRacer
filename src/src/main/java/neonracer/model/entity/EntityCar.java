package neonracer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.core.GameContext;
import neonracer.model.car.Car;
import neonracer.phys.entity.car.CarPhysicsFactory;
import neonracer.render.gl.core.Texture;

public class EntityCar extends Entity {

    private Car car;

    public EntityCar(float x, float y, float rotation, Car car) {
        super("car", x, y, rotation);
        this.car = car;
    }

    @JsonCreator
    public EntityCar(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation) {
        super(type, x, y, rotation);
    }

    public Car getCar() {
        return car;
    }

    @Override
    public Texture getColorTexture() {
        return car.getColorTexture();
    }

    @Override
    public Texture getGlowTexture() {
        return car.getGlowTexture();
    }

    @Override
    public void onInitialize(GameContext gameContext) {
        super.onInitialize(gameContext);
        if (getPhysics() == null)
            setPhysics(CarPhysicsFactory.createStatic(gameContext, this));
    }

}
