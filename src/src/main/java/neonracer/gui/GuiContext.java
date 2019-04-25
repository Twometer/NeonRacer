package neonracer.gui;

import neonracer.gui.font.FontRenderer;
import neonracer.gui.input.KeyboardState;
import neonracer.gui.input.MouseState;
import neonracer.render.RenderContext;

public class GuiContext {

    private KeyboardState keyboardState;

    private MouseState mouseState;

    private RenderContext renderContext;

    private FontRenderer fontRenderer;

    public GuiContext(KeyboardState keyboardState, MouseState mouseState, RenderContext renderContext, FontRenderer fontRenderer) {
        this.keyboardState = keyboardState;
        this.mouseState = mouseState;
        this.renderContext = renderContext;
        this.fontRenderer = fontRenderer;
    }

    public KeyboardState getKeyboardState() {
        return keyboardState;
    }

    public MouseState getMouseState() {
        return mouseState;
    }

    public RenderContext getRenderContext() {
        return renderContext;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

}
