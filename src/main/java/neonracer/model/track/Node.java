package neonracer.model.track;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.core.GameContext;
import neonracer.resource.IData;
import org.joml.Vector2f;

/**
 * One node in the spline curve that
 * defines the race track.
 */
public class Node {

    private Vector2f position;

    private float trackWidth;

    @JsonCreator
    public Node(@JsonProperty("x") int x, @JsonProperty("y") int y, @JsonProperty("w") float trackWidth) {
        this.position = new Vector2f(x, y);
        this.trackWidth = trackWidth;
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getTrackWidth() {
        return trackWidth;
    }

    public void setTrackWidth(float trackWidth) {
        this.trackWidth = trackWidth;
    }

}
