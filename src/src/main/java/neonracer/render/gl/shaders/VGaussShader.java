package neonracer.render.gl.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;

public class VGaussShader extends Shader {

    private int targetHeightLocation;

    public VGaussShader() {
        super("vgauss");
    }

    @Override
    protected void bindUniforms(int program) {
        targetHeightLocation = glGetUniformLocation(program, "targetHeight");
    }

    public void setTargetHeight(float targetHeight) {
        glUniform1f(targetHeightLocation, targetHeight);
    }

}
