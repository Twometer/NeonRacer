package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.model.entity.Entity;
import neonracer.model.entity.EntityStatic;
import neonracer.render.GameWindow;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import neonracer.render.engine.mesh.MeshBuilder;
import neonracer.render.engine.mesh.Rectangle;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.core.Texture;
import neonracer.render.gl.shaders.EntityShader;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.joml.Matrix4f;
import org.joml.Vector4f;

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

    private int[] viewport = new int[4];
    private AABB frustum;

    @Override
    public void render(RenderContext renderContext, GameContext gameContext, RenderPass renderPass) {
        List<Entity> entities = gameContext.getGameState().getEntities();
        calculateFrustum(renderContext);
        cullEntities(gameContext, entities);

        entityShader.bind();
        entityShader.setProjectionMatrix(renderContext.getWorldMatrix());
        for (Entity entity : entities) {
            if (!entity.isInFrustum())
                continue;
            if (entity.getGlowTexture() == null && renderPass == RenderPass.GLOW) // There may be entities that do not have a glow texture
                continue;
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

    private void calculateFrustum(RenderContext renderContext) {
        GameContext gameContext = renderContext.getGameContext();
        GameWindow gameWindow = gameContext.getGameWindow();
        viewport[2] = gameWindow.getWidth();
        viewport[3] = gameWindow.getHeight();
        Vector4f tl = renderContext.getWorldMatrix().unproject(0, gameWindow.getHeight(), 0.0f, viewport, new Vector4f());
        Vector4f tr = renderContext.getWorldMatrix().unproject(gameWindow.getWidth(), gameWindow.getHeight(), 0.0f, viewport, new Vector4f());
        Vector4f bl = renderContext.getWorldMatrix().unproject(0, 0, 0.0f, viewport, new Vector4f());
        Vector4f br = renderContext.getWorldMatrix().unproject(gameWindow.getWidth(), 0, 0.0f, viewport, new Vector4f());

        Vector4f[] frustumCorners = new Vector4f[]{
                tl,
                tr,
                bl,
                br
        };

        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        for (Vector4f corner : frustumCorners) {
            if (corner.x < minX)
                minX = corner.x;
            if (corner.y < minY)
                minY = corner.y;
            if (corner.x > maxX)
                maxX = corner.x;
            if (corner.y > maxY)
                maxY = corner.y;
        }

        frustum = new AABB(new Vec2(minX, minY), new Vec2(maxX, maxY));
    }

    private void cullEntities(GameContext gameContext, List<Entity> entities) {
        for (Entity entity : entities)
            if (entity instanceof EntityStatic)
                entity.setInFrustum(false);
        gameContext.getPhysicsEngine().getWorld().queryAABB(fixture -> {
            if (fixture.getUserData() == null || !(fixture.getUserData() instanceof Entity)) return true;
            ((Entity) fixture.getUserData()).setInFrustum(true);
            return true;
        }, frustum);

    }

    @Override
    public void destroy(RenderContext renderContext, GameContext gameContext) {
        rectangle.destroy();
        entityShader.destroy();
    }

}
