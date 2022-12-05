package Map;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
    PlaceInfo buildingInfo;
    PlaceInfo studyplaceInfo;
    PlaceInfo foodplaceInfo;

    private void initInfos() throws IOException {
        this.buildingInfo = new BuildingInfo();
        this.studyplaceInfo = new StudyInfo();
        this.foodplaceInfo = new FoodInfo();

        this.buildingInfo.getTotlist();
        this.studyplaceInfo.getTotlist();
        this.foodplaceInfo.getTotlist();

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

        File file = new File("C:\\Users\\R_asl\\CSC207\\csc207-project\\resources\\webmap.png");
        Image image = new Image(file.toURI().toString());
        mapImage.setImage(image);

        //Search Button
        searchBtn.setOnAction(e -> {
            //if filter not selected, assume its searching for building
            if(!searchBar.getText().equals(null)){
                String link = null;
                try {
                    link = searchFile("C:\\Users\\R_asl\\CSC207\\csc207-project\\resources\\buildingList.txt", searchBar.getText());
                    String display = this.buildingInfo.specPlace(link);
                    String display = buildingInfo.specPlace(link);
                    System.out.println(display);
                    sidebar = new Text(display);
                    sidebar.wrappingWidthProperty().set(345);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        TreeItem<String>  rootItem = new TreeItem<>("Places");
        TreeItem<String> studyItem = new TreeItem<>("Study Places");
        TreeItem<String> foodItem = new TreeItem<>("Food Places");
        TreeItem<String> buildingItem = new TreeItem<>("Building Places");
        TreeItem<String> allFoodItem = new TreeItem<>("All Places");
        foodItem.getChildren().add(allFoodItem);

        rootItem.getChildren().addAll(studyItem, foodItem, buildingItem);
        filterSearch.setRoot(rootItem);
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

    @Override
    public void mapInitialized() {
        GoogleMapsInstance.onInitialized();
    }


}
