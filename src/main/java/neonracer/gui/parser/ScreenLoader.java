package neonracer.gui.parser;

import neonracer.gui.annotation.LayoutFile;
import neonracer.gui.screen.Screen;

import java.io.InputStream;

public class ScreenLoader {

    public static void loadScreen(Screen screen) {
        if (screen.isLoaded())
            return;
        LayoutFile file = screen.getClass().getAnnotation(LayoutFile.class);
        if (file == null) throw new IllegalArgumentException("The given class does not have a layout file");
        InputStream stream = ScreenLoader.class.getClassLoader().getResourceAsStream(file.value());
        LayoutParser.fromStream(stream).loadInto(screen);
        screen.setLoaded(true);
    }
}
