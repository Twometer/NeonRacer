package neonracer.phys.entity.car;

import neonracer.core.ControlState;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class Tire {

    // Ported from http://www.iforce2d.net/src/iforce2d_TopdownCar.h

    private float maxForwardSpeed;
    private float maxReverseSpeed;
    private float maxDriveForce;
    private float maxLateralImpulse;
    // TODO: Don't hardcode this
    private float currentTraction = 1f;
    private float currentDrag = 30f;
    private Body body;

    public Tire(World world, float maxForwardSpeed, float maxReverseSpeed, float maxDriveForce, float maxLateralImpulse) {
        this.maxForwardSpeed = maxForwardSpeed;
        this.maxReverseSpeed = maxReverseSpeed;
        this.maxDriveForce = maxDriveForce;
        this.maxLateralImpulse = maxLateralImpulse;

        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.09f, 0.18f);
        body.createFixture(shape, 1);
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

        // Makes weird car glitches a little less worse
        if (currentForwardNormal.length() < 0.02)
            return;

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

        updateDrive(desiredSpeed);
    }

    void updateDrive(float desiredSpeed) {
        float maxLateralImpulse = this.maxLateralImpulse;
        float maxDriveForce = this.maxDriveForce;

        Vec2 currentForwardNormal = body.getWorldVector(new Vec2(0, 1));
        float currentSpeed = Vec2.dot(getForwardVelocity(), currentForwardNormal);

        float force = 0;
        if (desiredSpeed > currentSpeed)
            force = maxDriveForce;
        else if (desiredSpeed < currentSpeed && desiredSpeed != 0)
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
    }

    public Body getBody() {
        return body;
    }

}
