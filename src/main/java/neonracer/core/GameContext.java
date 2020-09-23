package neonracer.core;

import neonracer.client.Client;
import neonracer.gui.input.KeyboardState;
import neonracer.gui.input.MouseState;
import neonracer.phys.PhysicsEngine;
import neonracer.render.GameWindow;
import neonracer.render.gl.ShaderProvider;
import neonracer.render.gl.TextureProvider;
import neonracer.resource.AssetProvider;

import java.io.IOException;

public class GameContext {

    private final GameWindow gameWindow;

    private final TextureProvider textureProvider;

    private final ShaderProvider shaderProvider;

    private final AssetProvider assetProvider;

    private final PhysicsEngine physicsEngine;

    private final Timer timer;

    private final GameState gameState;

    private final MouseState mouseState;

    private final KeyboardState keyboardState;

    private final Client client;

    private final boolean debugMode;

    GameContext(GameWindow gameWindow, TextureProvider textureProvider, ShaderProvider shaderProvider,
                AssetProvider assetProvider, GameState gameState, PhysicsEngine physicsEngine,
                Timer timer, MouseState mouseState, KeyboardState keyboardState, Client client, boolean debugMode) {
        this.gameWindow = gameWindow;
        this.textureProvider = textureProvider;
        this.shaderProvider = shaderProvider;
        this.assetProvider = assetProvider;
        this.gameState = gameState;
        this.physicsEngine = physicsEngine;
        this.timer = timer;
        this.mouseState = mouseState;
        this.keyboardState = keyboardState;
        this.client = client;
        this.debugMode = debugMode;
    }

    public void initialize() throws IOException {
        gameWindow.create();
        gameWindow.setIcon("icon.png");

        assetProvider.load(this);
        if (physicsEngine != null) physicsEngine.initialize(this);
        gameState.initialize(this);

        if (client != null) client.initialize(this);
    }

    public void destroy() {
        gameWindow.destroy();
        if (client != null) client.disconnect();
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public TextureProvider getTextureProvider() {
        return textureProvider;
    }

    public ShaderProvider getShaderProvider() {
        return shaderProvider;
    }

    public AssetProvider getAssetProvider() {
        return assetProvider;
    }

    public GameState getGameState() {
        return gameState;
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public Timer getTimer() {
        return timer;
    }

    public MouseState getMouseState() {
        return mouseState;
    }

    public KeyboardState getKeyboardState() {
        return keyboardState;
    }

    public Client getClient() {
        return client;
    }

    public boolean isDebugMode() {
        return debugMode;
    }
}
