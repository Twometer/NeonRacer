package neonracer.core;

import neonracer.render.GameWindow;
import neonracer.render.gl.TextureProvider;
import neonracer.resource.DataManager;

import java.io.IOException;

public class GameContext {

    private GameWindow gameWindow;

    private TextureProvider textureProvider;

    private DataManager dataManager;

    private GameState gameState;

    GameContext(GameWindow gameWindow, TextureProvider textureProvider, DataManager dataManager, GameState gameState) {
        this.gameWindow = gameWindow;
        this.textureProvider = textureProvider;
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

    public TextureProvider getTextureProvider() {
        return textureProvider;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public GameState getGameState() {
        return gameState;
    }
}
