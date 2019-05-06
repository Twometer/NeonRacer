package neonracer.gui.widget;

import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Color;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;

public class Button extends Widget {

    private String text;

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
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), Color.GRAY.toVector(isMouseHover() ? 0.5f : 1.0f));
            fontRenderer.draw(text, getX() + 5, getY() + 2, getFontSize(), getFontColor().toVector(1.0f));
        }
    }

    @Override
    public Size measure() {
        return new Size((int) fontRenderer.getStringWidth(text, getFontSize()) + 50, (int) fontRenderer.getStringHeight(text, getFontSize()) + 4);
    }

}
