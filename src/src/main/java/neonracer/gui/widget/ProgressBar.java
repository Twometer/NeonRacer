package neonracer.gui.widget;

import neonracer.gui.GuiContext;
import neonracer.gui.widget.base.Widget;
import neonracer.render.engine.RenderPass;

public class ProgressBar extends Widget {

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void draw(GuiContext guiContext, RenderPass renderPass) {
        // TODO
    }

}
