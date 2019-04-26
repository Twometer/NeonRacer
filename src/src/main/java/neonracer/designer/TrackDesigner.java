package neonracer.designer;

import neonracer.core.GameContext;
import neonracer.core.GameContextFactory;
import neonracer.gui.GuiContext;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.parser.ScreenLoader;
import neonracer.gui.util.PrimitiveRenderer;
import neonracer.model.track.Node;
import neonracer.render.GameWindow;
import neonracer.render.RenderContext;
import neonracer.render.engine.Camera;
import neonracer.render.engine.mesh.MeshBuilder;
import neonracer.render.engine.mesh.Rectangle;
import neonracer.render.gl.core.Mesh;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.shaders.FlatShader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

class TrackDesigner {

    private GameContext gameContext = GameContextFactory.createForDesigner();

    private RenderContext renderContext = new RenderContext(new Camera(gameContext));

    private FontRenderer fontRenderer = new FontRenderer("lucida");

    private PrimitiveRenderer primitiveRenderer = new PrimitiveRenderer(renderContext);

    private GuiContext guiContext = new GuiContext(gameContext.getKeyboardState(), gameContext.getMouseState(), renderContext, fontRenderer, primitiveRenderer);

    private FlatShader flatShader;

    private Vector2f selectedNode;

    private List<Node> nodes = new ArrayList<>();

    private Model crosshairModel;

    private Model nodeModel;

    private TestScreen testScreen;

    void start() throws IOException {
        gameContext.initialize();
        fontRenderer.setup(gameContext);

        testScreen = ScreenLoader.loadScreen(TestScreen.class);
        testScreen.setWidth(gameContext.getGameWindow().getWidth());
        testScreen.setHeight(gameContext.getGameWindow().getHeight());
        testScreen.initialize(gameContext);
        testScreen.performLayout();

        setup();
        startRenderLoop();
    }

    private Matrix4f transformation = new Matrix4f();
    private Vector2f lastMouse;
    private boolean lastPressed;
    private int[] viewport = new int[4];
    private Vector2f unprojected;

    private void setup() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        flatShader = new FlatShader();

        MeshBuilder meshBuilder = new MeshBuilder(6);
        meshBuilder.putRectVertices(new Rectangle(0, 0, 1, 1));
        Mesh mesh = meshBuilder.getMesh();
        nodeModel = Model.create(mesh, GL_TRIANGLES);
        mesh.destroy();

        Mesh crosshairMesh = new Mesh(4);
        crosshairMesh.putVertex(0f, -10000f);
        crosshairMesh.putVertex(0f, 10000f);
        crosshairMesh.putVertex(-10000f, 0f);
        crosshairMesh.putVertex(10000f, 0f);
        crosshairModel = Model.create(crosshairMesh, GL_LINES);
        crosshairMesh.destroy();

        primitiveRenderer.initialize();

        renderContext.getCamera().setZoomFactor(0.01f);

        gameContext.getGameWindow().setSizeChangedListener((width, height) -> testScreen.performLayout());
    }

    private void startRenderLoop() {
        GameWindow gameWindow = gameContext.getGameWindow();
        gameWindow.setMouseScrollListener(this::onScroll);
        while (!gameWindow.shouldClose()) {
            render();
            gameWindow.update();
        }
    }

    private void render() {
        GameWindow gameWindow = gameContext.getGameWindow();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, gameWindow.getWidth(), gameWindow.getHeight());
        viewport[2] = gameWindow.getWidth();
        viewport[3] = gameWindow.getHeight();

        renderContext.updateMatrices(gameContext);

        gameContext.getKeyboardState().update(gameWindow);
        gameContext.getMouseState().update(gameWindow);

        // Figure out where the mouse cursor would be in world space
        Vector4f unprojected4 = renderContext.getWorldMatrix().unproject(gameWindow.getCursorPosition().x, gameWindow.getHeight() - gameWindow.getCursorPosition().y, 0.0f, viewport, new Vector4f());
        this.unprojected = new Vector2f(unprojected4.x, unprojected4.y);


        float lh = fontRenderer.getLineHeight(0.2f);
        fontRenderer.draw(renderContext, "node_count=" + nodes.size(), 0, 0, 0.2f);
        fontRenderer.draw(renderContext, "scale=" + renderContext.getCamera().getZoomFactor(), 0, lh, 0.2f);

        fontRenderer.draw(renderContext, unprojected.toString(NumberFormat.getNumberInstance()), gameContext.getMouseState().getPosition().x + 10f, gameContext.getMouseState().getPosition().y + 10f, 0.3f);

        // Finally, render all the nodes
        flatShader.bind();
        flatShader.setProjectionMatrix(renderContext.getWorldMatrix());

        for (Node node : nodes) {
            flatShader.setTransformationMatrix(transformation.setTranslation(node.getPosition().x, node.getPosition().y, 0));
            if (selectedNode == node.getPosition())
                flatShader.setColor(1f, 1f, 0f, 1f);
            else
                flatShader.setColor(1f, 0f, 0f, 1f);
            nodeModel.draw();
        }

        flatShader.setTransformationMatrix(new Matrix4f());
        flatShader.setColor(1f, 1f, 1f, 1f);
        crosshairModel.draw();

        flatShader.setProjectionMatrix(renderContext.getGuiMatrix());
        flatShader.setTransformationMatrix(new Matrix4f().translate(gameWindow.getWidth() - 300f, 0, 0).scale(300.0f, gameWindow.getHeight(), 1.0f));
        flatShader.setColor(0.5f, 0.5f, 0.5f, 0.95f);
        nodeModel.draw();

        flatShader.unbind();

        fontRenderer.draw(renderContext, "Node Properties", gameWindow.getWidth() - 150 - fontRenderer.getStringWidth("Node Properties", 0.3f) / 2, 10, 0.3f);

        testScreen.draw(guiContext);

        // Do the controls handling
        Vector2f curMouse = gameContext.getMouseState().getPosition();
        if (gameContext.getMouseState().isMiddle()) {
            float dx = curMouse.x - lastMouse.x;
            float dy = curMouse.y - lastMouse.y;
            renderContext.getCamera().translate(-dx * 0.1f, dy * 0.1f);
        }
        lastMouse = curMouse;

        if (gameContext.getMouseState().isLeft()) {
            if (!lastPressed) onClick();
            lastPressed = true;
        }
        if (!gameContext.getMouseState().isLeft()) lastPressed = false;
    }

    private void onClick() {

        GameWindow gameWindow = gameContext.getGameWindow();

        // Check if user selected a node
        for (Node node : nodes) {
            if (node.getPosition().x == (int) Math.floor(unprojected.x) && node.getPosition().y == (int) Math.floor(unprojected.y)) {
                selectedNode = node.getPosition();
                return;
            }
        }

        // Add a node at mouse position
        nodes.add(new Node((int) Math.floor(unprojected.x), (int) Math.floor(unprojected.y), 0, ""));
    }

    private void onScroll(float x, float y) {
        renderContext.getCamera().zoom(y * 0.0001f);
    }

}
