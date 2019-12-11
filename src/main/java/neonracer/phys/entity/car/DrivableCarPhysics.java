package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.gui.input.KeyboardState;
import neonracer.phys.entity.car.body.CarBody;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import java.util.List;

public class DrivableCarPhysics extends AbstractCarPhysics {

    public boolean braking = false;
    public boolean driving = false;

    private float forwardBrakeBoundary;
    private float reverseBrakeBoundary;

    DrivableCarPhysics(GameContext gameContext, CarBody carBody, List<Tire> tires, RevoluteJoint flJoint, RevoluteJoint frJoint, float dragCoefficient) {
        super(gameContext, carBody, tires, flJoint, frJoint, dragCoefficient);
        forwardBrakeBoundary = tires.get(0).getForwardForce() / gameContext.getTimer().getTicksPerSecond();
        reverseBrakeBoundary = tires.get(0).getReverseForce() / gameContext.getTimer().getTicksPerSecond();
    }

    @Override
    public void update() {
        KeyboardState keyboardState = gameContext.getKeyboardState();       //Getting keystrokes
        braking = isBreaking(keyboardState);                                //Determining braking state
        driving = keyboardState.isForward() || keyboardState.isReverse();   //Determining driving state
        super.update(driving, braking);                                     //Applying drag and stabilization
        for (Tire tire : tires) {
            tire.update(keyboardState, braking);
        }

        float lockAngle = (float) Math.toRadians(35);           //possible variables?
        float turnSpeed = (float) Math.toRadians(320);          //from lock to lock in 0.25 sec
        float turnTorque = 1000;
        float desiredAngle = 0;

        if (keyboardState.isLeft())
            desiredAngle = lockAngle;
        else if (keyboardState.isRight())
            desiredAngle = -lockAngle;

        float angleNow = flJoint.getJointAngle();
        float angleToTurn = desiredAngle - angleNow;

        if (Math.abs(angleToTurn) > 1E-2) {

            flJoint.setMaxMotorTorque(turnTorque);
            frJoint.setMaxMotorTorque(turnTorque);
            flJoint.setMotorSpeed(turnSpeed*Math.abs(angleToTurn));
            frJoint.setMotorSpeed(turnSpeed*Math.abs(angleToTurn));

            if (Math.abs(angleToTurn) < 1E-4) {
                flJoint.enableMotor(false);
                frJoint.enableMotor(false);
            } else {
                flJoint.enableMotor(true);
                frJoint.enableMotor(true);
            }

        } else {
            flJoint.enableMotor(false);
            frJoint.enableMotor(false);
        }
    }

    private boolean isBreaking(KeyboardState kbs) {
        return ((kbs.isForward() && relativeVelocity.y < forwardBrakeBoundary) || (kbs.isReverse() && (relativeVelocity.y > reverseBrakeBoundary)));
    }

    public float getForwardBrakeBoundary() {
        return forwardBrakeBoundary;
    }
}
