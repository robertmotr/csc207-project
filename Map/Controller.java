package Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable {

    @FXML
    private ImageView mapImage;

    @FXML
    private AnchorPane mapPane;

    @FXML
    private GridPane guiPane;

    private GoogleMapsGui instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        instance = GoogleMapsGui.getInstance();
        mapImage.setManaged(false);
        mapImage.fitWidthProperty().bind(mapPane.widthProperty());
        mapImage.fitHeightProperty().bind(mapPane.heightProperty());
        mapImage.setPreserveRatio(true);

        File file = new File("resources/webmap.png");
        Image image = new Image(file.toURI().toString());
        mapImage.setImage(image);
    }
}
