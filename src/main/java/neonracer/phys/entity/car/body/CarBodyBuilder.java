package neonracer.phys.entity.car.body;

import neonracer.core.GameContext;
import neonracer.model.car.Car;
import neonracer.model.entity.EntityCar;
import neonracer.phys.Box2dHelper;
import neonracer.phys.entity.car.Tire;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import java.util.ArrayList;
import java.util.List;

public class CarBodyBuilder {

    public static CarBody build(GameContext context, EntityCar car, BodyType type) {
        List<Tire> tires = new ArrayList<>();
        World world = context.getPhysicsEngine().getWorld();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(Box2dHelper.toVec2(car.getPosition()));
        bodyDef.angle = car.getRotation();

        PolygonShape shape = new PolygonShape();

        float halfWidth = car.getWidth() / 2;

        // The base shape of the car is a rectangle. Then we cut away
        // each corner so there is space for the wheels, which turns
        // the rectangle into an octagon. This octagon is defined here:
        Vec2[] vecs = new Vec2[]{
                // Right half of the octagon
                new Vec2(halfWidth - 0.22f, 0f),
                new Vec2(halfWidth, 0.7f),
                new Vec2(halfWidth - 0.05f, car.getHeight() / 2),
                new Vec2(halfWidth / 2, car.getHeight()),

                // Left half: Same as right half, just mirrored
                new Vec2(-(halfWidth / 2), car.getHeight()),
                new Vec2(-(halfWidth - 0.05f), car.getHeight() / 2),
                new Vec2(-(halfWidth), 0.7f),
                new Vec2(-(halfWidth - 0.22f), 0f)
        };

        shape.set(vecs, vecs.length);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setAngularDamping(5);

        RevoluteJointDef tireJoint = new RevoluteJointDef();
        tireJoint.bodyA = body;
        tireJoint.enableLimit = true;
        tireJoint.lowerAngle = 0;
        tireJoint.upperAngle = 0;
        tireJoint.localAnchorB.setZero();

        Car carDef = car.getCar();

        Tire backLeft = createTire(context, world, carDef, false, -halfWidth, 0.01f, tireJoint);
        world.createJoint(tireJoint);
        tires.add(backLeft);

        Tire backRight = createTire(context, world, carDef, false, halfWidth, 0.01f, tireJoint);
        world.createJoint(tireJoint);
        tires.add(backRight);

        Tire frontLeft = createTire(context, world, carDef, true, -halfWidth, car.getHeight() - 0.18f * 2, tireJoint);
        RevoluteJoint leftJoint = (RevoluteJoint) world.createJoint(tireJoint);
        tires.add(frontLeft);

        Tire frontRight = createTire(context, world, carDef, true, halfWidth, car.getHeight() - 0.18f * 2, tireJoint);
        RevoluteJoint rightJoint = (RevoluteJoint) world.createJoint(tireJoint);
        tires.add(frontRight);

        return new CarBody(body, tires, leftJoint, rightJoint);
    }

    private static Tire createTire(GameContext gameContext, World world, Car car, boolean front, float x, float y, RevoluteJointDef jointDef) {
        float maxDriveForce = front ? car.getMaxFrontForce() : car.getMaxBackForce();
        Tire tire = new Tire(gameContext, world, car.getMaxForwardSpeed(), car.getMaxReverseSpeed(), maxDriveForce, car.getMaxLateralForce());
        jointDef.bodyB = tire.getBody();
        jointDef.localAnchorA.set(x, y);
        return tire;
    }

}
