package neonracer.gui.screen;

import neonracer.gui.GuiContext;
import neonracer.gui.widget.base.Container;
import neonracer.gui.widget.base.Widget;
import neonracer.render.engine.RenderPass;
import neonracer.render.gl.core.Texture;

public abstract class Screen extends Container {

    private Texture backgroundTexture;

    private String background;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Override
    public void initialize(GuiContext guiContext) {
        super.initialize(guiContext);
        if (background != null && !background.isEmpty())
            this.backgroundTexture = guiContext.getTextureProvider().getTexture(background);
    }

    @Override
    public final void draw(GuiContext guiContext, RenderPass renderPass) {
        if (renderPass == RenderPass.COLOR && backgroundTexture != null) {
            backgroundTexture.bind();
            guiContext.getPrimitiveRenderer().drawTexturedRect(getX(), getY(), getWidth(), getHeight());
            backgroundTexture.unbind();
        }
        singleWidget().draw(guiContext, renderPass);
    }

    @Override
    public final void performLayout() {
        Widget singleWidget = singleWidget();
        singleWidget.setWidth(getWidth());
        singleWidget.setHeight(getHeight());
        if (singleWidget instanceof Container)
            ((Container) singleWidget).performLayout();
    }

    private Widget singleWidget() {
        if (children.size() > 1)
            throw new IllegalStateException("Screen only supports one child widget. Use a layout manager for more children.");
        return children.get(0);
    }

}
