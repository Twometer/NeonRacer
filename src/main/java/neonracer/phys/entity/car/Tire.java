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

    private final float rollCoefficient; // = -0.75f;
    private final float tractionCoefficient; // = -10f;
    private final float forwardForce; // = 20;
    private final float reverseForce; // = -10;
    private GameContext gameContext;
    private Material currentMaterial;
    private Body body;

    private Vec2 velocity = new Vec2(0, 0);
    private Vec2 relativeVelocity = new Vec2(0, 0);
    private Vec2 currentFriction = new Vec2(0, 0);
    private Vec2 currentRelativeFriction = new Vec2(0, 0);
    private Vec2 currentDrive = new Vec2(0, 0);

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

    public void update(KeyboardState keyboardState, boolean breaking) {
        velocity = body.getLinearVelocity();                                            //updating Velocity
        relativeVelocity = MathHelper.rotateVec2(this.velocity, -body.getAngle());

        Vector2f vec = Box2dHelper.toVector2f(body.getPosition());                      //loading Material
        TrackColliderResult colliderResult = gameContext.getGameState().getCurrentTrack().getCollider().collides(vec);

        if (colliderResult.isCollided()) currentMaterial = colliderResult.getCurrentMaterial();
        else currentMaterial = gameContext.getGameState().getCurrentTrack().getBackgroundMaterial();

        currentRelativeFriction.x = Math.signum(relativeVelocity.x) * tractionCoefficient * getMaterialTraction(); //calculating and applying friction
        if (breaking)
            currentRelativeFriction.y = Math.signum(relativeVelocity.y) * tractionCoefficient * getMaterialTraction();
        else
            currentRelativeFriction.y = Math.signum(relativeVelocity.y) * rollCoefficient * getMaterialTraction();
        currentFriction = MathHelper.rotateVec2(currentRelativeFriction, body.getAngle());
        if ((body.getLinearVelocity().length() > (2 * currentFriction.length() / gameContext.getTimer().getTicksPerSecond())) || breaking)
            body.applyForce(currentFriction, body.getWorldCenter());
        else if (keyboardState != null) {
            if (!(keyboardState.isForward() || keyboardState.isReverse())) {
                body.setLinearVelocity(MathHelper.nullVector);
                body.setAngularVelocity(0);
            }
        }

        if (breaking || (keyboardState == null))                                         //calculating and applying drive
            return;
        float currentForce = 0;
        if (keyboardState.isForward())
            currentForce = forwardForce;
        else if (keyboardState.isReverse())
            currentForce = reverseForce;

        currentDrive = MathHelper.angleToUnitVec2(body.getAngle()).mul(currentForce).mul(getMaterialTraction());
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

    public float getMaterialTraction() {
        if (currentMaterial == null)
            return 0;
        return currentMaterial.getTraction();
    }

}
