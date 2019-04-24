package neonracer.designer;

import neonracer.core.GameContext;
import neonracer.core.GameContextFactory;
import neonracer.model.track.Node;
import neonracer.render.GameWindow;
import neonracer.render.RenderContext;
import neonracer.render.engine.Camera;
import neonracer.render.engine.font.FontRenderer;
import neonracer.render.engine.mesh.MeshBuilder;
import neonracer.render.engine.mesh.Rectangle;
import neonracer.render.gl.core.Mesh;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.shaders.FlatShader;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

class TrackDesigner {

    private GameContext gameContext = GameContextFactory.createForDesigner();

    private RenderContext renderContext = new RenderContext(new Camera(gameContext));

    private FontRenderer fontRenderer = new FontRenderer("lucida");

    private BasicButton testButton = new BasicButton(fontRenderer, 10, 10, "New Node");

    private FlatShader flatShader;

    private List<Node> nodes = new ArrayList<>();

    private Model nodeModel;

    void start() throws IOException {
        gameContext.initialize();
        fontRenderer.setup(gameContext);
        setup();
        startRenderLoop();
    }

    private void setup() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        flatShader = new FlatShader();

        MeshBuilder meshBuilder = new MeshBuilder(6);
        meshBuilder.putRectVertices(new Rectangle(0, 0, 1, 1));
        meshBuilder.putRectColors(1.0f, 0.0f, 0.0f);
        Mesh mesh = meshBuilder.getMesh();
        nodeModel = Model.create(mesh, GL_TRIANGLES);
        mesh.destroy();

        testButton.setOnClickListener(() -> System.out.println("Button pressed"));

        renderContext.getCamera().setZoomFactor(0.01f);
    }

    private void startRenderLoop() {
        GameWindow gameWindow = gameContext.getGameWindow();
        while (!gameWindow.shouldClose()) {
            render();
            gameWindow.update();
        }
    }

    private void render() {
        GameWindow gameWindow = gameContext.getGameWindow();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, gameWindow.getWidth(), gameWindow.getHeight());
        renderContext.updateMatrices(gameContext);

        gameContext.getKeyboardState().update(gameWindow);
        gameContext.getMouseState().update(gameWindow);

        testButton.draw(renderContext, gameContext);

        // Figure out where the mouse cursor would be in world space
        Vector4f unprojected = renderContext.getWorldMatrix().unproject(gameWindow.getCursorPosition().x, gameWindow.getHeight() - gameWindow.getCursorPosition().y, 0.0f, new int[]{0, 0, gameWindow.getWidth(), gameWindow.getHeight()}, new Vector4f());

        // If the mouse is pressed, add a node at this position
        if (gameContext.getMouseState().isLeft()) {
            nodes.add(new Node((int) unprojected.x, (int) unprojected.y, 0, ""));
        }

        // Finally, render all the nodes
        flatShader.bind();
        flatShader.setProjectionMatrix(renderContext.getWorldMatrix());
        for (Node node : nodes) {
            flatShader.setTransformationMatrix(new Matrix4f().setTranslation(node.getPosition().x, node.getPosition().y, 0));
            nodeModel.draw();
        }
        flatShader.unbind();
    }

}
