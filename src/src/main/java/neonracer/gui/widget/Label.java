package neonracer.gui.widget;

import neonracer.gui.GuiContext;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Color;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import neonracer.render.engine.RenderPass;

public class Label extends Widget {

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

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    @Override
    public void initialize(GuiContext guiContext) {
        super.initialize(guiContext);
        this.fontRenderer = guiContext.getFontRenderer();
    }

    @Override
    public void draw(GuiContext guiContext, RenderPass renderPass) {
        if (renderPass == RenderPass.COLOR)
            fontRenderer.draw(text, getX() + getMargin().getLeft(), getY() + getMargin().getTop(), fontSize, textColor.toVector(1.0f));
    }

    @Override
    public Size measure() {
        return new Size((int) fontRenderer.getStringWidth(text, fontSize), (int) fontRenderer.getStringHeight(text, fontSize));
    }
}
