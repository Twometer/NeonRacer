package neonracer.gui.screen;

import neonracer.gui.GuiContext;
import neonracer.gui.widget.base.Container;
import neonracer.gui.widget.base.Widget;

public abstract class Screen extends Container {

    @Override
    public final void draw(GuiContext guiContext) {
        singleWidget().draw(guiContext);
    }

    @Override
    public final void performLayout() {
        Widget singleWidget = singleWidget();
        singleWidget.setX(getPadding());
        singleWidget.setY(getPadding());
        singleWidget.setWidth(getWidth() - getPadding());
        singleWidget.setHeight(getHeight() - getPadding());
        if (singleWidget instanceof Container)
            ((Container) singleWidget).performLayout();
    }

    private Widget singleWidget() {
        if (children.size() > 1)
            throw new IllegalStateException("Screen only supports one child widget. Use a layout manager for more children.");
        return children.get(0);
    }

}
