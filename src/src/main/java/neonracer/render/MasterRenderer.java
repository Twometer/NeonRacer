package neonracer.render;

import neonracer.core.GameContext;
import neonracer.render.gl.core.Mesh;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.shaders.SimpleShader;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

    private GameContext context;

    private SimpleShader simpleShader;

    private Model testModel;

    private Matrix4f projectionMatrix;

    public MasterRenderer(GameContext context) {
        this.context = context;
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

        Mesh rect = new Mesh(3);
        rect.putVertex(0.0f, 0.0f);
        rect.putColor(1.0f, 0.0f, 1.0f);

        rect.putVertex(150.0f, 0.0f);
        rect.putColor(0.0f, 1.0f, 1.0f);

        rect.putVertex(75f, 150.0f);
        rect.putColor(1.0f, 1.0f, 0.0f);

        testModel = Model.create(rect);
        rect.destroy();

        projectionMatrix = new Matrix4f().setOrtho2D(0, context.getGameWindow().getWidth(), context.getGameWindow().getHeight(), 0);

    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, context.getGameWindow().getWidth(), context.getGameWindow().getHeight());

        simpleShader.bind();
        simpleShader.setProjectionMatrix(projectionMatrix);
        testModel.draw();
        simpleShader.unbind();
    }

    private void destroy() {
        testModel.destroy();
        simpleShader.destroy();
    }

}
