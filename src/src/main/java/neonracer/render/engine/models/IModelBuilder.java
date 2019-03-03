package neonracer.render.engine.models;

import neonracer.render.gl.core.Model;

public interface IModelBuilder<T> {

    Model build(T obj);

}
