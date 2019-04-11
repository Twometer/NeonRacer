package neonracer.phys;

import org.jbox2d.dynamics.Body;

public class EntityPhysics {

    private Body body;

    public EntityPhysics(Body body) {
        this.body = body;
    }

    Body getBody() {
        return body;
    }

}
