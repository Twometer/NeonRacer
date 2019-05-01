package neonracer.gui.font;

import neonracer.resource.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

class FontFileParser {

    private String content;

    private FontFileParser(String content) {
        this.content = content;
    }

    static FontFileParser fromFile(String file) throws IOException {
        return new FontFileParser(ResourceLoader.loadString(file));
    }

    Map<Integer, Glyph> parse() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(content));
        Map<Integer, Glyph> glyphs = new HashMap<>();
        parseDocument(reader, glyphs);
        return glyphs;
    }

    private void parseDocument(BufferedReader reader, Map<Integer, Glyph> glyphs) throws IOException {
        String line;
        while ((line = reader.readLine()) != null)
            if (isGlyph(line)) {
                Glyph glyph = parseGlyph(line);
                glyphs.put(glyph.getId(), glyph);
            }
    }

    private boolean isGlyph(String line) {
        return line.startsWith("char ");
    }

    private Glyph parseGlyph(String line) {
        Glyph glyph = new Glyph();
        glyph.setId(parseProperty(line, "id"));
        glyph.setX(parseProperty(line, "x"));
        glyph.setY(parseProperty(line, "y"));
        glyph.setWidth(parseProperty(line, "width"));
        glyph.setHeight(parseProperty(line, "height"));
        glyph.setxOffset(parseProperty(line, "xoffset"));
        glyph.setyOffset(parseProperty(line, "yoffset"));
        glyph.setAdvance(parseProperty(line, "xadvance"));
        return glyph;
    }

    private int parseProperty(String line, String property) {
        String key = property + "=";
        int offset = line.indexOf(key);
        if (offset < 0)
            throw new RuntimeException("Unknown property " + property);
        String substring = line.substring(offset + key.length());
        String value = substring.substring(0, substring.indexOf(" "));
        return Integer.parseInt(value);
    }

}
