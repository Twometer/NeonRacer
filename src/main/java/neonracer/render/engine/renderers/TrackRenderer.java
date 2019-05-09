package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.model.track.Track;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import neonracer.render.engine.Spline2D;
import neonracer.render.engine.mesh.MeshBuilder;
import neonracer.render.engine.mesh.Rectangle;
import neonracer.render.gl.core.Mesh;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.core.Texture;
import neonracer.render.gl.shaders.FlatShader;
import neonracer.render.gl.shaders.WorldShader;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class TrackRenderer implements IRenderer {

    private FlatShader flatShader;

    private WorldShader worldShader;

    private Model backgroundModel;

    @Override
    public void setup(RenderContext renderContext, GameContext gameContext) {
        worldShader = renderContext.getShader(WorldShader.class);
        flatShader = renderContext.getShader(FlatShader.class);

        MeshBuilder builder = new MeshBuilder(6);
        Rectangle rectangle = new Rectangle(-1000, -1000, 1000, 1000);
        builder.putRectVertices(rectangle);
        builder.putRectTexCoords(-1000, -1000, 1000, 1000);
        backgroundModel = Model.create(builder.getMesh(), GL_TRIANGLES);
        builder.destroy();
    }

    @Override
    public void render(RenderContext renderContext, GameContext gameContext, RenderPass renderPass) {
        if (gameContext.getGameState().getCurrentTrack() == null) return;

        if (renderPass == RenderPass.COLOR) {
            flatShader.bind();
            flatShader.setProjectionMatrix(renderContext.getWorldMatrix());
            flatShader.setTransformationMatrix(new Matrix4f());
            flatShader.setHasTexture(true);
            Texture texture = gameContext.getGameState().getCurrentTrack().getBackgroundMaterial().getTexture();
            glActiveTexture(GL_TEXTURE0);
            texture.bind();
            backgroundModel.draw();
            texture.unbind();
            flatShader.unbind();

            worldShader.bind();
            worldShader.setProjectionMatrix(renderContext.getWorldMatrix());
            texture = gameContext.getGameState().getCurrentTrack().getForegroundMaterial().getTexture();
            glActiveTexture(GL_TEXTURE0);
            texture.bind();
            gameContext.getGameState().getCurrentTrack().getTrackDef().getModel().draw();
            texture.unbind();
            worldShader.unbind();

            renderFinishLine(renderContext, gameContext);
        } else renderFinishLine(renderContext, gameContext);

    }

    private void renderFinishLine(RenderContext renderContext, GameContext gameContext) {
        flatShader.bind();
        flatShader.setProjectionMatrix(renderContext.getWorldMatrix());
        flatShader.setTransformationMatrix(new Matrix4f());
        flatShader.setHasTexture(false);
        flatShader.setColor(1.0f, 0.0f, 1.0f, 1.0f);

        Track track = gameContext.getGameState().getCurrentTrack();
        Mesh mesh = new Mesh(4);
        putVertices(track, mesh, 0.0f);
        putVertices(track, mesh, 0.01f);
        Model model = Model.create(mesh, GL_TRIANGLE_STRIP);
        mesh.destroy();
        model.draw();

        flatShader.unbind();
    }

    private void putVertices(Track track, Mesh mesh, float t) {
        Spline2D spline = track.getTrackDef().getSpline2D();
        Vector2f vec = spline.interpolate(t);
        Vector2f n = spline.getNormal(t).normalize(0.5f * track.getPath().get(0).getTrackWidth());
        mesh.putVertex(vec.x + n.x, vec.y + n.y);
        mesh.putVertex(vec.x - n.x, vec.y - n.y);
    }

    @Override
    public void destroy(RenderContext renderContext, GameContext gameContext) {
        worldShader.destroy();
        flatShader.destroy();
        backgroundModel.destroy();
    }
}
