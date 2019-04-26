package neonracer.gui.widget;

import neonracer.core.GameContext;
import neonracer.gui.GuiContext;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
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
    public void draw(GuiContext guiContext) {
        srcTexture.bind();
        guiContext.getPrimitiveRenderer().drawTexturedRect(getX(), getY(), getWidth(), getHeight());
        srcTexture.unbind();
    }

    @Override
    public Size measure() {
        return new Size(srcTexture.getWidth(), srcTexture.getHeight());
    }

    @Override
    public void initialize(GameContext gameContext) {
        srcTexture = gameContext.getTextureProvider().getTexture(src);
    }
}
