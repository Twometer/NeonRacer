package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.model.entity.EntityCar;
import neonracer.phys.entity.car.body.CarBody;
import neonracer.phys.entity.car.body.CarBodyBuilder;
import org.jbox2d.dynamics.BodyType;

public class CarPhysicsFactory {

    public static AbstractCarPhysics createDriveable(GameContext context, EntityCar car) {
        CarBody body = CarBodyBuilder.build(context, car, BodyType.DYNAMIC);
<<<<<<< HEAD
        return new DrivableCarPhysics(context, body, body.getTires(), body.getLeftJoint(), body.getRightJoint(), car.getCar().getDragCoefficient());
=======
        return new DriveableCarPhysics(context, body, body.getTires(), body.getLeftJoint(), body.getRightJoint(),
                car.getCar().getDragCoefficient(), car.getCar().getSteeringLockAngle(), car.getCar().getSteeringAngularVelocity());
>>>>>>> f0a12b077ab45b8d80a55024ee9ab3aef4eca7b3
    }

    public static AbstractCarPhysics createStatic(GameContext context, EntityCar car) {
        CarBody body = CarBodyBuilder.build(context, car, BodyType.STATIC);
        return new StaticCarPhysics(context, body, body.getTires(), body.getLeftJoint(), body.getRightJoint(),
                car.getCar().getDragCoefficient());
    }

}
