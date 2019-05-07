package neonracer.render.gl;

import neonracer.render.gl.core.Texture;
import neonracer.resource.ResourceLoader;
import neonracer.util.Log;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL20.*;

/**
 * This class handles loading of resources to OpenGL,
 * such as textures and shaders
 */
public class GlLoader {

    private static final int BYTES_PER_PIXEL = 4;

    /**
     * Compiles a shader program consisting of vertex and fragment shader and uploads
     * it to OpenGL
     *
     * @param vertPath Virtual path to the vertex shader
     * @param fragPath Virtual path to the fragment shader
     * @return The OpenGL id of the shader program
     */
    public static int loadShader(String vertPath, String fragPath) {
        try {
            int vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
            int fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);

            String vertexShaderCode = ResourceLoader.loadString(vertPath);
            String fragmentShaderCode = ResourceLoader.loadString(fragPath);

            glShaderSource(vertexShaderId, vertexShaderCode);
            glCompileShader(vertexShaderId);
            checkShaderError(vertexShaderId);

            glShaderSource(fragmentShaderId, fragmentShaderCode);
            glCompileShader(fragmentShaderId);
            checkShaderError(fragmentShaderId);

            int programId = glCreateProgram();
            glAttachShader(programId, vertexShaderId);
            glAttachShader(programId, fragmentShaderId);
            glLinkProgram(programId);

            // After the shader program is linked, the shader sources can be cleaned up
            glDetachShader(programId, vertexShaderId);
            glDetachShader(programId, fragmentShaderId);
            glDeleteShader(vertexShaderId);
            glDeleteShader(fragmentShaderId);

            return programId;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkShaderError(int shaderId) {
        String log = glGetShaderInfoLog(shaderId);
        if (log.length() > 0)
            Log.e(log);
    }

    /**
     * Loads pixel data for a BufferedImage
     *
     * @param image The BufferedImage
     * @return A ByteBuffer containing the pixel data in format RGBA
     */
    public static ByteBuffer loadPixels(BufferedImage image) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y), true);
                buffer.put((byte) color.getRed());
                buffer.put((byte) color.getGreen());
                buffer.put((byte) color.getBlue());
                buffer.put((byte) color.getAlpha());
            }
        }
        buffer.flip();
        return buffer;
    }

    /**
     * Loads an image from the embedded jar resources and uploads it to the video memory as a 2D texture
     *
     * @param path The virtual path to the image file
     * @return A texture object containing all relevant information about the texture
     */
    public static Texture loadTexture(String path) {
        try {
            BufferedImage image = ResourceLoader.loadImage(path);
            ByteBuffer buffer = loadPixels(image);

            int textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glBindTexture(GL_TEXTURE_2D, 0);

            return new Texture(textureId, image.getWidth(), image.getHeight());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}