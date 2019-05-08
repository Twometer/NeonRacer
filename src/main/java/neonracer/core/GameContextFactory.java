package neonracer.core;

import neonracer.client.Client;
import neonracer.gui.input.KeyboardState;
import neonracer.gui.input.MouseState;
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
        Timer timer = new Timer(60);
        MouseState mouseState = new MouseState();
        KeyboardState keyboardState = new KeyboardState();
        Client client = new Client();
        return new GameContext(gameWindow, textureProvider, dataManager, gameState, physicsEngine, timer, mouseState, keyboardState, client);
    }

    public static GameContext createForDesigner() {
        GameWindow gameWindow = new GameWindow(1000, 800, BuildInfo.getGameTitle() + " - Track Designer");
        TextureProvider textureProvider = new TextureProvider();
        DataManager dataManager = new DataManager();
        GameState gameState = new GameState();
        MouseState mouseState = new MouseState();
        KeyboardState keyboardState = new KeyboardState();
        Client client = new Client();
        return new GameContext(gameWindow, textureProvider, dataManager, gameState, null, null, mouseState, keyboardState, client);
    }

}
