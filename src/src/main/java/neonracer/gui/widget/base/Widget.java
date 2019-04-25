package neonracer.gui.widget.base;

import neonracer.gui.GuiContext;
import neonracer.gui.events.Event;
import neonracer.gui.events.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Widget {

    private String id;

    private UUID internalId;

    private int x;

    private int y;

    private int width;

    private int height;

    private int padding;

    private Map<Class<? extends Event>, EventHandler> eventHandlers = new HashMap<>();

    private Map<String, String> foreignParameters = new HashMap<>();

    public Widget() {
        this.internalId = UUID.randomUUID();
    }

    public void addEventHandler(Class<? extends Event> event, EventHandler handler) {
        eventHandlers.put(event, handler);
    }

    public abstract void draw(GuiContext guiContext);

    public abstract void performLayout();

    public void raiseEvent(Event event) {
        onEvent(event);
        EventHandler handler = eventHandlers.get(event.getClass());
        if (handler != null) handler.handle(event);
    }

    public void onEvent(Event event) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public Map<String, String> getForeignParameters() {
        return foreignParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Widget widget = (Widget) o;
        return internalId.equals(widget.internalId);
    }

    @Override
    public int hashCode() {
        return internalId.hashCode();
    }

}
