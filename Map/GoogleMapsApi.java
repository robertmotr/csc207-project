package Map;

import com.dlsc.gmapsfx.GoogleMapView;
import okhttp3.*;

import java.io.IOException;

/**
 * Communications to Google Maps, directions and place API
 *
 * Reference from:
 * https://developers.google.com/maps/documentation/directions/get-directions#maps_http_directions_toronto_montreal-java
 */

public class GoogleMapsApi {

    public static GoogleMapsApi initialize() {
        return new GoogleMapsApi();
    }


    private final String apiKey = "AIzaSyCPfTsYtKOIcTNmhPGUrDphHTI5giH5X9s";
    OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create(mediaType, "");
    Request request = new Request.Builder()
            .url("https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=" + apiKey)
            .method("GET", body)
            .build();
    Response response;

    {
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
