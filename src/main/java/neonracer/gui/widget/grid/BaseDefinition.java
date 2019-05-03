package neonracer.gui.widget.grid;

abstract class BaseDefinition {

    private int value;

    private Mode mode;

    BaseDefinition(String str) {
        if (str.endsWith("%")) {
            this.value = Integer.parseInt(str.substring(0, str.length() - 1));
            this.mode = Mode.Relative;
        } else {
            this.value = Integer.parseInt(str);
            this.mode = Mode.Absolute;
        }
    }

    public int getValue() {
        return value;
    }

    Mode getMode() {
        return mode;
    }

    public enum Mode {
        Relative,
        Absolute
    }

}
