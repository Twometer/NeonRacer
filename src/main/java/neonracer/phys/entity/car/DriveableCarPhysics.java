package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.gui.input.KeyboardState;
import neonracer.phys.entity.car.body.CarBody;
import neonracer.util.MathHelper;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import java.util.List;

public class DriveableCarPhysics extends AbstractCarPhysics {

    public boolean breaking = false;
    public boolean driving = false;

    private float steeringLockAngle;
    private float steeringAngularVelocity;

    DriveableCarPhysics(GameContext gameContext, CarBody carBody, List<Tire> tires,
                        RevoluteJoint flJoint, RevoluteJoint frJoint, float dragCoefficient,
                        float steeringLockAngle, float steeringAngularVelocity) {

        super(gameContext, carBody, tires, flJoint, frJoint, dragCoefficient);

        this.steeringLockAngle = steeringLockAngle;
        this.steeringAngularVelocity = steeringAngularVelocity;
    }

    @Override
    public void update() {
        KeyboardState keyboardState = gameContext.getKeyboardState();
        breaking = isBreaking(keyboardState);
        driving = keyboardState.isForward() || keyboardState.isReverse();
        super.update(driving, breaking);
        for (Tire tire : tires) {
            tire.update(keyboardState, breaking);
        }
        float lockAngle = (float) Math.toRadians(steeringLockAngle);
        float turnAnglePerSec = (float) Math.toRadians(steeringAngularVelocity);
        float turnPerTimeStep = turnAnglePerSec / gameContext.getTimer().getTicksPerSecond();
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

    private boolean isBreaking(KeyboardState kbs) {
        return ((kbs.isForward() && (relativeVelocity.y < -0.1)) || (kbs.isReverse() && (relativeVelocity.y > 0.1)));
    }

}
