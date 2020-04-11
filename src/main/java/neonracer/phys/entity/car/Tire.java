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

    public static final int FRONT_LEFT = 0;
    public static final int FRONT_RIGHT = 1;
    public static final int BACK_LEFT = 2;
    public static final int BACK_RIGHT = 3;

    private final float rollCoefficient; // = -0.75f;
    private final float tractionCoefficient; // = -10f;
    private final float forwardForce; // = 20;
    private final float reverseForce; // = -10;
    private GameContext gameContext;
    private Material currentMaterial;
    private Body body;

    private Vec2 velocity = new Vec2(0, 0);                 //velocity of tire relative to ground, always equal to body velocity, used only for calculations, not set here
    private Vec2 relativeVelocity = new Vec2(0, 0);         //velocity of tire relative to car
    private Vec2 currentFriction = new Vec2(0, 0);          //friction force to be calculated and applied to tire body in update()
    private Vec2 currentRelativeFriction = new Vec2(0, 0);  //friction force relative to angle of tire to be calculated in update() depending on material
    private Vec2 currentForce = new Vec2(0, 0);             //drive or brake force relative to ground to be calculated and applied to tire body in update() depending on driving condition

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

    public void update(KeyboardState keyboardState, boolean braking) {
        velocity = body.getLinearVelocity();                                                                            //updating Velocity
        relativeVelocity = MathHelper.rotateVec2(this.velocity, -body.getAngle());                                      //Updating relative velocity (for debugging? possible TODO move to getter method?)

        Vector2f vec = Box2dHelper.toVector2f(body.getPosition());                                                      //Loading road material
        TrackColliderResult colliderResult = gameContext.getGameState().getCurrentTrack().getCollider().collides(vec);

        if (colliderResult.isCollided())
            currentMaterial = colliderResult.getCurrentMaterial();                                                      //Evaluating material
        else
            currentMaterial = gameContext.getGameState().getCurrentTrack().getBackgroundMaterial();                     //Setting background material as default

        currentRelativeFriction.x = Math.signum(relativeVelocity.x) * tractionCoefficient * getMaterialTraction();      //Calculating friction

        if (braking)                                                                                                    //Switching from rolling friction to sliding friction if brakes engaged
            currentRelativeFriction.y = Math.signum(relativeVelocity.y) * tractionCoefficient * getMaterialTraction();
        else
            currentRelativeFriction.y = Math.signum(relativeVelocity.y) * rollCoefficient * getMaterialTraction();

        currentFriction = MathHelper.rotateVec2(currentRelativeFriction, body.getAngle());                              //Rotating friction to tire direction

        if ((body.getLinearVelocity().length() > (2 * currentFriction.length() / gameContext.getTimer().getTicksPerSecond())) || braking)
            body.applyForce(currentFriction, body.getWorldCenter());
        else if (keyboardState != null) {
            if (!(keyboardState.isForward() || keyboardState.isReverse())) {
                killVelocity();
            }
        }

        float currentForceValue = 0;

        //calculating and applying drive
        if (braking || (keyboardState == null))
            return;
        if (keyboardState.isForward())
            currentForceValue = forwardForce;
        else if (keyboardState.isReverse())
            currentForceValue = reverseForce;

        currentForce = MathHelper.angleToUnitVec2(body.getAngle()).mul(currentForceValue).mul(getMaterialTraction());
        body.applyForce(currentForce, body.getWorldCenter());
    }

    public void killVelocity() {
        body.setAngularVelocity(0);
        body.setLinearVelocity(MathHelper.nullVector);
    }

    public Body getBody() {
        return body;
    }

    public float getForwardForce() {
        return forwardForce;
    }

    public float getReverseForce() {
        return reverseForce;
    }

    public Vec2 getCurrentForce() {
        return currentForce;
    }

    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
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
