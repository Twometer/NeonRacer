package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.render.RenderContext;

public interface IRenderer {

    void setup(GameContext context);

    void render(RenderContext renderContext, GameContext gameContext);

    void destroy(GameContext context);

}
