package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.model.entity.EntityCar;
import neonracer.phys.entity.car.body.CarBody;
import neonracer.phys.entity.car.body.CarBodyBuilder;

public class CarPhysicsFactory {

    public static AbstractCarPhysics createDriveable(GameContext context, EntityCar car) {
        CarBody body = CarBodyBuilder.build(context, car);
        return new DriveableCarPhysics(context, body, body.getTires(), body.getLeftJoint(), body.getRightJoint(), car.getCar().getDragCoefficient());
    }

    public static AbstractCarPhysics createStatic(GameContext context, EntityCar car) {
        CarBody body = CarBodyBuilder.build(context, car);
        return new StaticCarPhysics(context, body, body.getTires(), body.getLeftJoint(), body.getRightJoint(), car.getCar().getDragCoefficient());
    }

}
