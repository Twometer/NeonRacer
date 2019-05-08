package neonracer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.phys.entity.EntityPhysics;
import neonracer.render.gl.core.Texture;

import java.util.Map;

public class EntityItem extends Entity {

    @JsonCreator
    public EntityItem(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation, @JsonProperty("params") Map<String, String> params) {
        super(type, x, y, rotation, params);
    }

    @Override
    public Texture getColorTexture() {
        return null;
    }

    @Override
    public Texture getGlowTexture() {
        return null;
    }

    @Override
    public EntityPhysics getPhysics() {
        return null;
    }

}
