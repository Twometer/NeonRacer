package neonracer.gui;

import neonracer.core.GameContext;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.util.PrimitiveRenderer;
import neonracer.render.RenderContext;
import neonracer.render.gl.TextureProvider;

public class GuiContext {

    private GameContext gameContext;

    private RenderContext renderContext;

    private FontRenderer fontRenderer;

    private PrimitiveRenderer primitiveRenderer;

    public GuiContext(GameContext gameContext, RenderContext renderContext, FontRenderer fontRenderer, PrimitiveRenderer primitiveRenderer) {
        this.gameContext = gameContext;
        this.renderContext = renderContext;
        this.fontRenderer = fontRenderer;
        this.primitiveRenderer = primitiveRenderer;
    }

    public GameContext getGameContext() {
        return gameContext;
    }

    public RenderContext getRenderContext() {
        return renderContext;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public PrimitiveRenderer getPrimitiveRenderer() {
        return primitiveRenderer;
    }

    public TextureProvider getTextureProvider() {
        return gameContext.getTextureProvider();
    }

}
