package neonracer.gui.util;

import neonracer.gui.annotation.ParserMethod;
import org.joml.Vector4f;

import java.util.Arrays;

public class Color {

    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f);
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f);
    public static final Color RED = new Color(1.0f, 0.0f, 0.0f);
    public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f);
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f);

    private float r;

    private float g;

    private float b;

    private Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @ParserMethod
    public static Color fromString(String str) {
        double[] f = Arrays.stream(str.split(",")).mapToDouble(Double::parseDouble).toArray();
        if (f.length != 3)
            throw new IllegalArgumentException(str + " is not a valid Color string");
        return new Color((float) f[0], (float) f[1], (float) f[2]);
    }

    /**
     * Returns the red value of this color in an OpenGL-friendly way
     *
     * @return The red value in value range 0..1
     */
    public float getR() {
        return r > 1.0f ? r / 255.0f : r;
    }

    /**
     * Returns the green value of this color in an OpenGL-friendly way
     *
     * @return The green value in value range 0..1
     */
    public float getG() {
        return g > 1.0f ? g / 255.0f : g;
    }

    /**
     * Returns the blue value of this color in an OpenGL-friendly way
     *
     * @return The blue value in value range 0..1
     */
    public float getB() {
        return b > 1.0f ? b / 255.0f : b;
    }

    /**
     * Converts this color to a Vector4f object using the given alpha value.
     *
     * @return A Vector4f object with x=r y=g z=b and w=alpha
     */
    public Vector4f toVector(float alpha) {
        return new Vector4f(getR(), getG(), getB(), alpha);
    }

}
