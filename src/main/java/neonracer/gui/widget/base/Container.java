package neonracer.gui.widget.base;

import neonracer.gui.events.Event;
import neonracer.render.RenderContext;
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
    public void raiseEvent(Event event) {
        for (Widget widget : children) {
            widget.raiseEvent(event);
            if (event.isConsumed())
                return;
        }
        handleEvent(event);
    }

    @Override
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        for (Widget widget : children)
            widget.draw(renderContext, renderPass);
    }

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        for (Widget widget : children)
            widget.initialize(renderContext);
    }

}
