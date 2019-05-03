package neonracer.gui.font;

import neonracer.core.GameContext;
import neonracer.render.RenderContext;

import java.util.HashMap;
import java.util.Map;

public class Fonts {

    private Map<String, FontRenderer> registeredFonts = new HashMap<>();

    private FontRenderer titleFont;

    private FontRenderer contentFont;

    public Fonts() {
        for (FontFamily fontFamily : FontFamily.values())
            registeredFonts.put(fontFamily.getFileName(), new FontRenderer(fontFamily.getFileName()));
    }

    public void initialize(RenderContext renderContext, GameContext gameContext) {
        for (FontRenderer renderer : registeredFonts.values())
            renderer.initialize(renderContext, gameContext);
    }

    public FontRenderer get(FontFamily fontFamily) {
        return registeredFonts.get(fontFamily.getFileName());
    }

}
