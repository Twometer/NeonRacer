package neonracer.core;

import neonracer.render.GameWindow;
import neonracer.render.gl.TextureManager;
import neonracer.resource.DataManager;

import java.io.IOException;

public class GameContext {

    private GameWindow gameWindow;

    private TextureManager textureManager;

    private DataManager dataManager;

    private GameState gameState;

    GameContext(GameWindow gameWindow, TextureManager textureManager, DataManager dataManager, GameState gameState) {
        this.gameWindow = gameWindow;
        this.textureManager = textureManager;
        this.dataManager = dataManager;
        this.gameState = gameState;
    }

    public void initialize() throws IOException {
        gameWindow.create();
        dataManager.load(this);
    }

    public void destroy() {
        gameWindow.destroy();
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public GameState getGameState() {
        return gameState;
    }
}
