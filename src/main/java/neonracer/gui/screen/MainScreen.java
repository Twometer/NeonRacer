package neonracer.gui.screen;

import neonracer.gui.annotation.EventHandler;
import neonracer.gui.annotation.LayoutFile;
import neonracer.gui.events.ClickEvent;
import neonracer.network.proto.Race;

import javax.swing.*;

@LayoutFile("guis/main.xml")
public class MainScreen extends Screen {

    @EventHandler("btnStartRace")
    public void onStartRace(ClickEvent event) {
        parent.close(this);
        parent.show(new CarSelectorScreen());
        // Send packet after showing new window to bind event handler before receiving a response packet
        context.getClient().send(Race.Join.newBuilder().build());
    }

    @EventHandler("btnExitGame")
    public void onExitGame(ClickEvent event) {
        if (JOptionPane.showConfirmDialog(null, "Willst du das Spiel wirklich beenden?", "Spiel beenden", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            System.exit(0);
    }

}
