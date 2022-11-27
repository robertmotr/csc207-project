import Map.GoogleMapsGui;

import javax.swing.*;

/**
 * Main class used to store the main method responsible for launching the application.
 *
 */

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GoogleMapsGui mapsGui = GoogleMapsGui.getInstance();
            JScrollPane p = mapsGui.getCanvas();
            JFrame f = new JFrame();
            f.setContentPane(p);
            f.setSize(400, 300);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }
}
