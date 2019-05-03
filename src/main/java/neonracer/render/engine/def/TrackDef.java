package neonracer.render.engine.def;

import neonracer.render.engine.Spline2D;
import neonracer.render.gl.core.Model;

public class TrackDef extends ObjectDef {

    private Model model;

    private Spline2D spline2D;

    TrackDef(Model model, Spline2D spline2D) {
        this.model = model;
        this.spline2D = spline2D;
    }

    public Model getModel() {
        return model;
    }

    public Spline2D getSpline2D() {
        return spline2D;
    }

}
