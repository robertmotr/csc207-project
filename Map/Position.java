package Map;

import com.dlsc.gmapsfx.javascript.object.*;
import com.dlsc.gmapsfx.service.geocoding.GeocoderStatus;
import com.dlsc.gmapsfx.service.geocoding.GeocodingResult;
import com.dlsc.gmapsfx.service.geocoding.GeocodingService;

import java.util.Arrays;

/**
Class meant to abstract away details from a marker position from the GMapsFX library.
 **/
public class Position {

    private static GoogleMap map;

    private LatLong coordinate;
    private Marker marker;
    private MarkerOptions markerOptions;

    private InfoWindowOptions options;

    private InfoWindow window;

    public Position() {
        this.coordinate = null;
        this.marker = null;
        this.markerOptions = null;
    }

    public Position(LatLong coordinate, GoogleMap map) {

        this.coordinate = coordinate;
        this.markerOptions = new MarkerOptions();
        this.markerOptions.animation(Animation.BOUNCE);
        this.markerOptions.position(this.coordinate);

        this.marker = new Marker(this.markerOptions);
        Position.map = map;
        Position.map.addMarker(this.marker);

        final String[] placeId = new String[1];
        final String[] address = new String[1];

        GeocodingService service = new GeocodingService();
        service.reverseGeocode(coordinate.getLatitude(), coordinate.getLongitude(), (results, status) -> {
            String[] parts = results[0].getFormattedAddress().split(",");
            placeId[0] = parts[0] + "," + parts[1];
            address[0] = parts[2] + "," + parts[3];

            this.options = new InfoWindowOptions();
            this.options.content("<h2>Position selected:</h2>" +
                    placeId[0] + "<br>" +
                    address[0]);

            this.window = new InfoWindow(this.options);
            this.window.open(map, this.marker);
        });
    }

    /*
    Removes markers, windows, and associated objects from Position class.
     */
    public void destroyMarker() {
        this.marker.setVisible(false);
        this.markerOptions.visible(false);

        map.removeMarker(this.marker);

        this.marker = null;
        this.markerOptions = null;
        this.window = null;
        this.options = null;
        this.coordinate = null;
    }

}
