package neonracer.gui.events;

public class CharInputEvent extends Event {

    private char chr;

    public CharInputEvent(char chr) {
        this.chr = chr;
    }

    public char getChar() {
        return chr;
    }

}
