package neonracer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.core.GameContext;
import neonracer.model.car.Car;
import neonracer.phys.PhysEntity;
import neonracer.render.gl.core.Texture;

public class EntityCar extends PhysEntity {

    private Car car;

    public EntityCar(float x, float y, float rotation, Car car, GameContext gameContext) {
        super("car", x, y, rotation, gameContext);
        this.car = car;
    }

    @JsonCreator
    public EntityCar(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation) {
        super(type, x, y, rotation, null);
    }

    public EntityCar( String type,  float x,  float y,  float rotation, GameContext gameContext) {
        super(type, x, y, rotation, gameContext);
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

}
