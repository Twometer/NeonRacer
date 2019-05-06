package neonracer.gui.screen;

import neonracer.gui.annotation.EventHandler;
import neonracer.gui.annotation.LayoutFile;
import neonracer.gui.events.ClickEvent;

@LayoutFile("guis/main.xml")
public class MainScreen extends Screen {

    @EventHandler("BtnStartRace")
    public void onStartRace(ClickEvent event) {
        System.out.println("Starting race");
    }

}
