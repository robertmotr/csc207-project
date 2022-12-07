package Map;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.javascript.event.GMapMouseEvent;
import com.dlsc.gmapsfx.javascript.object.*;
import com.dlsc.gmapsfx.service.geocoding.GeocoderRequest;
import com.dlsc.gmapsfx.service.geocoding.GeocoderStatus;
import com.dlsc.gmapsfx.service.geocoding.GeocodingResult;
import com.dlsc.gmapsfx.service.geocoding.GeocodingService;
import javafx.scene.control.Alert;

import java.awt.*;
import java.util.Arrays;
import java.util.Stack;
/**
 * A dynamic map of the UTSG campus. Uses the Singleton design pattern.
 *
 * Reference from:
 * https://stackoverflow.com/questions/1993981/how-to-access-google-maps-api-in-java-application
 * https://www.tutorialspoint.com/java-program-to-display-a-webpage-in-jeditorpane
 * https://stackoverflow.com/questions/7298817/making-image-scrollable-in-jframe-contentpane/7299067
 * https://www.comm.utoronto.ca/~valaee/University%20of%20Toronto%20-%20St_%20George%20Campus%20Map_files/map_files/webmap.gif
 */

public class GoogleMapsGui {

    private static GoogleMapsGui instance = null;

    // Represents the two possible points that a user may select on the map.
    private Stack<Position> points;

    private GoogleMap map;
    private GoogleMapView mapView;

    // API Key should not be touched, and should also be removed from the source code if this project is
    // ever made public.
    private final String apiKey = "AIzaSyCPfTsYtKOIcTNmhPGUrDphHTI5giH5X9s";

    /**
     * Constructor to create a GoogleMapsGui Singleton instance. Should not be used directly, hence made private.
     * @param view
     */
    private GoogleMapsGui(GoogleMapView view) {
        view.setKey(apiKey);
        points = new Stack<>();
        this.mapView = view;
    }

    /**
     * getInstance method returns the current Singleton instance of GoogleMapsGui if there is one.
     * Throws runtime exception if this method is called without using the initialize method first.
     * @return
     */
    public static GoogleMapsGui getInstance() {
        if(instance == null) {
            throw new RuntimeException("Singleton must be initialized before getting instance.");
        }
        return instance;
    }

    /**
     * Creates and returns a Singleton GoogleMapsGui Singleton instance given a GoogleMapView control from GMapsFX.
     * @param view
     * @return
     */
    public static GoogleMapsGui initialize(GoogleMapView view) {
        if(instance == null) {
            instance = new GoogleMapsGui(view);
        }
        return instance;
    }


    /**
     * Handles map initialization for the GoogleMapView control within the controller. This method should NOT be
     * used anywhere else except under the mapInitialized method within Controller.
     */
    public void onInitialized() {

        LatLong centerLocation = new LatLong(43.66284509183421, -79.39576369024316);

        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(centerLocation)
                .mapType(MapTypeIdEnum.SATELLITE)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(20);

        map = mapView.createMap(mapOptions);

        //Add markers to the map
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(centerLocation);
        Marker main = new Marker(markerOptions1);
        main.setVisible(false);
        main.setAnimation(Animation.DROP);
        map.addMarker(main);

        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
        infoWindowOptions.content("<h2>University of Toronto</h2>"
                + "Welcome to the interactive map.<br>"
                + "Feel free to explore!");

        InfoWindow window = new InfoWindow(infoWindowOptions);
        window.open(map, main);
    }


    /**
     * Handles a mouseClick event under the mouseClick listener in controller. This method should be passed only when a
     * mouse click on the map is registered. This method should NOT be used anywhere else.
     * @param event
     */
    public void onMouseClick(GMapMouseEvent event) {
        if(this.points.size() == 0) {
            Position a = new Position(event.getLatLong(), map);
            points.add(a);
        }
        else if(this.points.size() == 1) {
            Position b = new Position(event.getLatLong(), map);
            Position a = points.pop();
            points.add(a); points.add(b);
        }
        else {
            Position b = points.pop();
            Position a = points.pop();

            a.destroyMarker();
            Position newPt = new Position(event.getLatLong(), map);
            points.add(b); points.add(newPt);
        }
    }

    /**
     * clearMap() clears all markers and associated objects from the map.
     */
    public void clearMap() {
        for(Position point : points) {
            point.destroyMarker();
        }
        points.clear();
        map.clearMarkers();
    }

    /**
     * Adds a marker given a LatLong object onto the map. Returns the Position object created by the LatLong object.
     * @param coordinate
     * @return
     */
    public Position addMarker(LatLong coordinate) {
        return new Position(coordinate, map);
    }

    // Getters and setters shown below:
    public GoogleMapView getMapView() {
        return mapView;
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }
}