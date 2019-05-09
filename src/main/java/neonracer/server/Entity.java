package neonracer.server;

class Entity {

    private long id;
    private float x;
    private float y;
    private float rotation;
    private String type;

    Entity(long id, float x, float y, float rotation, String type) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.type = type;
    }

    long getId() {
        return id;
    }

    int getOwnerId() {
        return (int) (id >> 32);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public String getType() {
        return type;
    }

}
