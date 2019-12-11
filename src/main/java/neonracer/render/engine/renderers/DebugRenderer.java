package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.gui.font.FontFamily;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Color;
import neonracer.model.entity.EntityCar;
import neonracer.phys.entity.car.AbstractCarPhysics;
import neonracer.phys.entity.car.DrivableCarPhysics;
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
        EntityCar playerEntity = gameContext.getGameState().getPlayerEntity();
        if (playerEntity != null)
            addPlayerData(playerEntity, text);
        for (String string : text) {
            fontRenderer.draw(string, 0.0f, lh * (text.indexOf(string)+10), 0.3f, Color.WHITE.toVector());
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

    private void addPlayerData(EntityCar playerEntity, List<String> text) {
        text.add("breaking=" + ((DrivableCarPhysics) playerEntity.getPhysics()).braking);
        text.add("driving=" + ((DrivableCarPhysics) playerEntity.getPhysics()).driving);
        text.add("carrot=" + MathHelper.modAngle(playerEntity.getPhysics().getRotation()));
        text.add("carvela=" + MathHelper.vec2ToAngle(playerEntity.getPhysics().getVelocity()));
        text.add("carvelx=" + playerEntity.getPhysics().getVelocity().x);
        text.add("carvely=" + playerEntity.getPhysics().getVelocity().y);
        text.add("carposx=" + playerEntity.getPhysics().getPosition().x);
        text.add("carposy=" + playerEntity.getPhysics().getPosition().y);
        text.add("cardragx=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getCurrentDrag().x);
        text.add("cardragy=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getCurrentDrag().y);
        text.add("tire1rot=" + MathHelper.modAngle(((AbstractCarPhysics) playerEntity.getPhysics()).getTires().get(0).getBody().getAngle()));
        text.add("tire1avel=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getTires().get(0).getBody().getAngularVelocity());
        text.add("tire1velx=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getTires().get(0).getVelocity().x);
        text.add("tire1vely=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getTires().get(0).getVelocity().y);
        //text.add("tire1vela=" + MathHelper.vec2ToAngle(((AbstractCarPhysics) playerEntity.getPhysics()).getTires().get(0).getVelocity()));
        text.add("tire1relvelx=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getTires().get(0).getRelativeVelocity().x);
        text.add("tire1relvely=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getTires().get(0).getRelativeVelocity().y);
        //text.add("tire1relvela=" + MathHelper.vec2ToAngle(((AbstractCarPhysics) playerEntity.getPhysics()).getTires().get(0).getRelativeVelocity()));
        text.add("tire1relfrix=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getTires().get(0).getCurrentRelativeFriction().x);
        text.add("tire1relfriy=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getTires().get(0).getCurrentRelativeFriction().y);
        text.add("drag=" + playerEntity.getCar().getDragCoefficient());
    }

}
