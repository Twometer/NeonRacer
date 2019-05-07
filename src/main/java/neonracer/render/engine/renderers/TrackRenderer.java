package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import neonracer.render.engine.mesh.MeshBuilder;
import neonracer.render.engine.mesh.Rectangle;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.core.Texture;
import neonracer.render.gl.shaders.FlatShader;
import neonracer.render.gl.shaders.WorldShader;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class TrackRenderer implements IRenderer {

    private FlatShader flatShader;

    private WorldShader worldShader;

    private Model backgroundModel;

    @Override
    public void setup(RenderContext renderContext, GameContext gameContext) {
        worldShader = new WorldShader();
        flatShader = new FlatShader();

        MeshBuilder builder = new MeshBuilder(6);
        Rectangle rectangle = new Rectangle(-1000, -1000, 1000, 1000);
        builder.putRectVertices(rectangle);
        builder.putRectTexCoords(-1000, -1000, 1000, 1000);
        backgroundModel = Model.create(builder.getMesh(), GL_TRIANGLES);
        builder.destroy();
    }

    @Override
    public void render(RenderContext renderContext, GameContext gameContext, RenderPass renderPass) {
        if (renderPass != RenderPass.COLOR) return;
        if (gameContext.getGameState().getCurrentTrack() == null) return;
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
    }

    @Override
    public void destroy(RenderContext renderContext, GameContext gameContext) {
        worldShader.destroy();
        flatShader.destroy();
        backgroundModel.destroy();
    }
}
