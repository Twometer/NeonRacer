package neonracer.phys.entity.car.body;

import neonracer.phys.entity.car.Tire;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import java.util.List;

public class CarBody {

    private Body body;

    private Tire[] tires;

    private RevoluteJoint leftJoint;

    private RevoluteJoint rightJoint;

    CarBody(Body body, Tire[] tires, RevoluteJoint leftJoint, RevoluteJoint rightJoint) {
        this.body = body;
        this.tires = tires;
        this.leftJoint = leftJoint;
        this.rightJoint = rightJoint;
    }

    public Body getBody() {
        return body;
    }

    public Tire[] getTires() {
        return tires;
    }

    public RevoluteJoint getLeftJoint() {
        return leftJoint;
    }

    public RevoluteJoint getRightJoint() {
        return rightJoint;
    }
}
