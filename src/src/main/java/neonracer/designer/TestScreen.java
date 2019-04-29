package neonracer.designer;

import neonracer.gui.annotation.EventHandler;
import neonracer.gui.annotation.LayoutFile;
import neonracer.gui.events.ClickEvent;
import neonracer.gui.screen.Screen;

@LayoutFile("guis/main.xml")
public class TestScreen extends Screen {

    @EventHandler("BtnStartRace")
    public void onStartRace(ClickEvent event) {
        System.out.println("Starting race");
    }

}
