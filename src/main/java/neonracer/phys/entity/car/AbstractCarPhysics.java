package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.phys.Box2dHelper;
import neonracer.phys.entity.EntityPhysics;
import neonracer.phys.entity.car.body.CarBody;
import neonracer.util.MathHelper;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.joml.Vector2f;
import org.jbox2d.common.Vec2;

import java.util.List;

public abstract class AbstractCarPhysics implements EntityPhysics {

    protected GameContext gameContext;
    List<Tire> tires;
    RevoluteJoint flJoint;
    RevoluteJoint frJoint;
    CarBody carBody;
    private Body body;

    AbstractCarPhysics(GameContext gameContext, CarBody carBody, List<Tire> tires, RevoluteJoint flJoint, RevoluteJoint frJoint) {
        this.gameContext = gameContext;
        this.carBody = carBody;
        this.body = carBody.getBody();
        this.tires = tires;
        this.flJoint = flJoint;
        this.frJoint = frJoint;
    }

    @Override
    public Vector2f getPosition() {
        return Box2dHelper.toVector2f(body.getPosition());
    }

    @Override
    public float getRotation() {
        return body.getAngle();
    }

    public Vec2 getVelocity(){
        Vec2 vel = body.getLinearVelocity();
        if(vel == null)
            return MathHelper.nullVector;
        return vel;
    }

    public List<Tire> getTires(){return tires;}

    public CarBody getCarBody() { return carBody; }
}
