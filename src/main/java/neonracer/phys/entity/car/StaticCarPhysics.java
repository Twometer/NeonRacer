package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.phys.entity.car.body.CarBody;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import java.util.List;

public class StaticCarPhysics extends AbstractCarPhysics {

    StaticCarPhysics(GameContext gameContext, CarBody carBody, List<Tire> tires, RevoluteJoint flJoint, RevoluteJoint frJoint) {
        super(gameContext, carBody, tires, flJoint, frJoint);
    }

    @Override
    public void update() {
        carBody.update();
        for (Tire tire : tires) {
            tire.update(null,false);
        }
    }

}
