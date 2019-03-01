package neonracer.model.track;

import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.core.GameContext;
import neonracer.render.gl.core.Texture;
import neonracer.resource.IData;

/**
 * Track materials. Can be things like dirt,
 * street or grass etc.
 */
public class Material implements IData {

    @JsonProperty("id")
    private String id;

    @JsonProperty("texture")
    private String texturePath;

    private Texture texture;

    @JsonProperty
    private int grip;

    @Override
    public void initialize(GameContext context) {
        texture = context.getTextureManager().getTexture(texturePath);
    }

    public String getId() {
        return id;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getGrip() {
        return grip;
    }
}
