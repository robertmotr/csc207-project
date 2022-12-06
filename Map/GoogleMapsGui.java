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
 * A dynamic map of UTSG campus
 *
 * Reference from:
 * https://stackoverflow.com/questions/1993981/how-to-access-google-maps-api-in-java-application
 * https://www.tutorialspoint.com/java-program-to-display-a-webpage-in-jeditorpane
 * https://stackoverflow.com/questions/7298817/making-image-scrollable-in-jframe-contentpane/7299067
 * https://www.comm.utoronto.ca/~valaee/University%20of%20Toronto%20-%20St_%20George%20Campus%20Map_files/map_files/webmap.gif
 */

public class GoogleMapsGui {

    private static GoogleMapsGui instance = null;

    private Stack<Position> points;

    private GoogleMap map;
    private GoogleMapView mapView;
    private final String apiKey = "AIzaSyCPfTsYtKOIcTNmhPGUrDphHTI5giH5X9s";

    private GoogleMapsGui(GoogleMapView view) {
        view.setKey(apiKey);
        points = new Stack<>();
        this.mapView = view;
    }

    public static GoogleMapsGui getInstance() {
        if(instance == null) {
            throw new RuntimeException("Singleton must be initialized before getting instance.");
        }
        return instance;
    }

    public static GoogleMapsGui initialize(GoogleMapView view) {
        if(instance == null) {
            instance = new GoogleMapsGui(view);
        }
        return instance;
    }


    public GoogleMapView getMapView() {
        return mapView;
    }

    public void onInitialized() {

        LatLong centerLocation = new LatLong(43.66284509183421, -79.39576369024316);

        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(centerLocation)
                .mapType(MapTypeIdEnum.ROADMAP)
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

    public void clearMap() {
        for(Position point : points) {
            point.destroyMarker();
        }
        points.clear();
        map.clearMarkers();
    }

    public Position addMarker(LatLong coordinate) {
        return new Position(coordinate, map);
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }
}