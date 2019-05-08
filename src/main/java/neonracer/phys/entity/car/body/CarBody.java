package neonracer.phys.entity.car.body;

import neonracer.phys.entity.car.Tire;
import neonracer.util.MathHelper;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.common.Vec2;

import java.util.List;

public class CarBody {

    private Body body;

    private List<Tire> tires;

    private RevoluteJoint leftJoint;

    private RevoluteJoint rightJoint;

    private Vec2 currentDrag = new Vec2(0,0);
    private Vec2 velocity = new Vec2(0,0);

    CarBody(Body body, List<Tire> tires, RevoluteJoint leftJoint, RevoluteJoint rightJoint) {
        this.body = body;
        this.tires = tires;
        this.leftJoint = leftJoint;
        this.rightJoint = rightJoint;
    }

    public Body getBody() {
        return body;
    }

    public List<Tire> getTires() {
        return tires;
    }

    public RevoluteJoint getLeftJoint() {
        return leftJoint;
    }

    public RevoluteJoint getRightJoint() {
        return rightJoint;
    }

    public Vec2 getCurrentDrag() { return currentDrag; }

    public Vec2 getVelocity() { return body.getLinearVelocity(); }

    public void updateAirResistance() {
        float airResistance = -0.5f;
        velocity = getVelocity();
        currentDrag = velocity.mul(airResistance*velocity.length());
        body.applyForce(currentDrag, body.getWorldCenter());
    }

}
