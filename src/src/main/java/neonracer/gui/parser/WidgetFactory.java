package neonracer.gui.parser;

import neonracer.gui.widget.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WidgetFactory {

    private static final List<Class<? extends Widget>> REGISTERED_WIDGETS = new ArrayList<>();

    static {
        // TODO register some widgets here
    }

    private WidgetFactory() {
    }

    private static void register(Class<? extends Widget> widgetClass) {
        REGISTERED_WIDGETS.add(widgetClass);
    }

    public static Widget create(String name) {
        for (Class<? extends Widget> widget : REGISTERED_WIDGETS) {
            if (Objects.equals(widget.getSimpleName(), name)) {
                try {
                    return widget.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new IllegalArgumentException("Unknown widget " + name);
    }

}
