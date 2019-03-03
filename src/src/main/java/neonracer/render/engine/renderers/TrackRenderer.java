package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.render.RenderContext;
import neonracer.render.gl.shaders.SimpleShader;

public class TrackRenderer implements IRenderer {

    private SimpleShader simpleShader;

    @Override
    public void setup(GameContext context) {
        simpleShader = new SimpleShader();
    }

    @Override
    public void render(RenderContext renderContext, GameContext gameContext) {
        simpleShader.bind();
        simpleShader.setProjectionMatrix(renderContext.getWorldMatrix());
        gameContext.getGameState().getCurrentTrack().getModel().draw();
        simpleShader.unbind();
    }

    @Override
    public void destroy(GameContext context) {
        simpleShader.destroy();
    }
}
