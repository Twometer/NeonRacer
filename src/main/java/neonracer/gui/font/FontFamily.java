package neonracer.gui.font;

import neonracer.gui.annotation.ParserMethod;

public enum FontFamily {

    Title("redthinker"),
    Content("lucida");

    private String fileName;

    FontFamily(String fileName) {
        this.fileName = fileName;
    }

    @ParserMethod
    public static FontFamily parse(String str) {
        for (FontFamily fontFamily : FontFamily.values())
            if (fontFamily.name().equalsIgnoreCase(str))
                return fontFamily;
        throw new IllegalArgumentException("Unknown font family " + str);
    }

    public String getFileName() {
        return fileName;
    }

}
