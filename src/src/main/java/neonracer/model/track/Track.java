package neonracer.model.track;

import com.fasterxml.jackson.annotation.JsonProperty;
import neonracer.core.GameContext;
import neonracer.model.entity.Entity;
import neonracer.render.engine.models.ModelBuilderFactory;
import neonracer.render.engine.models.ModelType;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.core.Texture;
import neonracer.resource.IData;

import java.util.List;

/**
 * Defines a race track using a background material and a
 * path where the track is on.
 */
public class Track implements IData {

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty("desc")
    private String description;

    @JsonProperty("thumbnail")
    private String thumbnailPath;

    private Texture thumbnail;

    @JsonProperty("base_material")
    private String baseMaterialId;

    private Material baseMaterial;

    @JsonProperty
    private List<Node> path;

    @JsonProperty
    private List<Entity> entities;

    private Model model;

    @Override
    public void initialize(GameContext context) {
        thumbnail = context.getTextureProvider().getTexture(thumbnailPath);
        baseMaterial = context.getDataManager().getMaterial(baseMaterialId);
        for (Node node : path)
            node.initialize(context);
        model = ModelBuilderFactory.<Track>getModelBuilder(ModelType.TRACK).build(this);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Texture getThumbnail() {
        return thumbnail;
    }

    public Material getBaseMaterial() {
        return baseMaterial;
    }

    public List<Node> getPath() {
        return path;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Model getModel() {
        return model;
    }
}
