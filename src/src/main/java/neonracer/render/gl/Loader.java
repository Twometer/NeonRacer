package neonracer.render.gl;

import neonracer.util.Log;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL20.*;

/**
 * This class handles loading of resources to OpenGL,
 * such as textures and shaders
 */
class Loader {

    private static final int BYTES_PER_PIXEL = 4;

    /**
     * Compiles a shader program consisting of vertex and fragment shader and uploads
     * it to OpenGL
     *
     * @param vertPath Virtual path to the vertex shader
     * @param fragPath Virtual path to the fragment shader
     * @return The OpenGL id of the shader program
     */
    static int loadShader(String vertPath, String fragPath) {
        try {
            int vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
            int fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);

            String vertexShaderCode = loadString(vertPath);
            String fragmentShaderCode = loadString(fragPath);

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
     * Loads an image from the embedded jar resources and uploads it to the video memory as a 2D texture
     *
     * @param path The virtual path to the image file
     * @return A texture object containing all relevant information about the texture
     */
    static Texture loadTexture(String path) {
        try {
            BufferedImage image = loadImage(path);
            ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = new Color(image.getRGB(x, y));
                    buffer.put((byte) color.getRed());
                    buffer.put((byte) color.getGreen());
                    buffer.put((byte) color.getBlue());
                    buffer.put((byte) color.getAlpha());
                }
            }
            buffer.flip();

            int textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glBindTexture(GL_TEXTURE_2D, 0);

            return new Texture(textureId, image.getWidth(), image.getHeight());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a string from a file embedded in the jar resources
     *
     * @param path The path to the file
     * @return The contents of the file
     * @throws IOException Will be thrown if the file does not exist, or the reading fails for any other reason.
     */
    private static String loadString(String path) throws IOException {
        InputStream stream = Loader.class.getClassLoader().getResourceAsStream(path);
        if (stream == null)
            throw new IOException(String.format("Failed to load '%s' from resources", path));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            builder.append(line).append("\n");
        return builder.toString();
    }

    /**
     * Loads an image from a file embedded in the jar resources
     *
     * @param path The path to the image
     * @return The image
     * @throws IOException Will be thrown if the file does not exist, or the image decoding fails
     */
    private static BufferedImage loadImage(String path) throws IOException {
        InputStream stream = Loader.class.getClassLoader().getResourceAsStream(path);
        if (stream == null)
            throw new IOException(String.format("Failed to load '%s' from resources", path));
        return ImageIO.read(stream);
    }

}