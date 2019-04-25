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

    private float width;

    private float height;

    BasicButton(FontRenderer fontRenderer, float x, float y, String text) {
        this.fontRenderer = fontRenderer;
        this.position = new Vector2f(x, y);
        this.text = text;
    }

    void draw(RenderContext renderContext, GameContext gameContext) {
        this.width = fontRenderer.getStringWidth(text, 0.35f);
        this.height = fontRenderer.getLineHeight(0.35f);

        Vector2f mouse = gameContext.getMouseState().getPosition();

        boolean hover = mouse.x >= position.x && mouse.y >= position.y && mouse.x <= position.x + width && mouse.y <= position.y + height;
        if (hover) {
            fontRenderer.draw(renderContext, text, position.x, position.y, 0.4f);
        } else {
            fontRenderer.draw(renderContext, text, position.x, position.y, 0.35f);
        }
    }

    boolean click(GameContext gameContext) {
        Vector2f mouse = gameContext.getMouseState().getPosition();
        boolean hover = mouse.x >= position.x && mouse.y >= position.y && mouse.x <= position.x + width && mouse.y <= position.y + height;

        if (onClickListener != null && hover) {
            onClickListener.onClick();
            return true;
        }
        return false;
    }

    void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {

        void onClick();

    }

}
