package neonracer.render.engine.collider;

import neonracer.model.track.Track;
import neonracer.render.engine.Spline2D;
import org.joml.Vector2f;

public class TrackCollider implements ICollider<Track> {

    private static final int TRACK_COLLIDER_SAMPLES = 100;

    private Track track;

    @Override
    public void initialize(Track track) {
        this.track = track;
    }

    @Override
    public boolean collides(Vector2f vector2f) {
        Spline2D spline = track.getTrackDef().getSpline2D();

        float sampleRate = 1.0f / TRACK_COLLIDER_SAMPLES;

        // Find closest T
        float closestDistance = Float.MAX_VALUE;
        float closestT = 0.0f;
        for (float t = 0; t <= 1.0f; t += sampleRate) {
            Vector2f vec = spline.interpolate(t);
            float dist = vec.distanceSquared(vector2f);
            if (dist < closestDistance) {
                closestDistance = dist;
                closestT = t;
            }
        }

        // Check if closest point is within spline width
        Spline2D.Segment segment = spline.getSegment(closestT);

        float widthFrom = track.getPath().get(segment.getIndex()).getTrackWidth();
        float widthTo = track.getPath().get(segment.getIndex() == spline.pointsSize() - 1 ? 0 : segment.getIndex() + 1).getTrackWidth();
        float width = widthFrom * (1 - segment.getT()) + widthTo * segment.getT();

        return closestDistance <= width;
    }
}
