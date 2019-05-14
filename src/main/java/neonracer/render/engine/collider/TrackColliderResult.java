package neonracer.render.engine.collider;

import neonracer.model.track.Material;

public class TrackColliderResult extends ColliderResult {

    private float closestT;

    private Material currentMaterial;

    TrackColliderResult(boolean collided, float closestT, Material currentMaterial) {
        super(collided);
        this.closestT = closestT;
        this.currentMaterial = currentMaterial;
    }

    public float getClosestT() {
        return closestT;
    }

    public Material getCurrentMaterial() {
        return currentMaterial;
    }

}
