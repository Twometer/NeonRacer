package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.gui.font.FontFamily;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Color;
import neonracer.model.entity.EntityCar;
import neonracer.phys.entity.car.AbstractCarPhysics;
import neonracer.phys.entity.car.DrivableCarPhysics;
import neonracer.phys.entity.car.Tire;
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
        text.add("carvel=" + playerEntity.getPhysics().getVelocity());
        text.add("carpos=" + playerEntity.getPhysics().getPosition());
        text.add("cardrag=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getCurrentDrag());
        text.add("steer=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getFlJoint().getJointAngle());
        text.add("tirerelfri=" + ((AbstractCarPhysics) playerEntity.getPhysics()).getTires()[Tire.BACK_LEFT].getCurrentRelativeFriction());
        text.add("drag=" + playerEntity.getCar().getDragCoefficient());
    }

}
