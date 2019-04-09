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

    GameContext(GameWindow gameWindow, TextureProvider textureProvider, DataManager dataManager, GameState gameState, PhysicsEngine physicsEngine, Timer timer) {
        this.gameWindow = gameWindow;
        this.textureProvider = textureProvider;
        this.dataManager = dataManager;
        this.gameState = gameState;
        this.physicsEngine = physicsEngine;
        this.timer = timer;
    }

    public void initialize() throws IOException {
        gameWindow.create();
        dataManager.load(this);
        physicsEngine.initialize(this);
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
}
