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
public class Node implements IData {

    private Vector2f position;

    private float trackWidth;

    private String materialId;

    private Material material;

    @JsonCreator
    public Node(@JsonProperty("x") int x, @JsonProperty("y") int y, @JsonProperty("w") float trackWidth, @JsonProperty("mat") String materialId) {
        this.position = new Vector2f(x, y);
        this.trackWidth = trackWidth;
        this.materialId = materialId;
    }

    @Override
    public void initialize(GameContext context) {
        material = context.getDataManager().getMaterial(materialId);
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getTrackWidth() {
        return trackWidth;
    }

    public Material getMaterial() {
        return material;
    }

}
