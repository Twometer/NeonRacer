package neonracer.phys.entity.car.body;

import neonracer.gui.input.KeyboardState;
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

    public float dragCoefficient;

    private Vec2 currentDrag = new Vec2(0,0);
    private Vec2 velocity = new Vec2(0,0);
    private Vec2 relativeVelocity = new Vec2(0,0);

    CarBody(Body body, List<Tire> tires, RevoluteJoint leftJoint, RevoluteJoint rightJoint, float dragCoefficient) {
        this.body = body;
        this.tires = tires;
        this.leftJoint = leftJoint;
        this.rightJoint = rightJoint;
        this.dragCoefficient = dragCoefficient;
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
        velocity = getVelocity();
        currentDrag = velocity.mul(dragCoefficient*velocity.length());
        body.applyForce(currentDrag, body.getWorldCenter());
    }

    public boolean checkBreak(KeyboardState kbs)
    {
        relativeVelocity = MathHelper.rotateVec2(this.velocity, -body.getAngle());
        if((kbs.isForward()&&(relativeVelocity.y<-0.1))||(kbs.isReverse()&&(relativeVelocity.y>0.1)))
            return true;
        return false;

    }

}
