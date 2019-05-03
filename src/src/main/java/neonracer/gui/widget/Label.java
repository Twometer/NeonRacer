package neonracer.gui.widget;

import neonracer.gui.font.FontFamily;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Color;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;

public class Label extends Widget {

    private FontFamily fontFamily = FontFamily.Title;

    private FontRenderer fontRenderer;

    private String text;

    private float fontSize = 1.0f;

    private Color textColor = Color.WHITE;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FontFamily getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(FontFamily fontFamily) {
        this.fontFamily = fontFamily;
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
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        this.fontRenderer = renderContext.getFonts().get(fontFamily);
    }

    @Override
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        if (renderPass == RenderPass.COLOR)
            fontRenderer.draw(text, getX() + getMargin().getLeft(), getY() + getMargin().getTop(), fontSize, textColor.toVector(1.0f));
    }

    @Override
    public Size measure() {
        return new Size((int) fontRenderer.getStringWidth(text, fontSize), (int) fontRenderer.getStringHeight(text, fontSize));
    }
}
