package neonracer.render.gl;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class Texture {

    private static final String TEXTURE_DIRECTORY = "textures/";

    private int textureId;

    private int width;

    private int height;

    Texture(int textureId, int width, int height) {
        this.textureId = textureId;
        this.width = width;
        this.height = height;
    }

    public static Texture create(String name) {
        return Loader.loadTexture(normalizePath(name));
    }

    /**
     * Textures should be in the textures/ directory, so this method makes sure
     * that the path is in the appropriate directory
     *
     * @param path Input path
     * @return The normalized path
     */
    private static String normalizePath(String path) {
        if (!path.startsWith(TEXTURE_DIRECTORY))
            path = TEXTURE_DIRECTORY + path;
        return path;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
