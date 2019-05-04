package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;
import neonracer.render.gl.core.Texture;
import neonracer.render.gl.shaders.WorldShader;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class TrackRenderer implements IRenderer {

    private WorldShader worldShader;

    @Override
    public void setup(RenderContext renderContext, GameContext gameContext) {
        worldShader = new WorldShader();
    }

    @Override
    public void render(RenderContext renderContext, GameContext gameContext, RenderPass renderPass) {
        if (renderPass != RenderPass.COLOR) return;
        if (gameContext.getGameState().getCurrentTrack() == null) return;
        worldShader.bind();
        worldShader.setProjectionMatrix(renderContext.getWorldMatrix());
        Texture texture = gameContext.getGameState().getCurrentTrack().getBaseMaterial().getTexture();
        glActiveTexture(GL_TEXTURE0);
        texture.bind();
        gameContext.getGameState().getCurrentTrack().getTrackDef().getModel().draw();
        texture.unbind();
        worldShader.unbind();
    }

    @Override
    public void destroy(RenderContext renderContext, GameContext gameContext) {
        worldShader.destroy();
    }
}
