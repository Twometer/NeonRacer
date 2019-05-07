package neonracer.gui.widget;

import neonracer.gui.util.Color;
import neonracer.gui.widget.base.Container;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import org.joml.Vector4f;

public class Border extends Container {

    private static final Vector4f BACKGROUND = new Vector4f(0.0f, 0.0509f, 0.1098f, 1.0f);
    private static final Vector4f GLOW = new Vector4f(0.0706f, 0.2314f, 0.5137f, 1.0f);

    private static final int GLOW_RADIUS = 3;

    private Color color = Color.WHITE;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        if (renderPass == RenderPass.COLOR) {
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), BACKGROUND);
        } else if (renderPass == RenderPass.GLOW) {
            renderContext.getPrimitiveRenderer().drawRect(getX() - GLOW_RADIUS, getY() - GLOW_RADIUS, getWidth() + GLOW_RADIUS * 2, getHeight() + GLOW_RADIUS * 2, GLOW);
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), Color.BLACK.toVector());
        }
        super.draw(renderContext, renderPass);
    }

    @Override
    public void performLayout() {
        checkSingleWidget();
        if (children.size() > 0) {
            Widget singleChild = children.get(0);
            singleChild.setX(getX() + singleChild.getMargin().getLeft());
            singleChild.setY(getY() + singleChild.getMargin().getTop());
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
