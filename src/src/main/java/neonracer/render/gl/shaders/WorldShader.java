package neonracer.render.gl.shaders;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class WorldShader extends Shader {

    private int projectionMatrix;

    public WorldShader() {
        super("world");
    }

    @Override
    protected void bindUniforms(int program) {
        this.projectionMatrix = glGetUniformLocation(program, "projectionMatrix");
    }

    public void setProjectionMatrix(Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        glUniformMatrix4fv(projectionMatrix, false, buffer);
    }

}
