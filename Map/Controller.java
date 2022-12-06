package Map;

import com.dlsc.gmapsfx.GoogleMapView;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.event.GMapMouseEvent;
import com.dlsc.gmapsfx.javascript.event.UIEventType;
import com.dlsc.gmapsfx.javascript.object.DirectionsPane;
import com.dlsc.gmapsfx.javascript.object.GoogleMap;
import com.dlsc.gmapsfx.service.directions.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static Map.PlaceInfo.getAnchorsofNamesURL;
import static Map.PlaceInfo.name;

public class Controller implements Initializable, MapComponentInitializedListener {
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

    //Selection choice
    TreeItem<String> prevVal;
    TreeItem<String> currVal;

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

        //Display tree view
        displayTreeView();

/*        ArrayList<String> suggestions = new ArrayList<>();
        suggestions.addAll((Collection<? extends String>) this.buildingInfo.stream().map(e -> e.name));
        suggestions.addAll((Collection<? extends String>) this.foodplaceInfo.stream().map(e -> e.name));
        suggestions.addAll((Collection<? extends String>) this.studyplaceInfo.stream().map(e -> e.name));

        TextFields.bindAutoCompletion(searchBar, suggestions).setDelay(50);*/

        //Turn on multiple select mode
        filterSearch.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Get slection model1
        MultipleSelectionModel<TreeItem<String>> selected = filterSearch.getSelectionModel();

        
        selected.selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            public void changed(ObservableValue<? extends TreeItem<String>> changed, TreeItem<String> oldVal,
                                TreeItem<String> newVal) {

                prevVal = oldVal;
                currVal = newVal;

                // Display the selection
                if (prevVal == null){
                    sidebar.setText("Your starting destination is: " + currVal.getValue());
                }
                else{
                    try {
                        sidebar.setText("New destination is " + currVal.getValue() + ". \n Starting from: " + prevVal.getValue());
                        if(!(prevVal.getChildren().contains("Places")) & !(currVal.getValue().contains("Places"))){
                            String originInfo = PlaceInfo.specPlace(searchPlace(prevVal.getValue()));
                            String destinationInfo = PlaceInfo.specPlace(searchPlace(currVal.getValue()));
                            String destination = (destinationInfo.split("\n"))[1].replaceAll(" ", "+")+ "Toronto,+ON";
                            String origin = (originInfo.split("\n"))[1].replaceAll(" ", "+") + "Toronto,+ON";

                            if(GoogleMapsApi.getDirections(origin, destination,"walking").equals("No route found")){
                                origin = prevVal.getValue().replaceAll(" ", "+") + "+University+Of+Toronto";
                                destination = currVal.getValue().replaceAll(" ", "+") + "+University+Of+Toronto";
                                System.out.println("first check failed");
                            }
                            sidebar.setText("New destination is " + currVal.getValue() + ". \n Starting from: " + prevVal.getValue() + ". \n" +
                                    "Directions: " + GoogleMapsApi.getDirections(origin, destination, "walking"));
                            
                            sidebar.setWrapText(true);
                            sidebar.setPrefWidth(730);
                            sidebar.setPrefHeight(500);
                            sidebar.setMaxHeight(500);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void displayTreeView(){
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
        GoogleMap map = GoogleMapsInstance.getMap();
        
        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
            GoogleMapsInstance.onMouseClick(event);
        });

        //Search Button
        searchBtn.setOnAction(e -> {
            //if filter not selected, assume its searching for building
            if(searchBar.getText() != null){
                String link = null;
                String display = "Invalid Place";
                try {
                    link = searchPlace(searchBar.getText());
                    if(link != null){
                        display = PlaceInfo.specPlace(link);
                        GoogleMapsInstance.clearMap();
                        String[] split = display.split("\n");
                        String address = split[4];
                        GoogleMapsInstance.addMarker(address);
                    }
                    sidebar.setText(display);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
