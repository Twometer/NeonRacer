package neonracer.gui.screen;

import neonracer.gui.annotation.LayoutFile;
import neonracer.render.RenderContext;

@LayoutFile("guis/cars.xml")
public class CarSelectorScreen extends Screen {

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            parent.close(this);
        }).start();
    }

}
