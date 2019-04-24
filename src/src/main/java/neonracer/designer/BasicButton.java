package neonracer.designer;

import neonracer.core.GameContext;
import neonracer.render.RenderContext;
import neonracer.render.engine.font.FontRenderer;
import org.joml.Vector2f;

class BasicButton {

    private FontRenderer fontRenderer;

    private Vector2f position;

    private String text;

    private OnClickListener onClickListener;

    private boolean pressed;

    BasicButton(FontRenderer fontRenderer, float x, float y, String text) {
        this.fontRenderer = fontRenderer;
        this.position = new Vector2f(x, y);
        this.text = text;
    }

    void draw(RenderContext renderContext, GameContext gameContext) {
        float width = fontRenderer.getStringWidth(text, 0.35f);
        float height = fontRenderer.getLineHeight(0.35f);

        Vector2f mouse = gameContext.getMouseState().getPosition();

        boolean hover = mouse.x >= position.x && mouse.y >= position.y && mouse.x <= position.x + width && mouse.y <= position.y + height;
        if (hover) {
            fontRenderer.draw(renderContext, text, position.x, position.y, 0.4f);
        } else {
            fontRenderer.draw(renderContext, text, position.x, position.y, 0.35f);
        }

        if (gameContext.getMouseState().isLeft() && hover) {
            if (!pressed && onClickListener != null) onClickListener.onClick();
            pressed = true;
        }
        if (!gameContext.getMouseState().isLeft()) pressed = false;

    }

    void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {

        void onClick();

    }

}
