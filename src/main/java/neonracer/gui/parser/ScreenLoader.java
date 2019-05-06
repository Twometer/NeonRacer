package neonracer.gui.parser;

import neonracer.gui.annotation.LayoutFile;
import neonracer.gui.screen.Screen;

import java.io.InputStream;

public class ScreenLoader {

    public static void loadScreen(Screen screen) {
        if(screen.getChildren().size() > 0) // Already loaded
            return;
        LayoutFile file = screen.getClass().getAnnotation(LayoutFile.class);
        if (file == null) throw new IllegalArgumentException("The given class does not have a layout file");
        InputStream stream = ScreenLoader.class.getClassLoader().getResourceAsStream(file.value());
        LayoutParser.fromStream(stream).loadInto(screen);
    }

    public static <T extends Screen> T loadScreen(Class<T> screenClass) {
        try {
            T screen = screenClass.newInstance();
            loadScreen(screen);
            return screen;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
