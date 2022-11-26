package Map;/*import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.geometry.Insets;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * This is the main program.

public class Map.MapViewer  { {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIza...")
                .build();
        GeocodingResult[] results =  GeocodingApi.geocode(context,
                "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(results[0].addressComponents));

// Invoke .shutdown() after your application is done making requests
        context.shutdown();
    }
}
*/
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MapViewer extends Application implements MapComponentInitializedListener {

        GoogleMapView mapView;
        GoogleMap map;

        @Override
        public void start(Stage stage) throws Exception {

                //Create the JavaFX component and set this as a listener so we know when
                //the map has been initialized, at which point we can then begin manipulating it.
                mapView = new GoogleMapView();
                mapView.setKey("AIzaSyBSLCW1EqS_KCYiN90uE96N5lb7ON8zCWo");
                mapView.addMapInializedListener(this);

                Scene scene = new Scene(mapView);

                stage.setTitle("JavaFX and Google Maps");
                stage.setScene(scene);
                stage.show();
        }


        @Override
        public void mapInitialized() {
                //Set the initial properties of the map.
                MapOptions mapOptions = new MapOptions();

                mapOptions.center(new LatLong(47.6097, -122.3331))
                        .mapType(MapTypeIdEnum.ROADMAP)
                        .overviewMapControl(true)
                        .panControl(false)
                        .rotateControl(false)
                        .scaleControl(false)
                        .streetViewControl(false)
                        .zoomControl(true)
                        .zoom(12);

                map = mapView.createMap(mapOptions);

                //Add a marker to the map
                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position( new LatLong(47.6, -122.3) )
                        .visible(Boolean.TRUE)
                        .title("My Marker");

                Marker marker = new Marker( markerOptions );

                map.addMarker(marker);

        }

        public static void main(String[] args) {
                launch(args);
        }
}
