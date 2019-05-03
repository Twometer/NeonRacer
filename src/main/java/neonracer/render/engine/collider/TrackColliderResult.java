package neonracer.render.engine.collider;

import neonracer.model.track.Material;

public class TrackColliderResult extends ColliderResult {

    private Material currentMaterial;

    TrackColliderResult(boolean collided, Material currentMaterial) {
        super(collided);
        this.currentMaterial = currentMaterial;
    }

    public Material getCurrentMaterial() {
        return currentMaterial;
    }
}
