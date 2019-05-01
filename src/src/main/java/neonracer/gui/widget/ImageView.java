package neonracer.gui.widget;

import neonracer.gui.GuiContext;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import neonracer.render.engine.RenderPass;
import neonracer.render.gl.core.Texture;

public class ImageView extends Widget {

    private String src;

    private Texture srcTexture;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public void draw(GuiContext guiContext, RenderPass renderPass) {
        if (renderPass != RenderPass.COLOR) return;
        srcTexture.bind();
        guiContext.getPrimitiveRenderer().drawTexturedRect(getX(), getY(), getWidth(), getHeight());
        srcTexture.unbind();
    }

    @Override
    public Size measure() {
        return new Size(srcTexture.getWidth(), srcTexture.getHeight());
    }

    @Override
    public void initialize(GuiContext guiContext) {
        super.initialize(guiContext);
        srcTexture = guiContext.getTextureProvider().getTexture(src);
    }
}
