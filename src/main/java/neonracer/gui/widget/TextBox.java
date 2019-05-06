package neonracer.gui.widget;

import neonracer.gui.events.Event;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Color;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import org.joml.Vector4f;

public class TextBox extends Widget {

    private static final Vector4f BACKGROUND = new Vector4f(0.0f, 0.0509f, 0.1098f, 1.0f);
    private static final Vector4f GLOW = new Vector4f(0.0706f, 0.2314f, 0.5137f, 1.0f);

    private String text = "";

    private FontRenderer fontRenderer;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        fontRenderer = getFontRenderer(renderContext);
    }

    @Override
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        FontRenderer fontRenderer = getFontRenderer(renderContext);
        if (renderPass == RenderPass.COLOR) {
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), BACKGROUND);
            fontRenderer.draw(text, getX(), getY() + 2, getFontSize(), getFontColor().toVector(1.0f));
        } else if (renderPass == RenderPass.GLOW) {
            renderContext.getPrimitiveRenderer().drawRect(getX() - 5, getY() - 5, getWidth() + 10, getHeight() + 10, GLOW);
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), Color.BLACK.toVector());
        }
    }

    @Override
    protected void onEvent(Event event) {
        super.onEvent(event);
    }

}
