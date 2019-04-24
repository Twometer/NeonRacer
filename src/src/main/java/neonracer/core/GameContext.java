package neonracer.core;

import neonracer.phys.PhysicsEngine;
import neonracer.render.GameWindow;
import neonracer.render.gl.TextureProvider;
import neonracer.resource.DataManager;

import java.io.IOException;

public class GameContext {

    private GameWindow gameWindow;

    private TextureProvider textureProvider;

    private DataManager dataManager;

    private PhysicsEngine physicsEngine;

    private Timer timer;

    private GameState gameState;

    private ControlState controlState;

    GameContext(GameWindow gameWindow, TextureProvider textureProvider, DataManager dataManager, GameState gameState, PhysicsEngine physicsEngine, Timer timer, ControlState controlState) {
        this.gameWindow = gameWindow;
        this.textureProvider = textureProvider;
        this.dataManager = dataManager;
        this.gameState = gameState;
        this.physicsEngine = physicsEngine;
        this.timer = timer;
        this.controlState = controlState;
    }

    public void initialize() throws IOException {
        gameWindow.create();
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

    public ControlState getControlState() {
        return controlState;
    }

}
