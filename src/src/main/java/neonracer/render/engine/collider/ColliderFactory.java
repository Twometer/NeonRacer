package neonracer.render.engine.collider;

import neonracer.model.track.Track;

public class ColliderFactory {

    public static ICollider<Track, TrackColliderResult> trackCollider(Track track) {
        TrackCollider collider = new TrackCollider();
        collider.initialize(track);
        return collider;
    }

}
