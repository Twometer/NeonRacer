package neonracer.gui.widget;

import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;

public class Label extends Widget {

    private FontRenderer fontRenderer;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        this.fontRenderer = getFontRenderer(renderContext);
    }

    @Override
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        if (renderPass == RenderPass.COLOR)
            fontRenderer.draw(text, getX(), getY(), getFontSize(), getFontColor().toVector(1.0f));
    }

    @Override
    public Size measure() {
        return new Size((int) fontRenderer.getStringWidth(text, getFontSize()), (int) fontRenderer.getStringHeight(text, getFontSize()));
    }
}
