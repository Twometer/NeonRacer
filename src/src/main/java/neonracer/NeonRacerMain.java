package neonracer;

import neonracer.render.GameWindow;
import neonracer.render.MasterRenderer;
import neonracer.util.Log;
import org.lwjgl.Version;

import static org.lwjgl.opengl.GL11.*;

public class NeonRacerMain {

    public static void main(String[] args) {
        Log.i("Starting Neon Racer");
        Log.i("LWJGL Version " + Version.getVersion());

        GameWindow gameWindow = new GameWindow(800, 600, "Neon Racer");
        MasterRenderer masterRenderer = new MasterRenderer(gameWindow);

        gameWindow.create();

        masterRenderer.startLoop();

        Log.i("Shutting down...");
        gameWindow.destroy();
    }

}
