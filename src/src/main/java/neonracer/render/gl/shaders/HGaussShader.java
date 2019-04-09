package neonracer.render.gl.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;

public class HGaussShader extends Shader {

    private int targetWidthLocation;

    public HGaussShader() {
        super("hgauss");
    }

    @Override
    protected void bindUniforms(int program) {
        targetWidthLocation = glGetUniformLocation(program, "targetWidth");
    }

    public void setTargetWidth(float targetWidth) {
        glUniform1f(targetWidthLocation, targetWidth);
    }

}
