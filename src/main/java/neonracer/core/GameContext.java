package neonracer.core;

import neonracer.client.Client;
import neonracer.gui.input.KeyboardState;
import neonracer.gui.input.MouseState;
import neonracer.phys.PhysicsEngine;
import neonracer.render.GameWindow;
import neonracer.render.gl.ShaderProvider;
import neonracer.render.gl.TextureProvider;
import neonracer.resource.DataManager;

import java.io.IOException;

public class GameContext {

    private GameWindow gameWindow;

    private TextureProvider textureProvider;

    private ShaderProvider shaderProvider;

    private DataManager dataManager;

    private PhysicsEngine physicsEngine;

    private Timer timer;

    private GameState gameState;

    private MouseState mouseState;

    private KeyboardState keyboardState;

    private Client client;

    GameContext(GameWindow gameWindow, TextureProvider textureProvider, ShaderProvider shaderProvider, DataManager dataManager, GameState gameState, PhysicsEngine physicsEngine, Timer timer, MouseState mouseState, KeyboardState keyboardState, Client client) {
        this.gameWindow = gameWindow;
        this.textureProvider = textureProvider;
        this.shaderProvider = shaderProvider;
        this.dataManager = dataManager;
        this.gameState = gameState;
        this.physicsEngine = physicsEngine;
        this.timer = timer;
        this.mouseState = mouseState;
        this.keyboardState = keyboardState;
        this.client = client;
    }

    public void initialize() throws IOException {
        gameWindow.create();
        gameWindow.setIcon("icon.png");

        dataManager.load(this);
        if (physicsEngine != null) physicsEngine.initialize(this);
        gameState.initialize(this);
    }

    public void destroy() {
        gameWindow.destroy();
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

    public DataManager getDataManager() {
        return dataManager;
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
}
