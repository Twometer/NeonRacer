package neonracer.gl;

import neonracer.util.Log;

import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;

public abstract class Shader {

    private static final String SHADER_DIRECTORY = "shaders/";
    private static final String VERT_SHADER_EXT = ".v.glsl";
    private static final String FRAG_SHADER_EXT = ".f.glsl";

    private int programId;

    public Shader(String name) {
        String vertexPath = normalizePath(name + VERT_SHADER_EXT);
        String fragmentPath = normalizePath(name + FRAG_SHADER_EXT);
        programId = Loader.loadShader(vertexPath, fragmentPath);
        initialize();
        Log.i(String.format("Loaded shader '%s'", name));
    }

    /**
     * Shaders live in the shaders/ directory, so this method checks whether the path is in
     * this shader directory.
     *
     * @param path Input path
     * @return The normalized path
     */
    private static String normalizePath(String path) {
        if (!path.startsWith(SHADER_DIRECTORY))
            path = SHADER_DIRECTORY + path;
        return path;
    }

    private void initialize() {
        bind();
        bindUniforms(programId);
        unbind();
    }

    /**
     * Binds the shader program
     */
    public final void bind() {
        glUseProgram(programId);
    }

    /**
     * Unbinds the shader program
     */
    public final void unbind() {
        glUseProgram(0);
    }

    /**
     * Releases all used native resources
     */
    public final void destroy() {
        glDeleteProgram(programId);
    }

    /**
     * Asks subclasses to initialize themselves and bind their
     * uniform locations
     *
     * @param program The current program id
     */
    protected abstract void bindUniforms(int program);

}
