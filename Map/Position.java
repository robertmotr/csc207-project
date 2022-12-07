package Map;

import com.dlsc.gmapsfx.javascript.object.*;
import com.dlsc.gmapsfx.service.geocoding.GeocoderStatus;
import com.dlsc.gmapsfx.service.geocoding.GeocodingResult;
import com.dlsc.gmapsfx.service.geocoding.GeocodingService;

import java.util.Arrays;

/**
Class meant to abstract away details from a marker position from the GMapsFX library. Uses the facade design pattern.
 Handles putting a marker on the map and geocoding the details.
 **/
public class Position {

    // Represents formatted "pretty address"
    private String address;

    // Represents the rest of the address details, such as postal code, province and country
    private String addressDetails;

    // Static map instance to be shared across Position instances.
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

    /**
     * Handles building a marker on a map given a LatLong co-ordinate object, and a map object to place the marker on.
     * Also handles creating an infowindow to describe the details of the location by reverse geocoding.
     * @param coordinate
     * @param map
     */
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

            this.address = placeId[0];
            this.addressDetails = address[0];

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


    // Getters and setters for class attributes that should be allowed to be retrieved and/or modified.
    public LatLong getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(LatLong coordinate) {
        this.coordinate = coordinate;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }

}
