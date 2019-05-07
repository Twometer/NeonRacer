package neonracer.gui.util;

public class Animator {

    private final float increment;

    private final float minimum;

    private final float maximum;

    private float value;

    public Animator(float increment, float minimum, float maximum) {
        this.increment = increment;
        this.minimum = minimum;
        this.maximum = maximum;
        this.value = minimum;
    }

    public void update(boolean increment) {
        if (increment && value < maximum)
            value += this.increment;
        else if (!increment && value > minimum)
            value -= this.increment;
    }

    public float getValue() {
        return value;
    }
}
