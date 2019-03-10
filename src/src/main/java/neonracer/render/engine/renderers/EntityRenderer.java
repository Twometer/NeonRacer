package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.model.entity.Entity;
import neonracer.render.RenderContext;
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
    public void setup(GameContext context) {
        entityShader = new EntityShader();

        MeshBuilder meshBuilder = new MeshBuilder(6);
        meshBuilder.putRectVertices(new Rectangle(0, 0, 1, 1));
        meshBuilder.putRectTexCoords(0, 0, 1, 1);
        rectangle = Model.create(meshBuilder.getMesh(), GL_TRIANGLES);
        meshBuilder.destroy();
    }

    @Override
    public void render(RenderContext renderContext, GameContext gameContext) {
        List<Entity> entities = gameContext.getGameState().getEntities();
        entityShader.bind();
        entityShader.setProjectionMatrix(renderContext.getWorldMatrix());
        for (Entity entity : entities) {
            Texture texture = entity.getColorTexture();
            texture.bind();
            float width = texture.getWidth() / 150.0f;
            float height = texture.getHeight() / 150.0f;
            Matrix4f transformationMatrix = new Matrix4f();
            transformationMatrix.translate(entity.getPosition().x(), entity.getPosition().y, 0.0f);
            transformationMatrix.rotate((float) Math.toRadians(entity.getRotation()), 0.0f, 0.0f, 1.0f);
            transformationMatrix.translate(-width / 2, -height / 2, 0.0f);
            transformationMatrix.scale(width, height, 1.0f);
            entityShader.setTransformationMatrix(transformationMatrix);
            rectangle.draw();
            texture.unbind();
        }
        entityShader.unbind();
    }

    @Override
    public void destroy(GameContext context) {
        rectangle.destroy();
        entityShader.destroy();
    }

}
