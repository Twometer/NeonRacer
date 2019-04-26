package neonracer.gui.widget.base;

import neonracer.core.GameContext;
import neonracer.gui.GuiContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Container extends Widget {

    protected List<Widget> children = new ArrayList<>();

    public Widget getChildById(String id) {
        List<Widget> allWidgets = scanWidgets(this);
        for (Widget widget : allWidgets)
            if (Objects.equals(widget.getId(), id))
                return widget;
        return null;
    }

    private List<Widget> scanWidgets(Widget parent) {
        List<Widget> widgets = new ArrayList<>();
        if (parent instanceof Container) {
            for (Widget widget : ((Container) parent).getChildren()) {
                widgets.add(widget);
                widgets.addAll(scanWidgets(widget));
            }
        }
        return widgets;
    }

    public void addChild(Widget widget) {
        children.add(widget);
    }

    protected List<Widget> getChildren() {
        return children;
    }

    public void performLayout() {
        for (Widget widget : children)
            if (widget instanceof Container)
                ((Container) widget).performLayout();
    }

    @Override
    public void draw(GuiContext guiContext) {
        for (Widget widget : children)
            widget.draw(guiContext);
    }

    @Override
    public void initialize(GameContext gameContext) {
        for (Widget widget : children)
            widget.initialize(gameContext);
    }

}
