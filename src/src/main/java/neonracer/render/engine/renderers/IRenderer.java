package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;

public interface IRenderer {

    void setup(GameContext context);

    void render(RenderContext renderContext, GameContext gameContext, RenderPass renderPass);

    void destroy(GameContext context);

}
