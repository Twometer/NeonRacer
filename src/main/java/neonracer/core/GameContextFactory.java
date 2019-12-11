package neonracer.core;

import neonracer.client.DebugClient;
import neonracer.client.NetworkClient;
import neonracer.gui.input.KeyboardState;
import neonracer.gui.input.MouseState;
import neonracer.phys.PhysicsEngine;
import neonracer.render.GameWindow;
import neonracer.render.gl.ShaderProvider;
import neonracer.render.gl.TextureProvider;
import neonracer.resource.DataManager;
import neonracer.util.BuildInfo;

public class GameContextFactory {

    public static GameContext createDefault() {
        GameWindow gameWindow = new GameWindow(1366, 768, BuildInfo.getGameTitle());
        TextureProvider textureProvider = new TextureProvider();
        ShaderProvider shaderProvider = new ShaderProvider();
        DataManager dataManager = new DataManager();
        GameState gameState = new GameState();
        PhysicsEngine physicsEngine = new PhysicsEngine();
        Timer timer = new Timer(60);
        MouseState mouseState = new MouseState();
        KeyboardState keyboardState = new KeyboardState();
        NetworkClient client = new NetworkClient();
        return new GameContext(gameWindow, textureProvider, shaderProvider, dataManager, gameState, physicsEngine, timer, mouseState, keyboardState, client, false);
    }

    public static GameContext createForDebug() {
        GameWindow gameWindow = new GameWindow(1000, 700, BuildInfo.getGameTitle() + " - Debug");
        TextureProvider textureProvider = new TextureProvider();
        ShaderProvider shaderProvider = new ShaderProvider();
        DataManager dataManager = new DataManager();
        GameState gameState = new GameState();
        PhysicsEngine physicsEngine = new PhysicsEngine();
        Timer timer = new Timer(60);
        MouseState mouseState = new MouseState();
        KeyboardState keyboardState = new KeyboardState();
        DebugClient client = new DebugClient();
        return new GameContext(gameWindow, textureProvider, shaderProvider, dataManager, gameState, physicsEngine, timer, mouseState, keyboardState, client, true);
    }

    public static GameContext createForDesigner() {
        GameWindow gameWindow = new GameWindow(1000, 800, BuildInfo.getGameTitle() + " - Track Designer");
        TextureProvider textureProvider = new TextureProvider();
        ShaderProvider shaderProvider = new ShaderProvider();
        DataManager dataManager = new DataManager();
        GameState gameState = new GameState();
        PhysicsEngine physicsEngine = new PhysicsEngine();
        MouseState mouseState = new MouseState();
        KeyboardState keyboardState = new KeyboardState();
        return new GameContext(gameWindow, textureProvider, shaderProvider, dataManager, gameState, physicsEngine, null, mouseState, keyboardState, null, false);
    }

}
