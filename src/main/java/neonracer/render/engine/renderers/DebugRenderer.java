package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.gui.font.FontFamily;
import neonracer.gui.font.FontRenderer;
import neonracer.phys.entity.car.AbstractCarPhysics;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import neonracer.util.BuildInfo;
import neonracer.util.MathHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class DebugRenderer implements IRenderer {

    private long lastReset = 0;
    private int fps = 0;
    private int frames = 0;

    @Override
    public void setup(RenderContext renderContext, GameContext gameContext) {
    }

    @Override
    public void render(RenderContext renderContext, GameContext gameContext, RenderPass renderPass) {
        if (renderPass != RenderPass.COLOR) return;
        FontRenderer fontRenderer = renderContext.getFonts().get(FontFamily.Content);
        float lh = fontRenderer.getLineHeight(0.3f);
        List<String> text = new ArrayList<>();
        text.add(BuildInfo.getGameTitle() + " v" + BuildInfo.getGameVersion());
        text.add("fps=" + fps);
        text.add("pos=" + renderContext.getCamera().getCenterPoint().toString(NumberFormat.getInstance()));
        text.add("rot=" + renderContext.getCamera().getRotation());
        text.add("track=" + gameContext.getGameState().getCurrentTrack().getId());
        text.add("carrot=" + MathHelper.modAngle(gameContext.getGameState().getPlayerEntity().getPhysics().getRotation()));
        text.add("carvelx=" + gameContext.getGameState().getPlayerEntity().getPhysics().getVelocity().x);
        text.add("carvely=" + gameContext.getGameState().getPlayerEntity().getPhysics().getVelocity().y);
        text.add("cardragx=" + ((AbstractCarPhysics) gameContext.getGameState().getPlayerEntity().getPhysics()).getCarBody().getLastDrag().x);
        text.add("cardragy=" + ((AbstractCarPhysics) gameContext.getGameState().getPlayerEntity().getPhysics()).getCarBody().getLastDrag().y);
        text.add("tire1rot=" + MathHelper.modAngle(((AbstractCarPhysics) gameContext.getGameState().getPlayerEntity().getPhysics()).getTires().get(0).getBody().getAngle()));
        text.add("tire1forcea=" + MathHelper.vec2ToAngle(((AbstractCarPhysics) gameContext.getGameState().getPlayerEntity().getPhysics()).getTires().get(0).getCurrentDrive()));
        text.add("tire2rot=" + MathHelper.modAngle(((AbstractCarPhysics) gameContext.getGameState().getPlayerEntity().getPhysics()).getTires().get(1).getBody().getAngle()));
        text.add("tire3rot=" + MathHelper.modAngle(((AbstractCarPhysics) gameContext.getGameState().getPlayerEntity().getPhysics()).getTires().get(2).getBody().getAngle()));
        text.add("tire4rot=" + MathHelper.modAngle(((AbstractCarPhysics) gameContext.getGameState().getPlayerEntity().getPhysics()).getTires().get(3).getBody().getAngle()));
        for (String string : text)
        {
            fontRenderer.draw(string,0.0f, lh * text.indexOf(string), 0.3f);
        }
        frames++;
        if (System.currentTimeMillis() - lastReset > 1000) {
            fps = frames;
            frames = 0;
            lastReset = System.currentTimeMillis();
        }
    }

    @Override
    public void destroy(RenderContext renderContext, GameContext gameContext) {
    }

}
