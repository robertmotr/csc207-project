package Map;

import com.dlsc.gmapsfx.GoogleMapView;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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


public class Controller implements Initializable, MapComponentInitializedListener {

    @FXML
    private GoogleMapView mapView;

    private GoogleMapsGui GoogleMapsInstance;

    private GoogleMapsGui instance;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchBtn;

    @FXML
    private Text sidebar;

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
        this.mapView = GoogleMapsInstance.getMapView();
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
                    sidebar = new Text(display);
                    sidebar.wrappingWidthProperty().set(345);
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
    }


}
