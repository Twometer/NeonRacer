package neonracer.render.engine.models;

public class ModelBuilderFactory {

    public static <T> IModelBuilder<T> getModelBuilder(ModelType modelType) {
        if (modelType == ModelType.TRACK)
            return (IModelBuilder<T>) new TrackModelBuilder();
        throw new IllegalArgumentException();
    }

}
