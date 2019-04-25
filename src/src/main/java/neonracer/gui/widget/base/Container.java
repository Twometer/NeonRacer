package neonracer.gui.widget.base;

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

    public List<Widget> getChildren() {
        return children;
    }

}
