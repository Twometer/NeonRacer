package neonracer.core;

import neonracer.render.GameWindow;
import neonracer.resource.DataManager;
import neonracer.render.gl.TextureManager;

import java.io.IOException;

public class GameContext {

    private GameWindow gameWindow;

    private TextureManager textureManager;

    private DataManager dataManager;

    GameContext(GameWindow gameWindow, TextureManager textureManager, DataManager dataManager) {
        this.gameWindow = gameWindow;
        this.textureManager = textureManager;
        this.dataManager = dataManager;
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
}
