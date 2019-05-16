package neonracer.model.entity;

import neonracer.core.GameContext;
import neonracer.model.car.Car;
import neonracer.phys.entity.car.CarPhysicsFactory;
import neonracer.render.gl.core.Texture;
import neonracer.stats.CarStats;

public class EntityCar extends Entity {

    private Car car;

    private CarStats carStats = new CarStats();

    public EntityCar(long entityId, float x, float y, float rotation, Car car) {
        super("car", x, y, rotation, null);
        this.entityId = entityId;
        this.car = car;
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

    @Override
    public void onInitialize(GameContext gameContext) {
        super.onInitialize(gameContext);
        if (getPhysics() == null)
            setPhysics(CarPhysicsFactory.createStatic(gameContext, this));
    }

}
