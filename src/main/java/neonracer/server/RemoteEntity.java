package neonracer.server;

class RemoteEntity {

    private long id;
    private float x, y, rotation;
    private String type;

    RemoteEntity(long id, float x, float y, float rotation, String type) {
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

}
