package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import java.util.List;

public class StaticCarPhysics extends AbstractCarPhysics {

    StaticCarPhysics(GameContext gameContext, Body body, List<Tire> tires, RevoluteJoint flJoint, RevoluteJoint frJoint) {
        super(gameContext, body, tires, flJoint, frJoint);
    }

    @Override
    public void update() {
    }

}
