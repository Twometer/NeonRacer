package neonracer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.render.gl.core.Texture;

public class EntityItem extends Entity {

    @JsonCreator
    public EntityItem(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation) {
        super(type, x, y, rotation);
    }

    @Override
    public Texture getColorTexture() {
        return null;
    }

    @Override
    public Texture getGlowTexture() {
        return null;
    }
}
