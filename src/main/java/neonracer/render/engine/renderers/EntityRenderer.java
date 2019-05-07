package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.model.entity.Entity;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import neonracer.render.engine.mesh.MeshBuilder;
import neonracer.render.engine.mesh.Rectangle;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.core.Texture;
import neonracer.render.gl.shaders.EntityShader;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class EntityRenderer implements IRenderer {

    private EntityShader entityShader;

    private Model rectangle;

    @Override
    public void setup(RenderContext renderContext, GameContext gameContext) {
        entityShader = renderContext.getShader(EntityShader.class);

        MeshBuilder meshBuilder = new MeshBuilder(6);
        meshBuilder.putRectVertices(new Rectangle(0, 0, 1, 1));
        meshBuilder.putRectTexCoords(0, 0, 1, 1);
        rectangle = Model.create(meshBuilder.getMesh(), GL_TRIANGLES);
        meshBuilder.destroy();
    }

    @Override
    public void render(RenderContext renderContext, GameContext gameContext, RenderPass renderPass) {
        List<Entity> entities = gameContext.getGameState().getEntities();
        entityShader.bind();
        entityShader.setProjectionMatrix(renderContext.getWorldMatrix());
        for (Entity entity : entities) {
            Texture texture = renderPass == RenderPass.GLOW ? entity.getGlowTexture() : entity.getColorTexture();
            texture.bind();
            float width = entity.getWidth();
            float height = entity.getHeight();
            Matrix4f transformationMatrix = new Matrix4f();
            transformationMatrix.translate(entity.getPosition().x(), entity.getPosition().y, 0.0f);
            transformationMatrix.rotate(entity.getRotation(), 0.0f, 0.0f, 1.0f);
            transformationMatrix.translate(-width / 2, 0, 0.0f);
            transformationMatrix.scale(width, height, 1.0f);
            entityShader.setTransformationMatrix(transformationMatrix);
            rectangle.draw();
            texture.unbind();
        }
        entityShader.unbind();
    }

    @Override
    public void destroy(RenderContext renderContext, GameContext gameContext) {
        rectangle.destroy();
        entityShader.destroy();
    }

}
