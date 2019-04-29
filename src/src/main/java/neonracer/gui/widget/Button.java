package neonracer.gui.widget;

import neonracer.gui.GuiContext;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;

public class Button extends Widget {

    private String text;

    private float fontSize;

    private FontRenderer fontRenderer;

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

    @Override
    public void initialize(GuiContext guiContext) {
        super.initialize(guiContext);
        this.fontRenderer = guiContext.getFontRenderer();
    }

    @Override
    public void draw(GuiContext guiContext) {
        fontRenderer.draw(text, getX() + getMargin().getLeft(), getY() + getMargin().getTop(), fontSize);
    }

    @Override
    public Size measure() {
        return new Size((int) fontRenderer.getStringWidth(text, fontSize), (int) fontRenderer.getStringHeight(text, fontSize));
    }
}
