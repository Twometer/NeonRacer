package neonracer.render.gl.shaders;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class FlatShader extends Shader {

    private int transformationMatrix;

    private int projectionMatrix;

    public FlatShader() {
        super("flat");
    }

    @Override
    protected void bindUniforms(int program) {
        this.projectionMatrix = glGetUniformLocation(program, "projectionMatrix");
        this.transformationMatrix = glGetUniformLocation(program, "transformationMatrix");
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

}
