package neonracer.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.core.GameContext;
import neonracer.phys.entity.item.StaticPhysics;
import neonracer.render.gl.core.Texture;

import java.util.Map;

public class EntityStatic extends Entity {

    private String colorTexturePath;

    private Texture colorTexture;

    private String glowTexturePath;

    private Texture glowTexture;

    @JsonCreator
    public EntityStatic(@JsonProperty("type") String type, @JsonProperty("x") float x, @JsonProperty("y") float y, @JsonProperty("r") float rotation, @JsonProperty("params") Map<String, String> params) {
        super(type, x, y, rotation, params);
        colorTexturePath = params.get("color_texture");
        glowTexturePath = params.get("glow_texture");
    }

    @Override
    public void onInitialize(GameContext gameContext) {
        super.onInitialize(gameContext);
        colorTexture = gameContext.getTextureProvider().getTexture(colorTexturePath);
        glowTexture = gameContext.getTextureProvider().getTexture(glowTexturePath);
        setPhysics(StaticPhysics.create(this, gameContext));
    }

    @Override
    public Texture getColorTexture() {
        return colorTexture;
    }

    @Override
    public Texture getGlowTexture() {
        return glowTexture;
    }

}
