package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.gui.input.KeyboardState;
import neonracer.phys.entity.car.body.CarBody;
import neonracer.util.MathHelper;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import java.util.List;

public class DriveableCarPhysics extends AbstractCarPhysics {

    DriveableCarPhysics(GameContext gameContext, CarBody carBody, List<Tire> tires, RevoluteJoint flJoint, RevoluteJoint frJoint) {
        super(gameContext, carBody, tires, flJoint, frJoint);
    }

    @Override
    public void update() {
        KeyboardState keyboardState = gameContext.getKeyboardState();
        for (Tire tire : tires) {
            tire.updateFriction(carBody.getVelocity());
            tire.updateDrive(keyboardState);
        }
        carBody.updateAirResistance();
        float lockAngle = (float) Math.toRadians(35);
        float turnSpeedPerSec = (float) Math.toRadians(320);//from lock to lock in 0.25 sec
        float turnPerTimeStep = turnSpeedPerSec / 60.0f;
        float desiredAngle = 0;
        if (keyboardState.isLeft())
            desiredAngle = lockAngle;
        else if (keyboardState.isRight())
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
