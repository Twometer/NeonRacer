package neonracer.gui;

import neonracer.gui.events.Event;
import neonracer.gui.parser.ScreenLoader;
import neonracer.gui.screen.Screen;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GuiManager {

    private final RenderContext renderContext;

    private final List<Screen> currentScreens = new CopyOnWriteArrayList<>();

    public GuiManager(RenderContext renderContext) {
        this.renderContext = renderContext;
    }

    public void show(Screen screen) {
        screen.setParent(this);
        screen.setContext(renderContext.getGameContext());
        if (!screen.isLoaded()) {
            ScreenLoader.loadScreen(screen);
            screen.initialize(renderContext);
        }
        screen.setWidth(renderContext.getGameContext().getGameWindow().getWidth());
        screen.setHeight(renderContext.getGameContext().getGameWindow().getHeight());
        screen.performLayout();
        currentScreens.add(screen);
        screen.initialized();
    }

    public void close(Screen screen) {
        currentScreens.remove(screen);
    }

    public void draw(RenderPass renderPass) {
        for (Screen screen : currentScreens)
            screen.draw(renderContext, renderPass);
    }

    public void resize(int width, int height) {
        for (Screen screen : currentScreens) {
            screen.setWidth(width);
            screen.setHeight(height);
            screen.performLayout();
        }
    }

    public void raiseEvent(Event event) {
        for (Screen screen : currentScreens)
            screen.raiseEvent(event);
    }
}
