package neonracer.render;

import neonracer.core.GameContext;
import neonracer.render.engine.Camera;
import neonracer.render.gl.core.Mesh;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.shaders.SimpleShader;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    private GameContext context;

    private Camera camera;

    private SimpleShader simpleShader;

    private Model testModel;

    public MasterRenderer(GameContext context) {
        this.context = context;
        this.camera = new Camera(context);
    }

    public void startLoop() {
        setup();
        while (!context.getGameWindow().shouldClose()) {
            render();
            context.getGameWindow().update();
        }
        destroy();
    }

    private void setup() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        simpleShader = new SimpleShader();

        Mesh rect = new Mesh(4);
        rect.putVertex(0.5f, 0.5f);
        rect.putColor(1.0f, 0.0f, 0.0f);

        rect.putVertex(0.5f,-0.5f);
        rect.putColor(0.0f, 1.0f, 0.0f);

        rect.putVertex(-0.5f,0.5f);
        rect.putColor(0.0f, 0.0f, 1.0f);

        rect.putVertex(-0.5f,-0.5f);
        rect.putColor(1.0f, 1.0f, 0.0f);

        testModel = Model.create(rect, GL_TRIANGLE_STRIP);
        rect.destroy();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, context.getGameWindow().getWidth(), context.getGameWindow().getHeight());
        simpleShader.bind();
        simpleShader.setProjectionMatrix(camera.calculateMatrix());
        testModel.draw();
        simpleShader.unbind();
    }

    private void destroy() {
        testModel.destroy();
        simpleShader.destroy();
    }

}
