package neonracer.phys.entity.car;

import neonracer.core.GameContext;
import neonracer.gui.input.KeyboardState;
import neonracer.phys.entity.car.body.CarBody;
import neonracer.util.MathHelper;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import java.util.List;

public class DrivableCarPhysics extends AbstractCarPhysics {

    private static final float forwardBrakeBoundary = -0.1f;
    private static final float reverseBrakeBoundary = 0.1f;

    public boolean braking = false;
    public boolean driving = false;

    private float steeringLockAngle;
    private float steeringAngularVelocity;

    DrivableCarPhysics(GameContext gameContext, CarBody carBody, Tire[] tires,
                       RevoluteJoint flJoint, RevoluteJoint frJoint, float dragCoefficient,
                       float steeringLockAngle, float steeringAngularVelocity) {
        super(gameContext, carBody, tires, flJoint, frJoint, dragCoefficient);

        this.steeringLockAngle = steeringLockAngle;
        this.steeringAngularVelocity = steeringAngularVelocity;
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

        float lockAngle = (float) Math.toRadians(steeringLockAngle);
        float turnPerStep = (float) Math.toRadians(steeringAngularVelocity) / gameContext.getTimer().getTicksPerSecond();
        float desiredAngle = 0;

        if (keyboardState.isLeft())
            desiredAngle = lockAngle;
        else if (keyboardState.isRight())
            desiredAngle = -lockAngle;

        float angleNow = flJoint.getJointAngle();
        float angleToTurn = MathHelper.clamp(desiredAngle - angleNow, -turnPerStep, turnPerStep);

        if (Math.abs(angleToTurn) < 1E-4) return;

        float newAngle = angleNow + angleToTurn;
        flJoint.setLimits(newAngle, newAngle);
        frJoint.setLimits(newAngle, newAngle);
    }

    private boolean isBreaking(KeyboardState kbs) {
        return ((kbs.isForward() && relativeVelocity.y < forwardBrakeBoundary) || (kbs.isReverse() && (relativeVelocity.y > reverseBrakeBoundary)));
    }
}
