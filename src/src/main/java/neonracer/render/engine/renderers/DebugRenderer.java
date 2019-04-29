package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.gui.font.FontRenderer;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import neonracer.util.BuildInfo;

import java.text.NumberFormat;

public class DebugRenderer implements IRenderer {

    private long lastReset = 0;
    private int fps = 0;
    private int frames = 0;

    private FontRenderer fontRenderer = new FontRenderer("lucida");

    @Override
    public void setup(RenderContext renderContext, GameContext gameContext) {
        fontRenderer.setup(renderContext, gameContext);
    }


    @Override
    public void render(RenderContext renderContext, GameContext gameContext, RenderPass renderPass) {
        if (renderPass != RenderPass.COLOR) return;
        float lh = fontRenderer.getLineHeight(0.3f);
        fontRenderer.draw(BuildInfo.getGameTitle() + " v" + BuildInfo.getGameVersion(), 0.0f, 0.0f, 0.3f);
        fontRenderer.draw("fps=" + fps, 0.0f, lh, 0.3f);
        fontRenderer.draw("pos=" + renderContext.getCamera().getCenterPoint().toString(NumberFormat.getInstance()), 0.0f, 2 * lh, 0.3f);
        fontRenderer.draw("rot=" + renderContext.getCamera().getRotation(), 0.0f, 3 * lh, 0.3f);
        fontRenderer.draw("track=" + gameContext.getGameState().getCurrentTrack().getId(), 0.0f, 4 * lh, 0.3f);
        frames++;
        if (System.currentTimeMillis() - lastReset > 1000) {
            fps = frames;
            frames = 0;
            lastReset = System.currentTimeMillis();
        }
    }

    @Override
    public void destroy(RenderContext renderContext, GameContext gameContext) {
        fontRenderer.destroy();
    }

}
