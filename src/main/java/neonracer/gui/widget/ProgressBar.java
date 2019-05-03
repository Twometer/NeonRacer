package neonracer.gui.widget;

import neonracer.gui.widget.base.Widget;
import neonracer.render.RenderContext;
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
    public void draw(RenderContext renderContext, RenderPass renderPass) {
        // TODO
    }

}
