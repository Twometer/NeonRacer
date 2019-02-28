package neonracer.model.track;

import org.joml.Vector2f;

/**
 * One node in the spline curve that
 * defines the race track.
 */
public class Node {

    private Vector2f position;

    private int trackWidth;

    private Material material;

    public Vector2f getPosition() {
        return position;
    }

    public int getTrackWidth() {
        return trackWidth;
    }

    public Material getMaterial() {
        return material;
    }

}
