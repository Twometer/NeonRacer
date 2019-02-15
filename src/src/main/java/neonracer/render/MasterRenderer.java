package neonracer.render;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    private GameWindow window;

    public MasterRenderer(GameWindow window) {
        this.window = window;
    }

    public void startLoop() {
        setup();
        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render();
            window.update();
        }
    }

    private void setup() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }

    private void render() {

    }

}
