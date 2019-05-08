package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.gui.input.KeyboardState;
import neonracer.model.track.Material;
import neonracer.phys.Box2dHelper;
import neonracer.render.engine.collider.TrackColliderResult;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

public class Tire {

    // Ported from http://www.iforce2d.net/src/iforce2d_TopdownCar.h

    private GameContext gameContext;

    private float maxForwardSpeed;
    private float maxReverseSpeed;
    private float maxDriveForce;
    private float maxLateralImpulse;
    private Material currentMaterial;
    private Body body;

    public Tire(GameContext gameContext, World world, float maxForwardSpeed, float maxReverseSpeed, float maxDriveForce, float maxLateralImpulse) {
        this.gameContext = gameContext;
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
        // Load traction and drag here
        float frictionValueRoll = -0.25f;
        float frictionValue = -12.25f;

        Vector2f vec = Box2dHelper.toVector2f(body.getPosition());
        TrackColliderResult colliderResult = gameContext.getGameState().getCurrentTrack().getCollider().collides(vec);

        if (colliderResult.isCollided()) currentMaterial = colliderResult.getCurrentMaterial();
        else currentMaterial = gameContext.getGameState().getCurrentTrack().getBackgroundMaterial();

        body.applyAngularImpulse(currentMaterial.getTraction() * 0.1f * body.getInertia() * -body.getAngularVelocity());

        Vec2 currentForwardNormal = getForwardVelocity();

        // Makes weird car glitches a little less worse
        if (currentForwardNormal.length() < 0.02)
            return;

        Vec2 dragForce = new Vec2(Math.signum(getForwardVelocity().x) * frictionValueRoll, Math.signum(getLateralVelocity().y) * frictionValue);
        dragForce = dragForce.mul(currentMaterial.getDrag());
        body.applyForce(dragForce, body.getWorldCenter());
    }

    void updateDrive(KeyboardState keyboardState) {
        float desiredSpeed = 0;
        if (keyboardState.isForward())
            desiredSpeed = maxForwardSpeed;
        else if (keyboardState.isReverse())
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


        body.applyLinearImpulse(impulse.mul(currentMaterial.getTraction()), body.getWorldCenter());
    }

    public Body getBody() {
        return body;
    }

}
