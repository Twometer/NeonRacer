package neonracer.model.track;

import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.core.GameContext;
import neonracer.render.gl.core.Texture;
import neonracer.resource.IAsset;

/**
 * Track materials. Can be things like dirt,
 * street or grass etc.
 */
public class Material implements IAsset {

    @JsonProperty("id")
    private String id;

    @JsonProperty("texture")
    private String texturePath;

    private Texture texture;

    @JsonProperty
    private float traction;

    @JsonProperty
    private float drag;

    @Override
    public void initialize(GameContext context) {
        texture = context.getTextureProvider().getTexture(texturePath);
    }

    public String getId() {
        return id;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getTraction() {
        return traction;
    }

    public float getDrag() {
        return drag;
    }
}
