package neonracer.gui.widget;

import neonracer.gui.GuiContext;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import org.joml.Vector4f;

public class Button extends Widget {

    @Override
    public void draw(GuiContext guiContext) {
        guiContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    @Override
    public Size measure() {
        return new Size(100, 100);
    }
}
