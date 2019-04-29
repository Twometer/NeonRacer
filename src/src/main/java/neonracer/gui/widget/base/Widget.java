package neonracer.gui.widget.base;

import neonracer.gui.GuiContext;
import neonracer.gui.events.Event;
import neonracer.gui.events.EventHandler;
import neonracer.gui.input.MouseState;
import neonracer.gui.util.Alignment;
import neonracer.gui.util.ForeignParameters;
import neonracer.gui.util.Margin;
import neonracer.gui.util.Size;
import neonracer.render.engine.RenderPass;
import org.joml.Vector2f;

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

    private Margin margin = new Margin();

    private Alignment verticalAlignment = Alignment.Fill;

    private Alignment horizontalAlignment = Alignment.Fill;

    private Map<Class<? extends Event>, EventHandler> eventHandlers = new HashMap<>();

    private ForeignParameters foreignParameters = new ForeignParameters();

    public Widget() {
        this.internalId = UUID.randomUUID();
    }

    public void addEventHandler(Class<? extends Event> event, EventHandler handler) {
        eventHandlers.put(event, handler);
    }

    public abstract void draw(GuiContext guiContext, RenderPass renderPass);

    public Size measure() {
        return new Size(width, height);
    }

    public void raiseEvent(Event event) {
        onEvent(event);
        EventHandler handler = eventHandlers.get(event.getClass());
        if (handler != null) handler.handle(event);
    }

    private void onEvent(Event event) {
    }

    public void initialize(GuiContext guiContext) {
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

    public Margin getMargin() {
        return margin;
    }

    public void setMargin(Margin margin) {
        this.margin = margin;
    }

    public Alignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(Alignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public Alignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(Alignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public ForeignParameters getForeignParameters() {
        return foreignParameters;
    }

    protected final boolean isMouseHover(GuiContext context) {
        MouseState state = context.getGameContext().getMouseState();
        Vector2f pos = state.getPosition();
        return pos.x >= x && pos.y >= y && pos.x <= x + width && pos.y <= y + height;
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
