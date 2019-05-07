package neonracer.gui.parser;

import neonracer.gui.widget.*;
import neonracer.gui.widget.base.Widget;
import neonracer.gui.widget.grid.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Used by the LayoutParser to convert strings like "Button"
 * to widget objects
 */
public class WidgetFactory {

    private static final List<Class<? extends Widget>> REGISTERED_WIDGETS = new ArrayList<>();

    static {
        register(Label.class);
        register(Button.class);
        register(TitleButton.class);
        register(TextBox.class);
        register(ProgressBar.class);
        register(ImageView.class);
        register(ImageButton.class);
        register(Grid.class);
        register(Border.class);
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
