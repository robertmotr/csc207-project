package Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import Map.BuildingInfo;


public class Controller implements Initializable {

    @FXML
    private ImageView mapImage;

    @FXML
    private AnchorPane mapPane;

    @FXML
    private GridPane guiPane;

    @FXML
    private VBox boxStuff;

    private GoogleMapsGui instance;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchBtn;

    @FXML
    private Text sidebar;

    PlaceInfo buildingInfo;
    PlaceInfo studyplaceInfo;
    PlaceInfo foodplaceInfo;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        instance = GoogleMapsGui.getInstance();
        mapImage.setManaged(false);
        mapImage.fitWidthProperty().bind(mapPane.widthProperty());
        mapImage.fitHeightProperty().bind(mapPane.heightProperty());
        mapImage.setPreserveRatio(true);


        //Initiate infos
        try {
            initInfos();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        File file = new File("resources/webmap.png");
        Image image = new Image(file.toURI().toString());
        mapImage.setImage(image);

        //Search Button
        searchBtn.setOnAction(e -> {
            //if filter not selected, assume its searching for building
            if(!searchBar.getText().equals(null)){
                String link = null;
                try {
                    link = searchFile("./resources/buildingList.txt", searchBar.getText());
                    String display = this.buildingInfo.specPlace(link);
                    System.out.println(display);
                    sidebar = new Text(display);
                    sidebar.wrappingWidthProperty().set(345);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });
    }

    private void initInfos() throws IOException {
        this.buildingInfo = new BuildingInfo();
        this.studyplaceInfo = new StudyInfo();
        this.foodplaceInfo = new FoodInfo();

        this.buildingInfo.getTotlist();
        this.studyplaceInfo.getTotlist();
        this.foodplaceInfo.getTotlist();

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
