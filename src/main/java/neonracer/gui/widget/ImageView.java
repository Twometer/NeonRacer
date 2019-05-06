package neonracer.gui.widget;

import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
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
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        if (renderPass != RenderPass.COLOR) return;
        srcTexture.bind();
        renderContext.getPrimitiveRenderer().drawTexturedRect(getX(), getY(), getWidth(), getHeight());
        srcTexture.unbind();
    }

    @Override
    public Size measure() {
        return new Size(srcTexture.getWidth(), srcTexture.getHeight());
    }

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        srcTexture = renderContext.getGameContext().getTextureProvider().getTexture(src);
    }
}
