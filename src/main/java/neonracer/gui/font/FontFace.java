package neonracer.gui.font;

import neonracer.core.GameContext;
import neonracer.render.gl.core.Texture;

import java.io.IOException;
import java.util.Map;

class FontFace {

    private static final String FONTS_DIRECTORY = "font/";

    private Texture fontTexture;

    private Map<Integer, Glyph> glyphs;

    private FontFace(Texture fontTexture, Map<Integer, Glyph> glyphs) {
        this.fontTexture = fontTexture;
        this.glyphs = glyphs;
    }

    static FontFace load(GameContext gameContext, String name) throws IOException {
        Texture texture = gameContext.getTextureProvider().getTexture(FONTS_DIRECTORY + name + ".png");
        Map<Integer, Glyph> glyphs = FontFileParser.fromFile(FONTS_DIRECTORY + name + ".fnt").parse();
        return new FontFace(texture, glyphs);
    }

    Glyph getGlyph(char chr) {
        return glyphs.get((int) chr);
    }

    Texture getFontTexture() {
        return fontTexture;
    }
}
