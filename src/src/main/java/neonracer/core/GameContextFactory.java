package neonracer.core;

import neonracer.phys.PhysicsEngine;
import neonracer.render.GameWindow;
import neonracer.render.gl.TextureProvider;
import neonracer.resource.DataManager;
import neonracer.util.BuildInfo;

public class GameContextFactory {

    public static GameContext createDefault() {
        GameWindow gameWindow = new GameWindow(800, 600, BuildInfo.getGameTitle());
        TextureProvider textureProvider = new TextureProvider();
        DataManager dataManager = new DataManager();
        GameState gameState = new GameState();
        PhysicsEngine physicsEngine = new PhysicsEngine();
        Timer timer = new Timer(30);
        return new GameContext(gameWindow, textureProvider, dataManager, gameState, physicsEngine, timer);
    }

}
