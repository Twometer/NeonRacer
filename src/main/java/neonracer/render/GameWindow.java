package neonracer.render;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Abstraction layer over GLFW functions
 */
public class GameWindow {

    private int width;

    private int height;

    private String title;

    private long window;

    private float scaleX;

    private float scaleY;

    private SizeChangedListener sizeChangedListener;

    public GameWindow(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    /**
     * Initializes GLFW, creates the window and sets the OpenGL context
     */
    public void create() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_SAMPLES, 8);

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create GLFW window");

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            if (sizeChangedListener != null)
                sizeChangedListener.onSizeChanged(width, height);
        });

        glfwMakeContextCurrent(window);

        float[] x = new float[1];
        float[] y = new float[1];
        glfwGetWindowContentScale(window, x, y);
        scaleX = x[0];
        scaleY = y[0];

        if (scaleX > 1.0f)
            setSize((int) (width * scaleX), (int) (height * scaleY));

        GL.createCapabilities();
    }

    /**
     * Cleans up all native resources used by the window
     */
    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    /**
     * Swaps back and front buffer and does event/message pumping
     */
    void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    /**
     * Used by the renderer to determine when to exit the render loop
     *
     * @return Whether the user requested the window to close
     */
    boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getScale() {
        return (scaleX + scaleY) / 2;
    }

    boolean isKeyPressed(int key) {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }

    void setSizeChangedListener(SizeChangedListener sizeChangedListener) {
        this.sizeChangedListener = sizeChangedListener;
    }

    private void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        glfwSetWindowSize(window, width, height);
    }

    public interface SizeChangedListener {
        void onSizeChanged(int width, int height);
    }

}