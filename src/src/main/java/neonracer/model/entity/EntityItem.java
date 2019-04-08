package neonracer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.core.GameContext;
import neonracer.phys.PhysEntity;
import neonracer.render.gl.core.Texture;

public class EntityItem extends PhysEntity {

    @JsonCreator
    public EntityItem(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation) {
        super(type, x, y, rotation);
    }

    public EntityItem(String type, float x, float y, float rotation, GameContext gameContext) {
        super(type, x, y, rotation, gameContext);
    }

    @Override
    public Texture getColorTexture() {
        return null;
    }

    @Override
    public Texture getGlowTexture() {
        return null;
    }

    public float getEntityWidth() { return 0f; }

    public float getEntityLength() { return 0f; }
}
