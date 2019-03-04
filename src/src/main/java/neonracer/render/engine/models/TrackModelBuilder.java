package neonracer.render.engine.models;

import neonracer.model.track.Node;
import neonracer.model.track.Track;
import neonracer.render.engine.Spline2D;
import neonracer.render.gl.core.Mesh;
import neonracer.render.gl.core.Model;
import org.joml.Vector2f;

import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;

public class TrackModelBuilder implements IModelBuilder<Track> {

    private static final int TRACK_SAMPLES = 100;

    @Override
    public Model build(Track track) {
        List<Vector2f> points = track.getPath().stream().map(Node::getPosition).collect(Collectors.toList());
        Spline2D spline = new Spline2D(points, true, 0.8f);

        Mesh mesh = new Mesh(TRACK_SAMPLES * 2 + 2);

        float sampleRate = 1 / (float) 1 / TRACK_SAMPLES;
        for (float t = 0; t <= 1.0f; t += sampleRate) {
            Spline2D.Segment segment = spline.getSegment(t);
            float widthFrom = track.getPath().get(segment.getIndex()).getTrackWidth();
            float widthTo = track.getPath().get(segment.getIndex() == points.size() - 1 ? 0 : segment.getIndex() + 1).getTrackWidth();
            float width = widthFrom * (1 - segment.getT()) + widthTo * segment.getT();
            Vector2f vec = spline.interpolate(t);
            Vector2f n = spline.getNormal(t).normalize(0.5f * width);

            mesh.putVertex(vec.x + n.x, vec.y + n.y);
            mesh.putColor(1.0f, 1.0f, 1.0f);

            mesh.putVertex(vec.x - n.x, vec.y - n.y);
            mesh.putColor(1.0f, 1.0f, 1.0f);
        }

        Model model = Model.create(mesh, GL_TRIANGLE_STRIP);
        mesh.destroy();
        return model;
    }
}
