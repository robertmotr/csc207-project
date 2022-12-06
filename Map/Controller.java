package Map;

import com.dlsc.gmapsfx.GoogleMapView;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
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
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import static Map.PlaceInfo.getAnchorsofNamesURL;
import static Map.PlaceInfo.name;
import java.util.Stack;

public class Controller implements Initializable, MapComponentInitializedListener {

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
    ArrayList<Place> buildingInfo;
    ArrayList<Place> studyplaceInfo;
    ArrayList<Place> foodplaceInfo;
    HashMap<String,String> foodTypeInfo;
    PlaceInfo initBuild;
    PlaceInfo initFood;
    PlaceInfo initStudy;

    private void initInfos() throws IOException {
        initBuild = new BuildingInfo();
        initFood = new StudyInfo();
        initStudy = new FoodInfo();

        this.buildingInfo = initBuild.getTotlist();
        this.studyplaceInfo = initFood.getTotlist();
        this.foodplaceInfo = initStudy.getTotlist();
        this.foodTypeInfo = initFood.foodTypeLIST;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        GoogleMapsInstance = GoogleMapsGui.initialize(mapView);
        this.instance = GoogleMapsInstance;
        this.mapView.addMapInitializedListener(this);


        //Initiate infos
        try {
            initInfos();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Search Button
        searchBtn.setOnAction(e -> {
            //if filter not selected, assume its searching for building
            if(!searchBar.getText().equals(null)){
                String link = null;
                String display = "Invalid Place";
                try {
                    link = searchPlace(searchBar.getText());
                    if(link != null){
                        display = PlaceInfo.specPlace(link);
                        System.out.println(display);
                    }
                    sidebar.setText(display);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        TreeItem<String>  rootItem = new TreeItem<>("Places");
        TreeItem<String> studyItem = new TreeItem<>("Study Places");
        for (Place place: this.studyplaceInfo){
            studyItem.getChildren().add(new TreeItem<>(place.name));
        }
        TreeItem<String> foodItem = new TreeItem<>("Food Places");
        TreeItem<String> buildingItem = new TreeItem<>("Building Places");
        for (Place place: this.buildingInfo){
            buildingItem.getChildren().add(new TreeItem<>(place.name));
        }
        TreeItem<String> allFoodItem = new TreeItem<>("All Places");
        for (Place place: this.foodplaceInfo) {
            allFoodItem.getChildren().add(new TreeItem<>(place.name));
        }
        TreeItem<String> halalItem = new TreeItem<>("Halal Places");
        try {
            for (HtmlAnchor place: PlaceInfo.getAnchorsofNamesURL(this.foodTypeInfo.get("Halal Entrees Available"))){
                halalItem.getChildren().add(new TreeItem<>(name(place)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TreeItem<String> beverageItem = new TreeItem<>("Beverage Places");
        try {
            for (HtmlAnchor place: PlaceInfo.getAnchorsofNamesURL(this.foodTypeInfo.get("Beverages"))){
                beverageItem.getChildren().add(new TreeItem<>(name(place)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TreeItem<String> kosherItem = new TreeItem<>("Kosher Places");
        try {
            for (HtmlAnchor place: PlaceInfo.getAnchorsofNamesURL(this.foodTypeInfo.get("Kosher Food Available"))){
                kosherItem.getChildren().add(new TreeItem<>(name(place)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TreeItem<String> veganItem = new TreeItem<>("Vegan Places");
        try {
            for (HtmlAnchor place: PlaceInfo.getAnchorsofNamesURL(this.foodTypeInfo.get("Vegan Foods"))){
                veganItem.getChildren().add(new TreeItem<>(name(place)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TreeItem<String> vegItem = new TreeItem<>("Vegetarian Places");
        try {
            for (HtmlAnchor place: PlaceInfo.getAnchorsofNamesURL(this.foodTypeInfo.get("Vegetarian Food"))){
                vegItem.getChildren().add(new TreeItem<>(name(place)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        foodItem.getChildren().addAll(allFoodItem, halalItem, beverageItem,
                kosherItem, vegItem, veganItem);

        rootItem.getChildren().addAll(studyItem, foodItem, buildingItem);
        filterSearch.setRoot(rootItem);
    }


    public String searchPlace(String place) throws IOException {
        String search = place.toLowerCase();
        for(Place p : this.buildingInfo){
            if(p.name.toLowerCase().equals(search)){
                return p.url;
            }
        }
        for(Place q : this.studyplaceInfo){
            if(q.name.toLowerCase().equals(search)){
                return q.url;
            }
        }
        for(Place r : this.foodplaceInfo){
            if(r.name.toLowerCase().equals(search)){
                return r.url;
            }
        }
        return null;
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

    @FXML
    private void onMenuBarClose() {
        System.exit(0);
    }

    @FXML
    private void onMenuBarAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Designed by WERM group for 2022 CSC207H5 final project @ UTM.\n" +
                "This program is an interactive, dynamic map that allows users to find specific information about buildings at UTSG, find specific food spots,\n" +
                "and get directions and distance between spots on the UTSG campus for walking. This project was made by Wardah, Emma, Robert and Michael. The GitHub link\n" +
                "where the git repository can be found is shown below:\n" +
                "github.com/robertmotr/csc207-project");
        alert.setTitle("About:");
        alert.setHeaderText("An informative note:");
        alert.showAndWait();
    }
}
