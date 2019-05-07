package neonracer.gui.widget;

import neonracer.gui.events.CharInputEvent;
import neonracer.gui.events.Event;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Animator;
import neonracer.gui.util.Color;
import neonracer.gui.util.Size;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;

public class TextBox extends Widget {

    private static final Vector4f BACKGROUND = new Vector4f(0.0f, 0.0509f, 0.1098f, 1.0f);
    private static final Vector4f GLOW = new Vector4f(0.0706f, 0.2314f, 0.5137f, 1.0f);

    private String text = "";

    private FontRenderer fontRenderer;

    private boolean lastPressed;

    private float cursorOpacity;

    private Animator glowAnimator = new Animator(0.4f, 5.0f, 8.0f);

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        fontRenderer = getFontRenderer(renderContext);
    }

    @Override
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        glowAnimator.update(isMouseHover());

        if (renderPass == RenderPass.COLOR) {
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), BACKGROUND);
        } else if (renderPass == RenderPass.GLOW) {
            renderContext.getPrimitiveRenderer().drawRect(getX() - glowAnimator.getValue(), getY() - glowAnimator.getValue(), getWidth() + glowAnimator.getValue() * 2, getHeight() + glowAnimator.getValue() * 2, GLOW);
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), Color.BLACK.toVector());
        }

        cursorOpacity += 0.03f;


        // Draw centered text
        Size textSize = fontRenderer.drawCentered(getText(), getX() + getWidth() / 2, getY() + getHeight() / 2, getFontSize(), getFontColor().toVector(renderPass == RenderPass.GLOW ? 0.3f : 1.0f));

        // Draw a cursor
        renderContext.getPrimitiveRenderer().drawRect(getX() + getWidth() / 2f + textSize.getWidth() / 2f, getY() + 8, 1.0f, getHeight() - 16, getFontColor().toVector((float) Math.abs(Math.sin(cursorOpacity))));

        if (renderContext.getGameContext().getGameWindow().isKeyPressed(GLFW_KEY_BACKSPACE) && !text.isEmpty()) {
            if (!lastPressed) text = text.substring(0, text.length() - 1);
            lastPressed = true;
        } else lastPressed = false;
    }

    @Override
    protected void onEvent(Event event) {
        super.onEvent(event);
        if (event instanceof CharInputEvent)
            text += ((CharInputEvent) event).getChar();
    }

}

