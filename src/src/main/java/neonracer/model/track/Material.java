package neonracer.model.track;

import neonracer.render.gl.Texture;

/**
 * Track materials. Can be things like dirt,
 * street or grass etc.
 */
public class Material {

    private String id;

    private Texture texture;

    private int grip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getGrip() {
        return grip;
    }

    public void setGrip(int grip) {
        this.grip = grip;
    }
}
