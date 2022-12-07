package Map;

import Features.*;
import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.javascript.object.LatLong;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.event.GMapMouseEvent;
import com.dlsc.gmapsfx.javascript.event.UIEventType;
import com.dlsc.gmapsfx.javascript.object.GoogleMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static Features.PlaceInfo.getAnchorsofNamesURL;
import static Features.PlaceInfo.name;

public class Controller implements Initializable, MapComponentInitializedListener {

    private Position currentSearchedPt = null;
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

    /**
     * Initialize infos
     * @throws IOException
     */
    private void initInfos() throws IOException {
        initBuild = new BuildingInfo();
        initFood = new StudyInfo();
        initStudy = new FoodInfo();

        this.buildingInfo = initBuild.getTotlist();
        this.studyplaceInfo = initFood.getTotlist();
        this.foodplaceInfo = initStudy.getTotlist();
        this.foodTypeInfo = initFood.foodTypeLIST;
    }

    /**
     * The initialize method for Controller is called when the FXML file is parsed and objects need to be initialized.
     * Note that any code below should assume that the GoogleMapView has NOT been initialized yet. See the mapInitialized
     * method for that purpose.
     * Method should not be called anywhere else but here.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sidebar.setWrapText(true);
        sidebar.setPrefWidth(730);
        sidebar.setPrefHeight(500);
        sidebar.setMaxHeight(500);

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

        HashSet<String> suggestions = new HashSet<>();
        suggestions.addAll(buildingInfo.stream().map(Place::getName).collect(Collectors.toSet()));
        suggestions.addAll(foodplaceInfo.stream().map(Place::getName).collect(Collectors.toSet()));
        suggestions.addAll(studyplaceInfo.stream().map(Place::getName).collect(Collectors.toSet()));

        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(searchBar, suggestions);
        binding.setPrefWidth(searchBar.getPrefWidth());
        binding.setDelay(50);

        //Turn on multiple select mode
        filterSearch.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Get selection model1
        MultipleSelectionModel<TreeItem<String>> selected = filterSearch.getSelectionModel();

        // Event listener to handle if user selects a TreeItem.
        selected.selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            public void changed(ObservableValue<? extends TreeItem<String>> changed, TreeItem<String> oldVal,
                                TreeItem<String> newVal) {
                //Store the two possible point
                prevVal = oldVal;
                currVal = newVal;

                // Display the selection
                if (prevVal == null){
                    sidebar.setText("Your starting destination is: " + currVal.getValue());
                }
                else{
                    try {
                        //Sets text for when the if statement doesn't execute
                        sidebar.setText("New destination is " + currVal.getValue() + ". \n Starting from: " + prevVal.getValue());

                        //Makes sure selections are actual places not headers
                        if(!(prevVal.getChildren().contains("Places")) & !(currVal.getValue().contains("Places"))){

                            //Gets info

                            String[] originInfo = PlaceInfo.specPlace(searchPlace(prevVal.getValue())).split("\n");
                            String[] destinationInfo = PlaceInfo.specPlace(searchPlace(currVal.getValue())).split("\n");

                            //Tries to get address from PlaceInfo, if none is available searches using the name instead
                            String origin = formatAddress(originInfo);
                            String destination = formatAddress(destinationInfo);

                            if(origin == null){
                                origin = currVal.getValue().replaceAll(" ", "+") + "+Toronto,+ON";
                            }

                            if(destination == null){
                                destination = prevVal.getValue().replaceAll(" ", "+") + "+Toronto,+ON";
                            }

                            //Sets text

                            sidebar.setText("New destination is " + currVal.getValue() + ". \n Starting from: " + prevVal.getValue() + ". \n" +
                                    "Directions: " + GoogleMapsApi.getDirections(origin, destination, "walking"));

                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    // Helper method to handle initializing and displaying the TreeItem objects for the filterSearch TreeView
    // control.
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


    /**
     * See if place exist in utsg
     * @param place
     * @return
     * @throws IOException
     */
    public String searchPlace(String place) throws IOException {
        String search = place.toLowerCase();
        //Go through all place list to find place
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


    /**
     * Method that gets called when the GoogleMapView control is initialized fully. You may assume that the mapView has been
     * loaded at this point, and the initialize method for the Controller has also terminated. This method should not
     * be called anywhere else but here.
     */
    @Override
    public void mapInitialized() {
        GoogleMapsInstance.onInitialized();
        this.points = new Stack<>();
        GoogleMap map = GoogleMapsInstance.getMap();
        
        map.addMouseEventHandler(UIEventType.click, (GMapMouseEvent event) -> {
            if(this.currentSearchedPt != null) {
                GoogleMapsInstance.clearMap();
                this.currentSearchedPt = null;
            }
            GoogleMapsInstance.onMouseClick(event);
        });

        //Search Button
        searchBtn.setOnAction(e -> {
            GoogleMapsInstance.clearMap();
            //if searchbar is not empty then search if it is valid and display on sidebar
            if(searchBar.getText() != null){
                String link = null;
                String display = "Invalid Place";
                try {
                    link = searchPlace(searchBar.getText());
                    if(link != null){
                        display = PlaceInfo.specPlace(link);
                        String address = formatAddress(display.split("\n"));
                        if(address == null){
                            address = searchBar.getText().replaceAll(" ", "+") + "+Toronto+Ontario";
                        }
                        LatLong coordinate = GoogleMapsApi.getLatLongFromAddress(address);
                        if(coordinate != null) {
                            this.currentSearchedPt = GoogleMapsInstance.addMarker(coordinate);
                        }
                    }
                    sidebar.setText(display);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /**
     * Used to determine the correct address from a web-scraped description of a place at University of Toronto.
     * Called in the searchBtn handler.
     * @param split
     * @return
     */
    private String formatAddress(String[] split) {
        String address = "";
        for(int i = 0; i < split.length; i++) {
            String testStr = split[i].trim();
            if(testStr.equals("Toronto, ON")) {
                address = split[i - 1];
                address = address.replaceAll(" ", "+") + "+Toronto+Ontario";
                return address;
            }
        }
        return null;
    }

    /**
     * Called when the user clicks on the File -> Close button in the menu bar. Simply terminates the program.
     * Can be used directly.
     */
    @FXML
    private void onMenuBarClose() {
        System.exit(0);
    }

    /**
     * Called when the user clicks on the Help -> About button in the menu bar. Shows a description of the project,
     * a short summary, group members behind it, and the GitHub repository link. Can be called directly.
     */
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

    /**
     * Called when the user clicks on the Map -> Clear markers button in the menu bar. Clears all the markers on the
     * dynamic map. Can be used directly.
     */
    @FXML
    private void onMenuBarClearMarkers() {this.GoogleMapsInstance.clearMap();}

    /**
     * Called when the user clicks on the TTS button in the lower right hand corner of the screen, for accessibility
     * purposes. A chat bot will then read out the directions/building info to the user through audio.
     */
    @FXML
    private void onTTS() {
        TextToSpeech.speak(this.sidebar.getText());
    }
}
