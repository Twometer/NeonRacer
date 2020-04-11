package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.model.entity.EntityCar;
import neonracer.phys.entity.car.body.CarBody;
import neonracer.phys.entity.car.body.CarBodyBuilder;
import org.jbox2d.dynamics.BodyType;

public class CarPhysicsFactory {

    public static AbstractCarPhysics createDriveable(GameContext context, EntityCar car) {
        CarBody body = CarBodyBuilder.build(context, car, BodyType.DYNAMIC);
        return new DrivableCarPhysics(context, body, body.getTires(), body.getLeftJoint(), body.getRightJoint(),
                car.getCar().getDragCoefficient(), car.getCar().getSteeringLockAngle(), car.getCar().getSteeringAngularVelocity());
    }

    public static AbstractCarPhysics createStatic(GameContext context, EntityCar car) {
        CarBody body = CarBodyBuilder.build(context, car, BodyType.STATIC);
        return new StaticCarPhysics(context, body, body.getTires(), body.getLeftJoint(), body.getRightJoint(),
                car.getCar().getDragCoefficient());
    }

}
