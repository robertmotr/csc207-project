package Map;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.event.GMapMouseEvent;
import com.dlsc.gmapsfx.javascript.event.UIEventType;
import com.dlsc.gmapsfx.javascript.object.DirectionsPane;
import com.dlsc.gmapsfx.javascript.object.GoogleMap;
import com.dlsc.gmapsfx.service.directions.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;


public class Controller implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

    private DirectionsService directionsService;
    private DirectionsPane directionsPane;

    @FXML
    private GoogleMapView mapView;

    private Stack<Position> points;

    private GoogleMapsGui GoogleMapsInstance;

    private GoogleMapsGui instance;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchBtn;

    @FXML
    private Label sidebar;

    @FXML
    private TreeView filterSearch;


    //infos
    PlaceInfo studyInfo;
    PlaceInfo foodInfo;
    PlaceInfo buildingInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        GoogleMapsInstance = GoogleMapsGui.initialize(mapView);
        this.instance = GoogleMapsInstance;
        this.mapView.addMapInitializedListener(this);


        //Initiate infos
        try {
            Factory factory = new Factory();
            this.studyInfo = factory.createInfo("study");
            this.foodInfo = factory.createInfo("food");
            this.buildingInfo = factory.createInfo("building");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Search Button
        searchBtn.setOnAction(e -> {
            //if filter not selected, assume its searching for building
            if(!searchBar.getText().equals(null)){
                String link = null;
                try {
                    link = searchFile("resources" + File.separator + "buildingList.txt", searchBar.getText());
                    String display = buildingInfo.specPlace(link);
                    sidebar.setText(display);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            MenuButton menuButton = new MenuButton("Don't touch this");
            menuButton.getItems().addAll(new MenuItem("Really"), new MenuItem("Do not"));
        });
    }

    public String searchFile(String filepath, String place) throws IOException {
        File file = new File(filepath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line, name, link;

        line = br.readLine();
        while (line != null){
            name = line.split(",")[0].toLowerCase();
            link = line.split(",")[1];
            if(name.equals(place.toLowerCase())){
                return link;
            }
            line = br.readLine();
        }
        br.close();
        return null;
    }


    // Currently doesn't work, might work in the future, but very buggy
    private void renderDirections(Position a, Position b) {
        DirectionsRequest request = new DirectionsRequest(a.getAddress(), b.getAddress(), TravelModes.WALKING, false);
        directionsService.getRoute(request, this, new DirectionsRenderer(true, mapView.getMap(), directionsPane));
    }

    @Override
    public void mapInitialized() {
        GoogleMapsInstance.onInitialized();
        this.points = new Stack<>();
        DirectionsService service = new DirectionsService();
        this.directionsService = service;
        GoogleMap map = GoogleMapsInstance.getMap();
        DirectionsPane pane = mapView.getDirec();
        this.directionsPane = pane;

        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
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
        });
    }


    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {}
}
