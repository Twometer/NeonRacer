package neonracer.gui.font;

import neonracer.core.GameContext;
import neonracer.render.RenderContext;

public class Fonts {

    private FontRenderer titleFont;

    private FontRenderer contentFont;

    public Fonts() {
        titleFont = new FontRenderer("redthinker");
        contentFont = new FontRenderer("lucida");
    }

    public void initialize(RenderContext renderContext, GameContext gameContext) {
        titleFont.initialize(renderContext, gameContext);
        contentFont.initialize(renderContext, gameContext);
    }

    public FontRenderer getTitleFont() {
        return titleFont;
    }

    public FontRenderer getContentFont() {
        return contentFont;
    }

}
