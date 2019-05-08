package neonracer.gui.widget;

import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import org.joml.Vector4f;

public class ProgressBar extends Widget {

    private static final Vector4f BACKGROUND = new Vector4f(0.031f, 0.1608f, 0.3686f, 0.85f);
    private static final Vector4f GLOW = new Vector4f(0.7411f / 2f, 1.0f, 1.0f, 1.0f);

    private static final int GLOW_RADIUS = 3;

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        GLOW.w = renderPass == RenderPass.COLOR ? 1.0f : 0.5f;
        renderContext.getPrimitiveRenderer().drawRect(getX() - GLOW_RADIUS, getY() - GLOW_RADIUS, getWidth() + GLOW_RADIUS * 2, getHeight() + GLOW_RADIUS * 2, GLOW);
        renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), BACKGROUND);
        renderContext.getPrimitiveRenderer().drawRect(getX() + 1, getY() + 1, getWidth() * (getValue() / 100f) - 2, getHeight() - 2, GLOW);
    }

}
