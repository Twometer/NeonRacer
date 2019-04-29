package neonracer.gui.widget;

import neonracer.gui.GuiContext;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Color;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import neonracer.render.engine.RenderPass;
import org.joml.Vector4f;

public class Button extends Widget {

    private FontRenderer fontRenderer;

    private String text;

    private float fontSize;

    private Color textColor = Color.WHITE;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    private int offset;

    public Color getTextColor() {
        return textColor;
    }

    @Override
    public void initialize(GuiContext guiContext) {
        super.initialize(guiContext);
        this.fontRenderer = guiContext.getFontRenderer();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    @Override
    public void draw(GuiContext guiContext, RenderPass renderPass) {
        boolean hover = isMouseHover(guiContext);
        if (hover && offset < 20)
            offset += 1;
        else if (!hover && offset > 0)
            offset -= 1;

        fontRenderer.draw(text, getX() + getMargin().getLeft() + offset, getY() + getMargin().getTop(), fontSize, textColor.toVector(renderPass == RenderPass.GLOW ? 0.3f : 1.0f));
        if (offset > 0)
            fontRenderer.draw("> ", getX() + getMargin().getLeft() + offset - fontRenderer.getStringWidth("> ", fontSize), getY() + getMargin().getTop(), fontSize, new Vector4f(textColor.getR(), textColor.getG(), textColor.getB(), offset / 20f));

    }

    @Override
    public Size measure() {
        return new Size((int) fontRenderer.getStringWidth(text, fontSize), (int) fontRenderer.getStringHeight(text, fontSize));
    }
}
