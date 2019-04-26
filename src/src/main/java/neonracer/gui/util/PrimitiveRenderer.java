package neonracer.gui.util;

import neonracer.render.RenderContext;
import neonracer.render.engine.mesh.MeshBuilder;
import neonracer.render.engine.mesh.Rectangle;
import neonracer.render.gl.core.Mesh;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.shaders.FlatShader;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class PrimitiveRenderer {

    private Model rectangle;

    private RenderContext renderContext;

    private FlatShader flatShader;

    public PrimitiveRenderer(RenderContext renderContext) {
        this.renderContext = renderContext;
    }

    public void initialize() {
        flatShader = new FlatShader();
        MeshBuilder meshBuilder = new MeshBuilder(6);
        meshBuilder.putRectVertices(new Rectangle(0, 0, 1, 1));
        meshBuilder.putRectTexCoords(0, 0, 1, 1);
        Mesh mesh = meshBuilder.getMesh();
        rectangle = Model.create(mesh, GL_TRIANGLES);
        mesh.destroy();
    }

    public void drawRect(float x, float y, float width, float height, Vector4f color) {
        flatShader.bind();
        flatShader.setProjectionMatrix(renderContext.getGuiMatrix());
        flatShader.setTransformationMatrix(new Matrix4f().translate(x, y, 0).scale(width, height, 1.0f));
        flatShader.setColor(color.x, color.y, color.z, color.w);
        flatShader.setHasTexture(false);
        rectangle.draw();
        flatShader.unbind();
    }

    public void drawTexturedRect(float x, float y, float width, float height) {
        flatShader.bind();
        flatShader.setProjectionMatrix(renderContext.getGuiMatrix());
        flatShader.setTransformationMatrix(new Matrix4f().translate(x, y, 0).scale(width, height, 1.0f));
        flatShader.setHasTexture(true);
        rectangle.draw();
        flatShader.unbind();
    }

}
