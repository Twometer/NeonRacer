package neonracer.gui.widget;

import neonracer.gui.GuiContext;
import neonracer.gui.widget.base.Container;
import org.joml.Vector4f;

public class Border extends Container {

    @Override
    public void draw(GuiContext guiContext) {
        guiContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		super.draw(guiContext);
    }

    @Override
    public void performLayout() {
        checkSingleWidget();
        super.performLayout();
    }

    private void checkSingleWidget() {
        if (children.size() > 1)
            throw new IllegalStateException("Borders are not a layout manager and can therefore only house one child");
    }

}
