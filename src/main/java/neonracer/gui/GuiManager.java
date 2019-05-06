package neonracer.gui;

import neonracer.gui.events.Event;
import neonracer.gui.parser.ScreenLoader;
import neonracer.gui.screen.Screen;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;

import java.util.HashMap;
import java.util.Map;

public class GuiManager {

    private RenderContext renderContext;

    private Map<Class<? extends Screen>, Screen> cache = new HashMap<>();

    private Screen currentScreen;

    public GuiManager(RenderContext renderContext) {
        this.renderContext = renderContext;
    }

    public void show(Class<? extends Screen> screenClass) {
        Screen screen = cache.containsKey(screenClass) ? cache.get(screenClass) : ScreenLoader.loadScreen(screenClass);
        cache.put(screenClass, screen);
        show(screen);
    }

    public void show(Screen screen) {
        ScreenLoader.loadScreen(screen);
        screen.setWidth(renderContext.getGameContext().getGameWindow().getWidth());
        screen.setHeight(renderContext.getGameContext().getGameWindow().getHeight());
        screen.initialize(renderContext);
        screen.performLayout();
        currentScreen = screen;
    }

    public void draw(RenderPass renderPass) {
        if (currentScreen != null)
            currentScreen.draw(renderContext, renderPass);
    }

    public void resize(int width, int height) {
        if (currentScreen != null) {
            currentScreen.setWidth(width);
            currentScreen.setHeight(height);
            currentScreen.performLayout();
        }
    }

    public void raiseEvent(Event event) {
        if (currentScreen != null)
            currentScreen.raiseEvent(event);
    }

}
