package neonracer.core;

import neonracer.render.GameWindow;
import neonracer.resource.DataManager;
import neonracer.render.gl.TextureManager;
import neonracer.util.BuildInfo;

public class GameContextFactory {

    public static GameContext createDefault() {
        GameWindow gameWindow = new GameWindow(800, 600, BuildInfo.getGameTitle());
        TextureManager textureManager = new TextureManager();
        DataManager dataManager = new DataManager();
        return new GameContext(gameWindow, textureManager, dataManager);
    }

}
