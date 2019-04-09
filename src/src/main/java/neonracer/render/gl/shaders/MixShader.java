package neonracer.render.gl.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

public class MixShader extends Shader {

    public MixShader() {
        super("mix");
    }

    @Override
    protected void bindUniforms(int program) {
        glUniform1i(glGetUniformLocation(program, "tex1"), 0);
        glUniform1i(glGetUniformLocation(program, "tex2"), 1);
    }

}
