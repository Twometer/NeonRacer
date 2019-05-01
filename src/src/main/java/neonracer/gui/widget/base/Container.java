package neonracer.gui.widget.base;

import neonracer.gui.GuiContext;
import neonracer.gui.events.Event;
import neonracer.render.engine.RenderPass;

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

    public void performLayout() {
        for (Widget widget : children)
            if (widget instanceof Container)
                ((Container) widget).performLayout();
    }

    @Override
    protected void onRaiseEvent(Event event) {
        super.onRaiseEvent(event);
        for (Widget widget : children) {
            widget.raiseEvent(event);
            if (event.isConsumed())
                return;
        }
    }

    @Override
    public void draw(GuiContext guiContext, RenderPass renderPass) {
        for (Widget widget : children)
            widget.draw(guiContext, renderPass);
    }

    @Override
    public void initialize(GuiContext guiContext) {
        super.initialize(guiContext);
        for (Widget widget : children)
            widget.initialize(guiContext);
    }

}
