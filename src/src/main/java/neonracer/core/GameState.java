package neonracer.core;

import neonracer.model.track.Track;

public class GameState {

    private Track currentTrack;

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }
}
