package neonracer.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MasterRenderer {

    private GameWindow window;

    public MasterRenderer(GameWindow window) {
        this.window = window;
    }

    public void startLoop() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // TODO: Rendering

            window.update();
        }
    }

}
