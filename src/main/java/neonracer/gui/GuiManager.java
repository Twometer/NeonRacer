package neonracer.gui;

import neonracer.gui.events.Event;
import neonracer.gui.parser.ScreenLoader;
import neonracer.gui.screen.Screen;
import neonracer.render.RenderContext;
import neonracer.render.engine.RenderPass;

import java.util.ArrayList;
import java.util.List;

public class GuiManager {

    private RenderContext renderContext;

    private List<Screen> currentScreens = new ArrayList<>();

    public GuiManager(RenderContext renderContext) {
        this.renderContext = renderContext;
    }

    public void show(Screen screen) {
        if (!screen.isLoaded()) {
            ScreenLoader.loadScreen(screen);
            screen.initialize(renderContext);
        }
        screen.setWidth(renderContext.getGameContext().getGameWindow().getWidth());
        screen.setHeight(renderContext.getGameContext().getGameWindow().getHeight());
        screen.performLayout();
        currentScreens.add(screen);
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

    public <T extends Screen> T getScreen(Class<T> screenClass) {
        for (Screen screen : currentScreens)
            if (screenClass.isInstance(screen))
                return screenClass.cast(screen);
        return null;
    }

}
