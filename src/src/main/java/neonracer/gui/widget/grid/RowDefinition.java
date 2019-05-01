package neonracer.gui.widget.grid;

import neonracer.gui.annotation.ParserMethod;

public class RowDefinition {

    static final RowDefinition AUTO = new RowDefinition(-1);
    static final RowDefinition FILL_REMAINING = new RowDefinition(-2);

    private int value;

    RowDefinition(int value) {
        this.value = value;
    }

    @ParserMethod
    public static RowDefinition fromString(String str) {
        if (str.equalsIgnoreCase("auto")) return AUTO;
        else if (str.equalsIgnoreCase("*")) return FILL_REMAINING;
        else return new RowDefinition(Integer.parseInt(str));
    }

    int getValue() {
        return value;
    }

}
