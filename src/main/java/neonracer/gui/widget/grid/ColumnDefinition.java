package neonracer.gui.widget.grid;

import neonracer.gui.annotation.ParserMethod;

public class ColumnDefinition extends BaseDefinition {

    static final ColumnDefinition AUTO = new ColumnDefinition("-1");
    static final ColumnDefinition FILL_REMAINING = new ColumnDefinition("-2");

    private ColumnDefinition(String str) {
        super(str);
    }

    @ParserMethod
    public static ColumnDefinition fromString(String str) {
        if (str.equalsIgnoreCase("auto")) return AUTO;
        else if (str.equalsIgnoreCase("*")) return FILL_REMAINING;
        else return new ColumnDefinition(str);
    }

}
