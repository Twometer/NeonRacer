package neonracer.gui.screen;

import neonracer.gui.annotation.BindWidget;
import neonracer.gui.annotation.LayoutFile;
import neonracer.gui.events.Event;
import neonracer.gui.events.TickEvent;
import neonracer.gui.widget.Label;
import neonracer.gui.widget.ProgressBar;
import neonracer.network.proto.Race;
import neonracer.render.RenderContext;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@LayoutFile("guis/cars.xml")
public class CarSelectorScreen extends Screen {

    private long totalWaitMs = 0;

    private long startMs = 0;

    @BindWidget("lbState")
    private Label lbState;

    @BindWidget("pbState")
    private ProgressBar pbState;

    @Override
    public void initialize(RenderContext renderContext) {
        super.initialize(renderContext);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onEvent(Event event) {
        super.onEvent(event);
        if (event instanceof TickEvent) {
            if (startMs == 0) {
                lbState.setText("Warte auf Spieler...");
                pbState.setValue(100);
            } else {
                long remaining = System.currentTimeMillis() - startMs;
                lbState.setText("Noch " + (remaining / 1000) + " Sekunden");
                pbState.setValue((int) (((float) remaining / totalWaitMs) * 100));
            }
        }
    }

    @Subscribe
    public void onStart(Race.Start start) {
        if (start.getElapsedMilliseconds() < 0) {
            totalWaitMs = -start.getElapsedMilliseconds();
            startMs = System.currentTimeMillis() + totalWaitMs;
        } else {
            parent.close(this);
        }
    }

}
