package neonracer.gui.widget.base;

import neonracer.gui.events.ClickEvent;
import neonracer.gui.events.Event;
import neonracer.gui.events.EventHandler;
import neonracer.gui.font.FontFamily;
import neonracer.gui.font.FontRenderer;
import neonracer.gui.input.MouseState;
import neonracer.gui.util.*;
import neonracer.render.RenderContext;
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

    private FontFamily fontFamily = FontFamily.Content;

    private Color fontColor = Color.WHITE;

    private float fontSize = 1.0f;

    private Map<Class<? extends Event>, EventHandler> eventHandlers = new HashMap<>();

    private ForeignParameters foreignParameters = new ForeignParameters();

    private MouseState mouseState;

    public Widget() {
        this.internalId = UUID.randomUUID();
    }

    public void putEventHandler(Class<? extends Event> event, EventHandler handler) {
        eventHandlers.put(event, handler);
    }

    public void removeEventHandler(Class<? extends Event> event) {
        eventHandlers.remove(event);
    }

    public abstract void draw(RenderContext renderContext, RenderPass renderPass);

    public Size measure() {
        return new Size(width, height);
    }

    public void raiseEvent(Event event) {
        if (event instanceof ClickEvent)
            if (isMouseHover())
                event.consume();
            else return;
        handleEvent(event);
    }

    void handleEvent(Event event) {
        onEvent(event);
        EventHandler handler = eventHandlers.get(event.getClass());
        if (handler != null) handler.handle(event);
    }

    protected void onEvent(Event event) {
    }

    protected FontRenderer getFontRenderer(RenderContext renderContext) {
        return renderContext.getFonts().get(fontFamily);
    }

    public void initialize(RenderContext renderContext) {
        mouseState = renderContext.getGameContext().getMouseState();
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

    public FontFamily getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(FontFamily fontFamily) {
        this.fontFamily = fontFamily;
    }

    protected Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    protected float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public ForeignParameters getForeignParameters() {
        return foreignParameters;
    }

    protected final boolean isMouseHover() {
        Vector2f pos = mouseState.getPosition();
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
