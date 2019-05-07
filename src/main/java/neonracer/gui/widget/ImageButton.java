package neonracer.gui.widget;

import neonracer.gui.util.Animator;
import neonracer.gui.util.Color;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import neonracer.render.gl.core.Texture;
import org.joml.Vector4f;

public class ImageButton extends Widget {

    private static final Vector4f BACKGROUND = new Vector4f(0.0f, 0.0509f, 0.1098f, 1.0f);
    private static final Vector4f GLOW = new Vector4f(0.0706f, 0.2314f, 0.5137f, 1.0f);

    private String src;

    private Texture texture;

    private Animator glowAnimator = new Animator(0.4f, 5.0f, 8.0f);

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        texture = renderContext.getGameContext().getTextureProvider().getTexture(src);
    }

    @Override
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        glowAnimator.update(isMouseHover());

        if (renderPass == RenderPass.COLOR) {
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), BACKGROUND);
            texture.bind();
            float scaledHeight = getHeight() - 10f;
            float scaledWidth = texture.getWidth() / (float) texture.getHeight() * scaledHeight;
            renderContext.getPrimitiveRenderer().drawTexturedRect(getX() + getWidth() / 2f - scaledWidth / 2f, getY() + getHeight() / 2f - scaledHeight / 2f, scaledWidth, scaledHeight);
            texture.unbind();
        } else if (renderPass == RenderPass.GLOW) {
            renderContext.getPrimitiveRenderer().drawRect(getX() - glowAnimator.getValue(), getY() - glowAnimator.getValue(), getWidth() + glowAnimator.getValue() * 2, getHeight() + glowAnimator.getValue() * 2, GLOW);
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), Color.BLACK.toVector());
        }
    }


    //@Override
    //p ublic Size measure() {
    //return new Size(texture.getWidth(), texture.getHeight());
    // }

}

