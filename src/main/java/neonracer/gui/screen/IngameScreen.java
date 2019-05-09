package neonracer.gui.screen;

import neonracer.gui.annotation.BindWidget;
import neonracer.gui.annotation.LayoutFile;
import neonracer.gui.events.Event;
import neonracer.gui.events.TickEvent;
import neonracer.gui.widget.Label;
import neonracer.model.entity.Entity;
import neonracer.model.entity.EntityCar;

@LayoutFile("guis/ingame.xml")
public class IngameScreen extends Screen {

    @BindWidget("lbLap")
    private Label lbLap;

    @BindWidget("lbPlacement")
    private Label lbPlacement;

    @Override
    protected void onEvent(Event event) {
        super.onEvent(event);
        if (event instanceof TickEvent && context.getGameState().getPlayerEntity() != null) {
            StringBuilder placement = new StringBuilder();
            placement.append(context.getGameState().getPlayerEntity().getCarStats().getPlace());
            placement.append("/");
            int totalCars = 0;
            for (Entity entity : context.getGameState().getEntities())
                if (entity instanceof EntityCar)
                    totalCars++;
            placement.append(totalCars);
            lbPlacement.setText(placement.toString());
            lbLap.setText("Runde: " + context.getGameState().getPlayerEntity().getCarStats().getLapsPassed());
        }
    }

}
