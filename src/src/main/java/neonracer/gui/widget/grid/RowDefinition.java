package neonracer.gui.widget.grid;

import neonracer.gui.annotation.ParserMethod;

public class RowDefinition extends BaseDefinition {

    static final RowDefinition AUTO = new RowDefinition("-1");
    static final RowDefinition FILL_REMAINING = new RowDefinition("-2");

    private RowDefinition(String str) {
        super(str);
    }

    @ParserMethod
    public static RowDefinition fromString(String str) {
        if (str.equalsIgnoreCase("auto")) return AUTO;
        else if (str.equalsIgnoreCase("*")) return FILL_REMAINING;
        else return new RowDefinition(str);
    }

}
