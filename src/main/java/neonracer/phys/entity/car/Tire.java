package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.gui.input.KeyboardState;
import neonracer.model.track.Material;
import neonracer.phys.Box2dHelper;
import neonracer.render.engine.collider.TrackColliderResult;
import neonracer.util.MathHelper;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

public class Tire {

    // Bits ported from http://www.iforce2d.net/src/iforce2d_TopdownCar.h

    private GameContext gameContext;

    private float rollCoefficient = -0.75f;
    private float tractionCoefficient = -10f;
    private float forwardForce = 20;
    private float reverseForce = -10;
    private Material currentMaterial;
    private Body body;

    private Vec2 velocity = new Vec2(0,0);
    private Vec2 relativeVelocity = new Vec2(0,0);
    private Vec2 currentFriction = new Vec2(0,0);
    private Vec2 currentRelativeFriction = new Vec2(0,0);
    private Vec2 currentDrive = new Vec2(0,0);

    public Tire(GameContext gameContext, World world, float rollCoefficient, float tractionCoefficient, float forwardForce, float reverseForce) {
        this.gameContext = gameContext;
        this.rollCoefficient = rollCoefficient;
        this.tractionCoefficient = tractionCoefficient;
        this.forwardForce = forwardForce;
        this.reverseForce = reverseForce;

        BodyDef def = new BodyDef();
        def.type = BodyType.DYNAMIC;
        body = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.09f, 0.18f);
        body.createFixture(shape, 1);
        body.setUserData(this);
    }

    void updateFriction( boolean breaking)
    {
        // Load traction here
        Vector2f vec = Box2dHelper.toVector2f(body.getPosition());
        TrackColliderResult colliderResult = gameContext.getGameState().getCurrentTrack().getCollider().collides(vec);

        if (colliderResult.isCollided()) currentMaterial = colliderResult.getCurrentMaterial();
        else currentMaterial = gameContext.getGameState().getCurrentTrack().getBackgroundMaterial();

        velocity = body.getLinearVelocity();

        relativeVelocity = MathHelper.rotateVec2(this.velocity, -body.getAngle());
        currentRelativeFriction.x = Math.signum(relativeVelocity.x) * tractionCoefficient * getMat();
        if(breaking)
            currentRelativeFriction.y = Math.signum(relativeVelocity.y) * tractionCoefficient * getMat();
        else
            currentRelativeFriction.y = Math.signum(relativeVelocity.y) * rollCoefficient * getMat();
        currentFriction = MathHelper.rotateVec2(currentRelativeFriction, body.getAngle());
        body.applyForce(currentFriction, body.getWorldCenter());
    }

    void updateDrive(KeyboardState keyboardState,boolean breaking) {
        if(breaking)
            return;
        float currentForce = 0;
        if (keyboardState.isForward())
            currentForce = forwardForce;
        else if (keyboardState.isReverse())
            currentForce = reverseForce;
        updateDrive(currentForce);
    }

    void updateDrive(float currentForce) {
        currentDrive = MathHelper.angleToUnitVec2(body.getAngle()).mul(currentForce);
        body.applyForce(currentDrive, body.getWorldCenter());
    }

    public Body getBody() {
        return body;
    }

    public Vec2 getCurrentDrive() {
        return currentDrive;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public Vec2 getRelativeVelocity() {
        return relativeVelocity;
    }

    public Vec2 getCurrentRelativeFriction() {
        return currentRelativeFriction;
    }

    public float getMat()
    {
        if(currentMaterial == null)
            return 0;
        return currentMaterial.getTraction();
    }

}
