package neonracer.render.engine.renderers;

import neonracer.core.GameContext;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;

public interface IRenderer {

    void setup(RenderContext renderContext, GameContext gameContext);

    void render(RenderContext renderContext, GameContext gameContext, RenderPass renderPass);

    void destroy(RenderContext renderContext, GameContext gameContext);

}
