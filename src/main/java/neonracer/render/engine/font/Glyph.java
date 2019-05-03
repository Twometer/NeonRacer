package neonracer.render.engine.font;

class Glyph {

    private int id;

    private int x;

    private int y;

    private int width;

    private int height;

    private int xOffset;

    private int yOffset;

    private int advance;

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    int getX() {
        return x;
    }

    void setX(int x) {
        this.x = x;
    }

    int getY() {
        return y;
    }

    void setY(int y) {
        this.y = y;
    }

    int getWidth() {
        return width;
    }

    void setWidth(int width) {
        this.width = width;
    }

    int getHeight() {
        return height;
    }

    void setHeight(int height) {
        this.height = height;
    }

    int getxOffset() {
        return xOffset;
    }

    void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    int getyOffset() {
        return yOffset;
    }

    void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    int getAdvance() {
        return advance;
    }

    void setAdvance(int advance) {
        this.advance = advance;
    }
}
