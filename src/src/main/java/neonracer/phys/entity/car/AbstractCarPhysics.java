package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.phys.Box2dHelper;
import neonracer.phys.entity.EntityPhysics;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.joml.Vector2f;

import java.util.List;

public abstract class AbstractCarPhysics implements EntityPhysics {

    protected GameContext gameContext;
    List<Tire> tires;
    RevoluteJoint flJoint;
    RevoluteJoint frJoint;
    private Body body;

    AbstractCarPhysics(GameContext gameContext, Body body, List<Tire> tires, RevoluteJoint flJoint, RevoluteJoint frJoint) {
        this.gameContext = gameContext;
        this.body = body;
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


}
