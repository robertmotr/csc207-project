package Map;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.GoogleMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable, MapComponentInitializedListener {

    @FXML
    private GoogleMapView mapView;
    private GoogleMapsGui GoogleMapsInstance;
    @FXML
    private TextField searchBar;
    @FXML
    private Button searchBtn;
    @FXML
    private Text sidebar;

    //infos
    PlaceInfo studyInfo;
    PlaceInfo foodInfo;
    PlaceInfo buildingInfo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        GoogleMapsInstance = GoogleMapsGui.initialize(mapView);
        this.mapView = GoogleMapsInstance.getMapView();
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
                    link = searchFile("./resources/buildingList.txt", searchBar.getText());
                    String display = buildingInfo.specPlace(link);
                    System.out.println(display);
                    sidebar = new Text(display);
                    sidebar.wrappingWidthProperty().set(345);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });
    }

    // method runs when map is initialized, put all the stuff to control the graphical aspects of map at startup here
    @Override
    public void mapInitialized() {
        GoogleMapsInstance.onInitialized();
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
}
