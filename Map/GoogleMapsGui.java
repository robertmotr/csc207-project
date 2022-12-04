package Map; /**
 * A static map of UTSG campus
 *
 * Reference from:
 * https://stackoverflow.com/questions/1993981/how-to-access-google-maps-api-in-java-application
 * https://www.tutorialspoint.com/java-program-to-display-a-webpage-in-jeditorpane
 * https://stackoverflow.com/questions/7298817/making-image-scrollable-in-jframe-contentpane/7299067
 * https://www.comm.utoronto.ca/~valaee/University%20of%20Toronto%20-%20St_%20George%20Campus%20Map_files/map_files/webmap.gif
 */

public class GoogleMapsGui {
    private static GoogleMapsGui instance = null;

    private GoogleMapsGui() {}

    public static GoogleMapsGui getInstance() {
        if(instance == null) {
            instance = new GoogleMapsGui();
        }
        return instance;
    }

}