package neonracer.render.engine.def;

import neonracer.model.track.Track;

public class DefBuilderFactory {

    public static IDefBuilder<Track, TrackDef> trackDefBuilder() {
        return new TrackDefBuilder();
    }

}
