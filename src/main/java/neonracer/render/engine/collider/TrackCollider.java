package neonracer.render.engine.collider;

import neonracer.model.track.Node;
import neonracer.model.track.Track;
import neonracer.render.engine.Spline2D;
import org.joml.Vector2f;

public class TrackCollider implements ICollider<Track, TrackColliderResult> {

    private Track track;

    @Override
    public void initialize(Track track) {
        this.track = track;
    }

    @Override
    public TrackColliderResult collides(Vector2f vector2f) {
        Spline2D spline = track.getTrackDef().getSpline2D();

        float sampleRate = 1.0f / track.getSamples();

        // Find closest T
        float closestDistanceSquared = Float.MAX_VALUE;
        float closestT = 0.0f;
        for (float t = 0; t <= 1.0f; t += sampleRate) {
            Vector2f vec = spline.interpolate(t);
            float dist = vec.distanceSquared(vector2f);
            if (dist < closestDistanceSquared) {
                closestDistanceSquared = dist;
                closestT = t;
            }
        }

        // Check if closest point is within spline width
        Spline2D.Segment segment = spline.getSegment(closestT);

        Node node = track.getPath().get(segment.getIndex());
        float widthFrom = node.getTrackWidth();
        float widthTo = track.getPath().get(segment.getIndex() == spline.pointsSize() - 1 ? 0 : segment.getIndex() + 1).getTrackWidth();
        float width = widthFrom * (1 - segment.getT()) + widthTo * segment.getT();
        float radius = width / 2;

        float closestDistance = (float) Math.sqrt(closestDistanceSquared);
        return new TrackColliderResult(closestDistance <= radius, node.getMaterial());
    }
}
