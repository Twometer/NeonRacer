package neonracer.gui.widget;

import neonracer.gui.util.Color;
import neonracer.gui.widget.base.Container;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;

public class Border extends Container {

    private Color color = Color.WHITE;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        if (renderPass == RenderPass.COLOR)
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), color.toVector(1.0f));
        super.draw(renderContext, renderPass);
    }

    @Override
    public void performLayout() {
        checkSingleWidget();
        if (children.size() > 0) {
            Widget singleChild = children.get(0);
            singleChild.setX(getX());
            singleChild.setY(getY());
            singleChild.setWidth(getWidth());
            singleChild.setHeight(getHeight());
        }
        super.performLayout();
    }

    private void checkSingleWidget() {
        if (children.size() > 1)
            throw new IllegalStateException("Borders are not a layout manager and can therefore only house one child");
    }

}
