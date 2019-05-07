package neonracer.gui.widget;

import neonracer.gui.events.CharInputEvent;
import neonracer.gui.events.Event;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.Color;
import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;

public class TextBox extends Widget {

    private static final Vector4f BACKGROUND = new Vector4f(0.0f, 0.0509f, 0.1098f, 1.0f);
    private static final Vector4f GLOW = new Vector4f(0.0706f, 0.2314f, 0.5137f, 1.0f);

    private String text = "";

    private FontRenderer fontRenderer;

    private boolean lastPressed;

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
        if (renderPass == RenderPass.COLOR) {
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), BACKGROUND);
            fontRenderer.draw(text, getX(), getY() + 2, getFontSize(), getFontColor().toVector(1.0f));
        } else if (renderPass == RenderPass.GLOW) {
            float glowRadius = isMouseHover() ? 10 : 5;
            renderContext.getPrimitiveRenderer().drawRect(getX() - glowRadius, getY() - glowRadius, getWidth() + glowRadius * 2, getHeight() + glowRadius * 2, GLOW);
            renderContext.getPrimitiveRenderer().drawRect(getX(), getY(), getWidth(), getHeight(), Color.BLACK.toVector());
        }

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
