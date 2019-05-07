package neonracer.gui.widget;

import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Animator;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import org.joml.Vector4f;

public class Button extends Widget {

    private static final Vector4f BACKGROUND = new Vector4f(0.0f, 0.0509f, 0.1098f, 1.0f);
    private static final Vector4f GLOW = new Vector4f(BACKGROUND).mul(2.0f);

    private String text;

    private FontRenderer fontRenderer;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private Animator opacityAnimator = new Animator(0.05f, 0.0f, 1.0f);

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        fontRenderer = getFontRenderer(renderContext);
    }

    @Override
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        FontRenderer fontRenderer = getFontRenderer(renderContext);

        opacityAnimator.update(isMouseHover());

        BACKGROUND.w = renderPass == RenderPass.COLOR ? 1.0f : 0.2f;
        renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), BACKGROUND);

        if (renderPass == RenderPass.GLOW && opacityAnimator.getValue() > 0) {
            GLOW.w = opacityAnimator.getValue();
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), GLOW);
        }
        float width = fontRenderer.getStringWidth(text, getFontSize());
        float height = fontRenderer.getStringHeight(text, getFontSize());
        fontRenderer.draw(text, getX() + getWidth() / 2f - width / 2f, getY() + getHeight() / 2f - height / 2f, getFontSize(), getFontColor().toVector(renderPass == RenderPass.GLOW ? 0.3f : 1.0f));
    }

    @Override
    public Size measure() {
        return new Size((int) fontRenderer.getStringWidth(text, getFontSize()), (int) fontRenderer.getStringHeight(text, getFontSize()) + 10);
    }

}
