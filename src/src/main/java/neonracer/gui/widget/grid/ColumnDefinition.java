package neonracer.gui.widget.grid;

import neonracer.gui.annotation.ParserMethod;

public class ColumnDefinition {

    static final ColumnDefinition AUTO = new ColumnDefinition(-1);
    static final ColumnDefinition FILL_REMAINING = new ColumnDefinition(-2);

    private int value;

    ColumnDefinition(int value) {
        this.value = value;
    }

    @ParserMethod
    public static ColumnDefinition fromString(String str) {
        if (str.equalsIgnoreCase("auto")) return AUTO;
        else if (str.equalsIgnoreCase("*")) return FILL_REMAINING;
        else return new ColumnDefinition(Integer.parseInt(str));
    }

    int getValue() {
        return value;
    }

}
