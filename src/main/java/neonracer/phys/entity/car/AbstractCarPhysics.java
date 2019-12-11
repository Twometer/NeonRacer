package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.phys.Box2dHelper;
import neonracer.phys.entity.EntityPhysics;
import neonracer.phys.entity.car.body.CarBody;
import neonracer.util.MathHelper;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.joml.Vector2f;

import java.util.List;

public abstract class AbstractCarPhysics implements EntityPhysics {

    private final float dragCoefficient;
    protected GameContext gameContext;
    List<Tire> tires;
    RevoluteJoint flJoint;
    RevoluteJoint frJoint;
    Vec2 relativeVelocity;
    private Vec2 currentDrag = new Vec2(0, 0);
    private Body body;

    AbstractCarPhysics(GameContext gameContext, CarBody carBody, List<Tire> tires, RevoluteJoint flJoint, RevoluteJoint frJoint, float dragCoefficient) {
        this.gameContext = gameContext;
        this.body = carBody.getBody();
        this.tires = tires;
        this.flJoint = flJoint;
        this.frJoint = frJoint;
        this.dragCoefficient = dragCoefficient;
    }

    @Override
    public Vector2f getPosition() {
        return Box2dHelper.toVector2f(body.getPosition());
    }

    @Override
    public float getRotation() {
        return body.getAngle();
    }

    @Override
    abstract public void update();

    public void update(boolean driving, boolean braking) {
        if (!driving && (body.getLinearVelocity().length() < (tires.get(0).getCurrentRelativeFriction().length() / gameContext.getTimer().getTicksPerSecond()))) {  //Killing small velocities when not driving
            body.setLinearVelocity(MathHelper.nullVector);      //Killing linear velocity
            body.setAngularVelocity(0);                         //Killing angular velocity
            for (Tire tire : tires) {
                tire.killVelocity();                            //killing the velocities of all tires
            }

        }else {
            relativeVelocity = MathHelper.rotateVec2(getVelocity(), -body.getAngle());              //Updating velocity relative to car rotation(for debugging? possible TODO move to getter method?)
            currentDrag = getVelocity().mul(dragCoefficient * getVelocity().length()).negate();     //Calculating air drag force on the car
            body.applyForce(currentDrag, body.getWorldCenter());                                    //Applying drag force
        }
    }

    public Vec2 getVelocity() {
        Vec2 vel = body.getLinearVelocity();
        if (vel == null)
            return MathHelper.nullVector;
        return vel;
    }

    public Vec2 getCurrentDrag() {
        return currentDrag;
    }

    public List<Tire> getTires() {
        return tires;
    }
}
