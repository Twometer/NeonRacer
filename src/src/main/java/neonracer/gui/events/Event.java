package neonracer.gui.events;

public abstract class Event {

    private boolean consumed;

    public void consume() {
        this.consumed = true;
    }

    public boolean isConsumed() {
        return consumed;
    }
}
