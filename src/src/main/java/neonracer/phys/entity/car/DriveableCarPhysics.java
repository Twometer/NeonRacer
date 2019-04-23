package neonracer.phys.entity.car;

import neonracer.core.ControlState;
import neonracer.core.GameContext;
import neonracer.util.MathHelper;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import java.util.List;

public class DriveableCarPhysics extends AbstractCarPhysics {

    DriveableCarPhysics(GameContext gameContext, Body body, List<Tire> tires, RevoluteJoint flJoint, RevoluteJoint frJoint) {
        super(gameContext, body, tires, flJoint, frJoint);
    }

    @Override
    public void update() {
        ControlState controlState = gameContext.getControlState();
        for (Tire tire : tires) {
            tire.updateFriction();
            tire.updateDrive(controlState);
        }
        float lockAngle = (float) Math.toRadians(35);
        float turnSpeedPerSec = (float) Math.toRadians(320);//from lock to lock in 0.25 sec
        float turnPerTimeStep = turnSpeedPerSec / 60.0f;
        float desiredAngle = 0;
        if (controlState.isLeft())
            desiredAngle = lockAngle;
        else if (controlState.isRight())
            desiredAngle = -lockAngle;
        float angleNow = flJoint.getJointAngle();
        float angleToTurn = desiredAngle - angleNow;
        angleToTurn = MathHelper.clamp(angleToTurn, -turnPerTimeStep, turnPerTimeStep);

        if (Math.abs(angleToTurn) < 1E-4)
            return;

        float newAngle = angleNow + angleToTurn;
        flJoint.setLimits(newAngle, newAngle);
        frJoint.setLimits(newAngle, newAngle);
    }
}