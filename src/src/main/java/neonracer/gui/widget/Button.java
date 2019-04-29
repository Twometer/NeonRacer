package neonracer.gui.widget;

import neonracer.gui.GuiContext;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;

public class Button extends Widget {

    private String text;

    public Button() {
        setPadding(8);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(GuiContext guiContext) {
        float lh = guiContext.getFontRenderer().getStringHeight(getText(), 1.0f);
        guiContext.getFontRenderer().draw(text, getX() + getPadding(), getY() + getPadding(), (getHeight() - getPadding() * 2) / lh);
    }

    @Override
    public Size measure() {
        return new Size(100, 100);
    }
}
