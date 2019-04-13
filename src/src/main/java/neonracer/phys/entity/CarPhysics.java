package neonracer.phys.entity;

import neonracer.core.ControlState;
import neonracer.core.GameContext;
import neonracer.model.entity.EntityCar;
import neonracer.phys.Box2dHelper;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class CarPhysics extends EntityPhysics {

    // Ported from http://www.iforce2d.net/src/iforce2d_TopdownCar.h

    private Body body;

    private List<Tire> tires;

    private RevoluteJoint flJoint;

    private RevoluteJoint frJoint;

    private CarPhysics(Body body, List<Tire> tires, RevoluteJoint flJoint, RevoluteJoint frJoint) {
        this.body = body;
        this.tires = tires;
        this.flJoint = flJoint;
        this.frJoint = frJoint;
    }

    public static CarPhysics create(GameContext context, EntityCar car) {
        List<Tire> tires = new ArrayList<>();
        World world = context.getPhysicsEngine().getWorld();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(Box2dHelper.toVec2(car.getPosition()));

        PolygonShape shape = new PolygonShape();
        //shape.setAsBox(car.getWidth() / 2f, car.getHeight() / 2f);

        Vec2[] vecs = new Vec2[]{
                new Vec2(1.5f, 0f),
                new Vec2(3f, 2.5f),
                new Vec2(2.8f, 5.5f),
                new Vec2(1f, 7f),
                new Vec2(-1f, 7f),
                new Vec2(-2.8f, 5.5f),
                new Vec2(-3f, 2.5f),
                new Vec2(-1.5f, 0f)
        };

        shape.set(vecs, vecs.length);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setAngularDamping(5);

        float maxForwardSpeed = 300;
        float maxBackwardSpeed = -40;
        float backTireMaxDriveForce = 950;
        float frontTireMaxDriveForce = 400;
        float backTireMaxLateralImpulse = 9f;
        float frontTireMaxLateralImpulse = 9f;

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = body;
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;
        jointDef.localAnchorB.setZero();

        Tire backLeft = new Tire(world, maxForwardSpeed, maxBackwardSpeed, backTireMaxDriveForce, backTireMaxLateralImpulse);
        jointDef.bodyB = backLeft.body;
        jointDef.localAnchorA.set(-3, 0.75f);
        world.createJoint(jointDef);
        tires.add(backLeft);

        Tire backRight = new Tire(world, maxForwardSpeed, maxBackwardSpeed, backTireMaxDriveForce, backTireMaxLateralImpulse);
        jointDef.bodyB = backRight.body;
        jointDef.localAnchorA.set(3, 0.75f);
        world.createJoint(jointDef);
        tires.add(backRight);

        Tire frontLeft = new Tire(world, maxForwardSpeed, maxBackwardSpeed, frontTireMaxDriveForce, frontTireMaxLateralImpulse);
        jointDef.bodyB = frontLeft.body;
        jointDef.localAnchorA.set(-3, 5.5f);
        RevoluteJoint flJoint = (RevoluteJoint) world.createJoint(jointDef);
        tires.add(frontLeft);

        Tire frontRight = new Tire(world, maxForwardSpeed, maxBackwardSpeed, frontTireMaxDriveForce, frontTireMaxLateralImpulse);
        jointDef.bodyB = frontRight.body;
        jointDef.localAnchorA.set(3, 5.5f);
        RevoluteJoint frJoint = (RevoluteJoint) world.createJoint(jointDef);
        tires.add(frontRight);

        return new CarPhysics(body, tires, flJoint, frJoint);
    }

    public void update(ControlState controlState) {
        for (Tire tire : tires) {
            tire.updateFriction();
            tire.updateDrive(controlState);
        }
        float lockAngle = (float) Math.toRadians(35);
        float turnSpeedPerSec = (float) Math.toRadians(320);//from lock to lock in 0.25 sec
        float turnPerTimeStep = turnSpeedPerSec / 60.0f;
        float desiredAngle = 0;
        if (controlState.isLeft())
            desiredAngle = lockAngle;
        else if (controlState.isRight())
            desiredAngle = -lockAngle;
        float angleNow = flJoint.getJointAngle();
        float angleToTurn = desiredAngle - angleNow;
        angleToTurn = clamp(angleToTurn, -turnPerTimeStep, turnPerTimeStep);

        float newAngle = angleNow + angleToTurn;
        flJoint.setLimits(newAngle, newAngle);
        frJoint.setLimits(newAngle, newAngle);
    }

    private float clamp(float f, float min, float max) {
        if (f < min) return min;
        if (f > max) return max;
        return f;
    }

    @Override
    public Vector2f getPosition() {
        return Box2dHelper.toVector2f(body.getPosition());
    }

    @Override
    public float getRotation() {
        return body.getAngle();
    }

    private static class Tire {

        float maxForwardSpeed;
        float maxReverseSpeed;
        float maxDriveForce;
        float maxLateralImpulse;
        // TODO: Don't hardcode this
        float currentTraction = 1f;
        float currentDrag = 30f;
        private Body body;

        Tire(World world, float maxForwardSpeed, float maxReverseSpeed, float maxDriveForce, float maxLateralImpulse) {
            this.maxForwardSpeed = maxForwardSpeed;
            this.maxReverseSpeed = maxReverseSpeed;
            this.maxDriveForce = maxDriveForce;
            this.maxLateralImpulse = maxLateralImpulse;

            BodyDef def = new BodyDef();
            def.type = BodyType.DYNAMIC;
            body = world.createBody(def);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.5f, 1.25f);
            Fixture fixture = body.createFixture(shape, 1);
            body.setUserData(this);
        }

        private Vec2 getLateralVelocity() {
            Vec2 currentRightNormal = body.getWorldVector(new Vec2(1, 0));
            return currentRightNormal.mul(Vec2.dot(currentRightNormal, body.getLinearVelocity()));
        }

        private Vec2 getForwardVelocity() {
            Vec2 currentForwardNormal = body.getWorldVector(new Vec2(0, 1));
            return currentForwardNormal.mul(Vec2.dot(currentForwardNormal, body.getLinearVelocity()));
        }

        void updateFriction() {
            body.applyAngularImpulse(currentTraction * 0.1f * body.getInertia() * -body.getAngularVelocity());

            Vec2 currentForwardNormal = getForwardVelocity();
            float currentForwardSpeed = currentForwardNormal.normalize();
            float dragForceMagnitude = -0.25f * currentForwardSpeed;
            dragForceMagnitude *= currentDrag;
            body.applyForce(currentForwardNormal.mul(dragForceMagnitude).mul(currentTraction), body.getWorldCenter());
        }

        void updateDrive(ControlState controlState) {
            float desiredSpeed = 0;
            if (controlState.isForward())
                desiredSpeed = maxForwardSpeed;
            else if (controlState.isReverse())
                desiredSpeed = maxReverseSpeed;

            float maxLateralImpulse = this.maxLateralImpulse;
            float maxDriveForce = this.maxDriveForce;

            /*
            NITRO MODE
             */
            if (controlState.isSpacebar()) {

                desiredSpeed *= 2;
                maxDriveForce *= 4;
                maxLateralImpulse *= 4;

            }

            Vec2 currentForwardNormal = body.getWorldVector(new Vec2(0, 1));
            float currentSpeed = Vec2.dot(getForwardVelocity(), currentForwardNormal);

            float force = 0;
            if (desiredSpeed > currentSpeed)
                force = maxDriveForce;
            else if (desiredSpeed < currentSpeed)
                force = -maxDriveForce * 0.5f;

            float speedFactor = currentSpeed / 120f;
            Vec2 driveImpulse = currentForwardNormal.mul(force / 60.0f);
            if (driveImpulse.length() > maxLateralImpulse)
                driveImpulse = driveImpulse.mul(maxLateralImpulse / driveImpulse.length());

            Vec2 lateralFrictionImpulse = getLateralVelocity().mul(-body.getMass());
            float lateralImpulseAvailable = 2.0f * speedFactor * maxLateralImpulse;
            if (lateralImpulseAvailable < 0.5f * maxLateralImpulse)
                lateralImpulseAvailable = 0.5f * maxLateralImpulse;

            if (lateralFrictionImpulse.length() > lateralImpulseAvailable)
                lateralFrictionImpulse = lateralFrictionImpulse.mul(lateralImpulseAvailable / lateralFrictionImpulse.length());

            Vec2 impulse = driveImpulse.add(lateralFrictionImpulse);
            if (impulse.length() > maxLateralImpulse)
                impulse = impulse.mul(maxLateralImpulse / impulse.length());


            body.applyLinearImpulse(impulse.mul(currentTraction), body.getWorldCenter());

            //body.applyForce(currentForwardNormal.mul(currentTraction).mul(force), body.getWorldCenter());
        }

    }
}
