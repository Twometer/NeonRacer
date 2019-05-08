package neonracer.designer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
import neonracer.gui.util.Color;
import neonracer.gui.widget.Button;
import neonracer.gui.widget.Label;
import neonracer.model.entity.Entity;
import neonracer.model.entity.EntityItem;
import neonracer.model.entity.EntityStatic;
import neonracer.model.track.Material;
import neonracer.model.track.Node;
import neonracer.model.track.Track;
import neonracer.render.GameWindow;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import neonracer.render.engine.postproc.PostProcessing;
import neonracer.render.engine.renderers.EntityRenderer;
import neonracer.render.engine.renderers.IRenderer;
import neonracer.render.engine.renderers.TrackRenderer;
import neonracer.render.gl.core.Mesh;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.shaders.FlatShader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

// Yes, I know this is ugly. Yes, I could do it better.
// But I don't have the time to do it better, so this class is ugly.
// The rest of the game is not ugly, but this designer is nothing
// the user will ever see.
// If you want to avoid eye bleeding, do not read this file :)

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

    private IRenderer[] renderers = new IRenderer[]{
            new TrackRenderer(),
            new EntityRenderer()
    };

    private DecimalFormat format = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

    private List<Entity> entities = new ArrayList<>();

    private Class[] registeredEntities = new Class[]{
            EntityStatic.class,
            EntityItem.class
    };

    private Class currentEntityClass;

    private Map<String, String> currentEntityParams;

    private int samples = 100;

    @BindWidget("lbNodePosition")
    private Label nodePositionLabel;

    @BindWidget("lbSamples")
    private Label samplesLabel;

    @BindWidget("btnRepositionNode")
    private Button repositionButton;

    @BindWidget("btnAddNode")
    private Button btnAddNode;

    @BindWidget("lbWidth")
    private Label lbWidth;

    @BindWidget("switchMaterialFg")
    private Button btnSwitchFg;

    @BindWidget("switchMaterialBg")
    private Button btnSwitchBg;

    @BindWidget("btnAddEntity")
    private Button btnAddEntity;

    @BindWidget("lbEntityPosition")
    private Label lbEntityPosition;

    @BindWidget("lbRot")
    private Label lbRot;

    private Entity selectedEntity;

    private Mode mode = Mode.None;

    private String bgMaterial = "grass";

    private String fgMaterial = "street";

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    @EventHandler("btnSave")
    public void onSave(ClickEvent event) {
        System.out.printf("- samples: %d%n", samples);
        System.out.println("  background_material: " + bgMaterial);
        System.out.println("  foreground_material: " + fgMaterial);
        System.out.println("  path:");
        for (Node node : nodes) {
            System.out.printf("    - {x: %s, y: %s, w: %s}%n",
                    format.format(node.getPosition().x),
                    format.format(node.getPosition().y),
                    format.format(node.getTrackWidth()));
        }
        System.out.println("  entities:");
        for (Entity temp : entities) {
            if (temp instanceof EntityStatic) {
                EntityStatic entity = (EntityStatic) temp;
                System.out.printf("    - {type: \"%s\", x: %s, y: %s, r: %s, params: {color_texture: \"%s\", glow_texture: \"%s\"}}%n",
                        entity.getType(),
                        format.format(entity.getPosition().x),
                        format.format(entity.getPosition().y),
                        format.format(entity.getRotation()),
                        entity.getColorTexturePath(),
                        entity.getGlowTexturePath());
            } else {
                System.out.println("Cannot serialize entity type " + temp.getType());
            }
        }
    }

    @EventHandler("btnLoad")
    public void onLoad(ClickEvent event) throws IOException {
        JFileChooser chooser = new JFileChooser();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            Track[] tracks = mapper.readValue(file, Track[].class);
            String s = (String) JOptionPane.showInputDialog(
                    null,
                    "Select which track you want to load",
                    "Load file",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    Arrays.stream(tracks).map(c -> c.getName() + " (" + c.getId() + ")").toArray(),
                    tracks[0].getName() + " (" + tracks[0].getId() + ")");
            for (Track track : tracks) {
                if ((track.getName() + " (" + track.getId() + ")").equals(s)) {
                    track.initialize(gameContext);
                    gameContext.getGameState().setCurrentTrack(track);
                    nodes = gameContext.getGameState().getCurrentTrack().getPath();
                    return;
                }
            }
        }
    }

    void start() throws IOException {
        gameContext.initialize();
        renderContext.initialize();


        guiManager.show(this);

        setup();
        startRenderLoop();
    }

    @EventHandler("btnRepositionNode")
    public void onRepositionNode(ClickEvent event) {
        if (selectedNode == null)
            return;
        toggleMode(Mode.Repositioning);
    }

    @EventHandler("addSamples")
    public void onAddSamples(ClickEvent event) {
        this.samples += 100;
        samplesLabel.setText("Samples: " + samples);
        rebuild();
    }

    @EventHandler("remSamples")
    public void onRemSamplles(ClickEvent event) {
        this.samples -= 100;
        samplesLabel.setText("Samples: " + samples);
        rebuild();
    }

    @EventHandler("btnDeleteNode")
    public void onDeleteNode(ClickEvent event) {
        nodes.remove(selectedNode);
        selectedNode = null;
        nodePositionLabel.setText("Position: None");
        rebuild();
    }

    @EventHandler("btnAddNode")
    public void onAddNode(ClickEvent event) {
        toggleMode(Mode.CreatingNodes);
    }

    @EventHandler("widthIncr")
    public void onIncrWidth(ClickEvent event) {
        selectedNode.setTrackWidth(selectedNode.getTrackWidth() + 1);
        lbWidth.setText("Width: " + selectedNode.getTrackWidth());
        rebuild();
    }

    @EventHandler("btnRebuildPreview")
    public void onRebuildPreview(ClickEvent event) {
        rebuild();
    }

    @EventHandler("widthDecr")
    public void onDecrWidth(ClickEvent event) {
        selectedNode.setTrackWidth(selectedNode.getTrackWidth() - 1);
        lbWidth.setText("Width: " + selectedNode.getTrackWidth());
        rebuild();
    }

    @EventHandler("switchMaterialFg")
    public void onSwitchMatFg(ClickEvent event) {
        Material[] mat = gameContext.getDataManager().getMaterials();
        Material nextMaterial = null;

        for (int i = 0; i < mat.length; i++) {
            if (fgMaterial.equalsIgnoreCase(mat[i].getId())) {
                nextMaterial = mat[(i + 1) % mat.length];
                break;
            }
        }
        if (nextMaterial == null) return;

        fgMaterial = nextMaterial.getId();
        btnSwitchFg.setText("Foreground: " + fgMaterial);
        rebuild();
    }

    @EventHandler("switchMaterialBg")
    public void onSwitchMatBg(ClickEvent event) {
        Material[] mat = gameContext.getDataManager().getMaterials();
        Material nextMaterial = null;

        for (int i = 0; i < mat.length; i++) {
            if (bgMaterial.equalsIgnoreCase(mat[i].getId())) {
                nextMaterial = mat[(i + 1) % mat.length];
                break;
            }
        }
        if (nextMaterial == null) return;

        bgMaterial = nextMaterial.getId();
        btnSwitchBg.setText("Background: " + bgMaterial);
        rebuild();
    }

    @EventHandler("btnAddEntity")
    public void onAddEntities(ClickEvent event) throws ClassNotFoundException {

        if (mode == Mode.CreatingEntities) {
            mode = Mode.None;
            return;
        }

        String s = (String) JOptionPane.showInputDialog(
                null,
                "Select which type of entity you want to use",
                "Add Entity",
                JOptionPane.QUESTION_MESSAGE,
                null,
                Arrays.stream(registeredEntities).map(Class::getName).toArray(), registeredEntities[0].getName());

        Class clazz = Class.forName(s);
        RegisteredParams a = (RegisteredParams) clazz.getAnnotation(RegisteredParams.class);
        if (a == null) return;
        String[] params = a.value();

        JTextField[] fields = new JTextField[params.length];

        JPanel panel = new JPanel();
        for (int i = 0; i < params.length; i++) {
            panel.add(new JLabel(params[i] + ": "));
            JTextField tf = new JTextField(15);
            fields[i] = tf;
            panel.add(tf);
        }
        int result = JOptionPane.showConfirmDialog(null, panel, "Enter required parameters", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            currentEntityClass = clazz;
            currentEntityParams = new HashMap<>();
            for (int i = 0; i < params.length; i++) {
                currentEntityParams.put(params[i], fields[i].getText());
            }
            toggleMode(Mode.CreatingEntities);
        }
    }

    @EventHandler("rotIncr")
    public void rotIncr(ClickEvent event) {
        if (selectedEntity != null) {
            selectedEntity.setRotation((float) (selectedEntity.getRotation() + Math.toRadians(2)));
            lbRot.setText("Rotation: " + format.format(Math.toDegrees(selectedEntity.getRotation())));
        }
    }

    @EventHandler("rotDecr")
    public void rotDecr(ClickEvent event) {
        if (selectedEntity != null) {
            selectedEntity.setRotation((float) (selectedEntity.getRotation() - Math.toRadians(2)));
            lbRot.setText("Rotation: " + format.format(Math.toDegrees(selectedEntity.getRotation())));
        }
    }

    @EventHandler("btnDeleteEntity")
    public void delEntity(ClickEvent event) {
        if (selectedEntity != null) {
            entities.remove(selectedEntity);
            System.out.println("Deleted entity");
            rebuild();
        }
    }

    private void toggleMode(Mode mode) {
        if (this.mode != mode)
            setMode(mode);
        else
            setMode(Mode.None);
    }

    private void setMode(Mode mode) {
        if (mode == Mode.Repositioning)
            repositionButton.setFontColor(Color.GREEN);
        else
            repositionButton.setFontColor(Color.BLUE);

        if (mode == Mode.CreatingNodes)
            btnAddNode.setFontColor(Color.GREEN);
        else
            btnAddNode.setFontColor(Color.WHITE);

        if (mode == Mode.CreatingEntities)
            btnAddEntity.setFontColor(Color.GREEN);
        else
            btnAddEntity.setFontColor(Color.WHITE);

        this.mode = mode;
    }

    private void rebuild() {
        gameContext.getGameState().getEntities().clear();
        Track track = new Track("", "", "", "", bgMaterial, fgMaterial, nodes, entities, samples);
        track.initialize(gameContext);

        gameContext.getGameState().setCurrentTrack(track);
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
                lbWidth.setText("Width: " + node.getTrackWidth());
                return;
            }
        }

        for (Entity entity : entities) {
            if (unprojected.x > entity.getPosition().x && unprojected.y > entity.getPosition().y && unprojected.x < entity.getPosition().x + entity.getWidth() && unprojected.y < entity.getPosition().y + entity.getHeight()) {
                selectedEntity = entity;
                lbRot.setText("Rotation: " + Math.toDegrees(selectedEntity.getRotation()));
                lbEntityPosition.setText(selectedEntity.getPosition().toString(NumberFormat.getNumberInstance()));
                break;
            }
        }

        // Add a node at mouse position
        switch (mode) {
            case CreatingNodes:
                Node node = new Node((int) Math.floor(unprojected.x), (int) Math.floor(unprojected.y), 8.0f);
                nodes.add(node);
                if (nodes.size() > 2)
                    rebuild();
                break;
            case Repositioning:
                if (selectedNode == null) return;
                selectedNode.getPosition().x = (int) Math.floor(unprojected.x);
                selectedNode.getPosition().y = (int) Math.floor(unprojected.y);
                rebuild();
                break;
            case CreatingEntities:
                try {
                    Constructor c = currentEntityClass.getConstructor(String.class, float.class, float.class, float.class, Map.class);
                    Entity entity = (Entity) c.newInstance("", unprojected.x, unprojected.y, 0f, currentEntityParams);
                    entity.onInitialize(gameContext);
                    entities.add(entity);
                    rebuild();
                    break;
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
    }

    private Vector2f lastMouse;
    private boolean lastPressed;
    private int[] viewport = new int[4];
    private Vector2f unprojected;

    private void setup() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        flatShader = renderContext.getShader(FlatShader.class);

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

        // Render nodes
        for (Node node : nodes) {
            Vector4f color = selectedNode == node ? Color.GREEN.toVector() : Color.RED.toVector();
            renderContext.getPrimitiveRenderer().drawRect(node.getPosition().x, node.getPosition().y, 1.0f, 1.0f, color, renderContext.getWorldMatrix());
        }

        // Render crosshair
        flatShader.bind();
        flatShader.setProjectionMatrix(renderContext.getWorldMatrix());
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
        } else lastPressed = false;
    }

    enum Mode {
        None,
        Repositioning,
        CreatingNodes,
        CreatingEntities
    }

    private void onScroll(float x, float y) {
        renderContext.getCamera().zoom(y * 0.0001f);
    }

}
