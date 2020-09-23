package neonracer.model.entity;

import neonracer.core.GameContext;
import neonracer.model.car.Car;
import neonracer.phys.entity.car.CarPhysicsFactory;
import neonracer.render.gl.core.Texture;
import neonracer.stats.CarStats;

public class EntityCar extends Entity {

    private final Car car;

    private final String username;

    private final CarStats carStats = new CarStats();

    public EntityCar(long entityId, float x, float y, float rotation, Car car, String username) {
        super("car", x, y, rotation, null);
        this.entityId = entityId;
        this.car = car;
        this.username = username;
    }

    public Car getCar() {
        return car;
    }

    public CarStats getCarStats() {
        return carStats;
    }

    @Override
    public Texture getColorTexture() {
        return car.getColorTexture();
    }

    @Override
    public Texture getGlowTexture() {
        return car.getGlowTexture();
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void onInitialize(GameContext gameContext) {
        super.onInitialize(gameContext);
        if (getPhysics() == null)
            setPhysics(CarPhysicsFactory.createStatic(gameContext, this));
    }

}
