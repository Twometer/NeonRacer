package neonracer.gui.screen;

import neonracer.gui.widget.base.Container;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import neonracer.render.gl.core.Texture;

import static org.lwjgl.opengl.GL11.*;

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
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        if (background != null && !background.isEmpty())
            this.backgroundTexture = renderContext.getGameContext().getTextureProvider().getTexture(background);
    }

    @Override
    public final void draw(RenderContext renderContext, RenderPass renderPass) {
        if (backgroundTexture != null) {
            if (renderPass == RenderPass.COLOR) {
                backgroundTexture.bind();
                renderContext.getPrimitiveRenderer().drawTexturedRect(getX(), getY(), getWidth(), getHeight());
                backgroundTexture.unbind();
            } else {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            }
        }
        singleWidget().draw(renderContext, renderPass);
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
