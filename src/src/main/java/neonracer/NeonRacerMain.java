package neonracer;

import neonracer.core.GameContext;
import neonracer.core.GameContextFactory;
import neonracer.model.entity.Entity;
import neonracer.model.track.Track;
import neonracer.render.MasterRenderer;
import neonracer.util.BuildInfo;
import neonracer.util.Log;
import org.lwjgl.Version;

import java.io.IOException;

public class NeonRacerMain {

    public static void main(String[] args) throws IOException {
        Log.i(String.format("Starting %s v%s", BuildInfo.getGameTitle(), BuildInfo.getGameVersion()));
        Log.i("LWJGL Version " + Version.getVersion());

        GameContext gameContext = GameContextFactory.createDefault();
        gameContext.initialize();

        MasterRenderer masterRenderer = new MasterRenderer(gameContext);
        masterRenderer.startLoop();

        // When the loop exits, the program was closed, so shut down here
        Log.i("Shutting down...");
        gameContext.destroy();
    }

}
