package neonracer.gui.widget;

import neonracer.gui.GuiContext;
import neonracer.gui.util.Color;
import neonracer.gui.widget.base.Container;
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
    public void draw(GuiContext guiContext, RenderPass renderPass) {
        if (renderPass == RenderPass.COLOR)
            guiContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), color.toVector(1.0f));
        super.draw(guiContext, renderPass);
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
