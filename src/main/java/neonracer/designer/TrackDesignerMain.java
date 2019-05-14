package neonracer.designer;

import neonracer.gui.util.SwingUtil;

import java.io.IOException;

public class TrackDesignerMain {

    public static void main(String[] args) throws IOException {
        SwingUtil.applyNativeLookAndFeel();

        TrackDesigner trackDesigner = new TrackDesigner();
        trackDesigner.start();
    }

}
