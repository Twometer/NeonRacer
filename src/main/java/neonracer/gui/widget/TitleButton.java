package neonracer.gui.widget;

import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import org.joml.Vector4f;

public class TitleButton extends Widget {

    private String text;

    private int offset;

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

        boolean hover = isMouseHover();
        if (hover && offset < 20)
            offset += 1;
        else if (!hover && offset > 0)
            offset -= 1;

        fontRenderer.draw(text, getX() + getMargin().getLeft() + offset, getY() + getMargin().getTop(), getFontSize(), getFontColor().toVector(renderPass == RenderPass.GLOW ? 0.3f : 1.0f));
        if (offset > 0)
            fontRenderer.draw("> ", getX() + getMargin().getLeft() + offset - fontRenderer.getStringWidth("> ", getFontSize()), getY() + getMargin().getTop(), getFontSize(), new Vector4f(getFontColor().getR(), getFontColor().getG(), getFontColor().getB(), offset / 20f));

    }

    @Override
    public Size measure() {
        return new Size((int) fontRenderer.getStringWidth(text, getFontSize()), (int) fontRenderer.getStringHeight(text, getFontSize()));
    }

}
