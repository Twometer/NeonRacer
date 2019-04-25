package neonracer.gui.parser;

import neonracer.gui.annotation.LayoutFile;
import neonracer.gui.screen.Screen;

import java.io.InputStream;

public class ScreenLoader {

    public static <T extends Screen> T loadScreen(Class<T> screenClass) {
        LayoutFile file = screenClass.getAnnotation(LayoutFile.class);
        if (file == null) throw new IllegalArgumentException("The given class does not have a layout file");
        InputStream stream = ScreenLoader.class.getClassLoader().getResourceAsStream(file.value());
        try {
            T result = screenClass.newInstance();
            LayoutParser.fromStream(stream).loadInto(result);
            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
