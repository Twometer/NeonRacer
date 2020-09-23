package neonracer.render.gl.shaders;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class FontShader extends Shader {

    private int projectionMatrix;
    private int transformationMatrix;

    private int color;

    public FontShader() {
        super("font");
    }

    @Override
    protected void bindUniforms(int program) {
        this.projectionMatrix = glGetUniformLocation(program, "projectionMatrix");
        this.transformationMatrix = glGetUniformLocation(program, "transformationMatrix");
        this.color = glGetUniformLocation(program, "color");
    }

    public void setProjectionMatrix(Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        glUniformMatrix4fv(projectionMatrix, false, buffer);
    }

    public void setTransformationMatrix(Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        glUniformMatrix4fv(transformationMatrix, false, buffer);
    }

    public void setColor(float r, float g, float b, float a) {
        glUniform4f(color, r, g, b, a);
    }

}
