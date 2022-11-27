package Map; /**
 * A singleton map of UTSG campus
 *
 * Reference from:
 * https://stackoverflow.com/questions/1993981/how-to-access-google-maps-api-in-java-application
 * https://www.tutorialspoint.com/java-program-to-display-a-webpage-in-jeditorpane
 * https://stackoverflow.com/questions/7298817/making-image-scrollable-in-jframe-contentpane/7299067
 * https://www.comm.utoronto.ca/~valaee/University%20of%20Toronto%20-%20St_%20George%20Campus%20Map_files/map_files/webmap.gif
 */


import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleMapsGui extends JPanel {

    private static final long serialVersionUID = 1L;
    private BufferedImage image;
    private JPanel canvas;

    private static GoogleMapsGui instance = null;

    private GoogleMapsGui() {
        try {
            String utsgCampusMap = "https://www.comm.utoronto.ca/~valaee/University%20of%20Toronto%20-%20St_%20George%20Campus%20Map_files/map_files/webmap.gif";
            this.image = ImageIO.read(new URL(utsgCampusMap));
        }catch(IOException ex) {
            Logger.getLogger(GoogleMapsGui.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.canvas = new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        canvas.add(new JButton("Currently I do nothing"));
        canvas.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        JScrollPane sp = new JScrollPane(canvas);
        setLayout(new BorderLayout());
        add(sp, BorderLayout.CENTER);
    }

    public static GoogleMapsGui getInstance() {
        if(instance == null) {
            instance = new GoogleMapsGui();
        }
        return instance;
    }

    public JPanel getCanvas() {
        return canvas;
    }

    public void setCanvas(JPanel canvas) {
        this.canvas = canvas;
    }


//    final Logger log = Logger.getLogger(GoogleMapsGui.class.getName());
//    private JPanel contentPane;
//
//    /**
//     * Launch the application.
//     */
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    GoogleMapsGui frame = new GoogleMapsGui();
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//
//        });
//        String utsgCampusAdre = "27King's+College+Cir,Toronto,ON";
//        String apiKey = "AIzaSyBfybhe83kPUTqNm-wWo7S8PUVUeKW7HIg";
//
//        JEditorPane editorPane = new JEditorPane();
//        try {
//            editorPane.setPage("https://map.utoronto.ca/?id=1809#!ct/45469?s/");
//        } catch (IOException e) {
//            editorPane.setContentType("text/html");
//            editorPane.setText("<html>Connection issues!</html>");
//        }
//        JScrollPane pane = new JScrollPane(editorPane);
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(pane);
//        frame.setSize(500, 300);
//        frame.setVisible(true);
//

//    }
//
//    /**
//     * Create the frame.
//     */
//    public GoogleMapsGui() {
//        setTitle("Map");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setBounds(100, 100, 1000, 800);
//        contentPane = new JPanel();
//        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//        setContentPane(contentPane);
//
//        JFrame test = new JFrame("Google Maps");
//
//        try {
            // String latitude = "-25.9994652";
            // String longitude = "28.3112051";
//            String location = JOptionPane
//                    .showInputDialog(" please enter the desired loccation");// get
            // the
            // location
            // for
            // geo
            // coding
//            Scanner sc = new Scanner(location);
//            Scanner sc2 = new Scanner(location);
//            String marker = "";
//            String path = JOptionPane
//                    .showInputDialog("what is your destination?");
//            String zoom = JOptionPane
//                    .showInputDialog("how far in do you want to zoom?\n"
//                            + "12(zoomed out) - 20 (zoomed in)");
//
//            String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?";
//            while (sc.hasNext()) {// add location to imageUrl
//                imageUrl = imageUrl + sc.next();
//            }
//            marker = "&markers=color:red|";
//            while (sc2.hasNext()) {// add marker location to marker
//                marker = marker + sc2.next() + ",";
//
//            }
//            marker = marker.substring(0, marker.length() - 1);
//
//            imageUrl = imageUrl + "&size=620x620&scale=2&maptype=hybrid"
//                    + marker;
            //
//            log.info("Generated url");
//
//            String destinationFile = "image.jpg";
//            String utsgCampusAdre = "27King's+College+Cir,Toronto,ON";
//            String apiKey = "AIzaSyBfybhe83kPUTqNm-wWo7S8PUVUeKW7HIg";
//            String utsgCampusMap = "https://www.comm.utoronto.ca/~valaee/University%20of%20Toronto%20-%20St_%20George%20Campus%20Map_files/map_files/webmap.gif";

            // read the map image from Google
            // then save it to a local file: image.jpg
            //
//            URL url = new URL(imageUrl);
//            URL url = new URL(utsgCampusMap);
//            InputStream is = url.openStream();
//            OutputStream os = new FileOutputStream(destinationFile);
//
//            byte[] b = new byte[2048];
//            int length;
//
//            while ((length = is.read(b)) != -1) {
//                os.write(b, 0, length);
//            }
//            log.info("Created image.jpg");
//
//            is.close();
//            os.close();
////            sc.close();
////            sc2.close();
//            log.info("Closed util's");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//            log.severe("Could not create image.jpg");
//        }// fin getting and storing image
//
//        ImageIcon imageIcon = new ImageIcon((new ImageIcon("image.jpg"))
//                .getImage().getScaledInstance(1452, 1284,
//                        java.awt.Image.SCALE_SMOOTH));
//        contentPane.setLayout(null);
//        JLabel imgMap = new JLabel(imageIcon);
//        imgMap.setBounds(-20, -10, 1500, 1300);
//        contentPane.add(imgMap);
//    }

}