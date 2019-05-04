package neonracer.designer;

import neonracer.core.GameContext;
import neonracer.core.GameContextFactory;
import neonracer.gui.GuiManager;
import neonracer.gui.annotation.BindWidget;
import neonracer.gui.annotation.EventHandler;
import neonracer.gui.annotation.LayoutFile;
import neonracer.gui.events.ClickEvent;
import neonracer.gui.font.FontFamily;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.screen.Screen;
import neonracer.gui.widget.Label;
import neonracer.model.track.Node;
import neonracer.model.track.Track;
import neonracer.render.GameWindow;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import neonracer.render.engine.mesh.MeshBuilder;
import neonracer.render.engine.mesh.Rectangle;
import neonracer.render.engine.postproc.PostProcessing;
import neonracer.render.engine.renderers.IRenderer;
import neonracer.render.engine.renderers.TrackRenderer;
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

@LayoutFile("guis/designer.xml")
public class TrackDesigner extends Screen {

    private GameContext gameContext = GameContextFactory.createForDesigner();
    private RenderContext renderContext = new RenderContext(gameContext);

    private GuiManager guiManager = new GuiManager(renderContext);

    private PostProcessing postProcessing;

    private FlatShader flatShader;

    private Node selectedNode;

    private List<Node> nodes = new ArrayList<>();

    private Model crosshairModel;

    private Model nodeModel;

    private IRenderer[] renderers = new IRenderer[]{
            new TrackRenderer()
    };

    @BindWidget("lbNodePosition")
    private Label nodePositionLabel;

    void start() throws IOException {
        gameContext.initialize();
        renderContext.initialize();


        guiManager.show(this);

        setup();
        startRenderLoop();
    }

    @EventHandler("btnDeleteNode")
    public void onDeleteNode(ClickEvent event) {
        nodes.remove(selectedNode);
        selectedNode = null;
        nodePositionLabel.setText("Position: None");
    }

    @EventHandler("btnRepositionNode")
    public void onRepositionNode(ClickEvent event) {

    }

    @EventHandler("btnRebuildPreview")
    public void onRebuildPreview(ClickEvent event) {
        List<Node> path = new ArrayList<>();
        for (Node node : nodes) {
            path.add(new Node((int) node.getPosition().x, (int) node.getPosition().y, 8.0f, "street"));
        }

        Track track = new Track("preview", "", "", "", "grass", path, null);
        track.initialize(gameContext);

        gameContext.getGameState().setCurrentTrack(track);
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

        postProcessing = new PostProcessing(gameContext);

        renderContext.getCamera().setZoomFactor(0.01f);

        gameContext.getGameWindow().setSizeChangedListener((width, height) -> {
            guiManager.resize(width, height);
            postProcessing.onResize(width, height);
        });


        for (IRenderer renderer : renderers)
            renderer.setup(renderContext, gameContext);


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

        renderContext.updateMatrices();

        gameContext.getKeyboardState().update(gameWindow);
        gameContext.getMouseState().update(gameWindow);

        // Figure out where the mouse cursor would be in world space
        viewport[2] = gameWindow.getWidth();
        viewport[3] = gameWindow.getHeight();
        Vector4f unprojected4 = renderContext.getWorldMatrix().unproject(gameWindow.getCursorPosition().x, gameWindow.getHeight() - gameWindow.getCursorPosition().y, 0.0f, viewport, new Vector4f());
        this.unprojected = new Vector2f(unprojected4.x, unprojected4.y);

        postProcessing.beginPass(RenderPass.COLOR);

        // Draw some debug information
        FontRenderer fontRenderer = renderContext.getFonts().get(FontFamily.Content);
        float lh = fontRenderer.getLineHeight(0.2f);
        fontRenderer.draw("node_count=" + nodes.size(), 0, 0, 0.2f);
        fontRenderer.draw("scale=" + renderContext.getCamera().getZoomFactor(), 0, lh, 0.2f);

        fontRenderer.draw(unprojected.toString(NumberFormat.getNumberInstance()), gameContext.getMouseState().getPosition().x + 10f, gameContext.getMouseState().getPosition().y + 10f, 0.3f);

        // Render the track, if there is any
        handleControls();
        for (IRenderer renderer : renderers)
            renderer.render(renderContext, gameContext, RenderPass.COLOR);

        // Render all drawn nodes to the screen
        flatShader.bind();
        flatShader.setProjectionMatrix(renderContext.getWorldMatrix());

        for (Node node : nodes) {
            flatShader.setTransformationMatrix(transformation.setTranslation(node.getPosition().x, node.getPosition().y, 0));
            if (selectedNode == node)
                flatShader.setColor(1f, 1f, 0f, 1f);
            else
                flatShader.setColor(1f, 0f, 0f, 1f);
            nodeModel.draw();
        }

        flatShader.setTransformationMatrix(new Matrix4f());
        flatShader.setColor(1f, 1f, 1f, 1f);
        crosshairModel.draw();
        flatShader.unbind();

        // Draw the GUI
        guiManager.draw(RenderPass.COLOR);

        // Now, draw the glowing parts
        postProcessing.beginPass(RenderPass.GLOW);
        guiManager.draw(RenderPass.GLOW);

        // And send all that to the screen
        postProcessing.draw();


    }

    private void handleControls() {
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
        // Notify the GUI of the click
        ClickEvent event = new ClickEvent();
        guiManager.raiseEvent(event);
        if (event.isConsumed())
            return;

        // Check if user selected a node
        for (Node node : nodes) {
            if (node.getPosition().x == (int) Math.floor(unprojected.x) && node.getPosition().y == (int) Math.floor(unprojected.y)) {
                selectedNode = node;
                nodePositionLabel.setText("Position: " + node.getPosition().toString(NumberFormat.getIntegerInstance()));
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
