package Map;

import java.io.File;
import java.io.IOException;

public class BuildingInfo extends PlaceInfo {
    String TRINITY = "?id=1809&cId=48656";
    String UNIOFMICHAEL = "?id=1809&cId=48657";
    String VICTORIAUNI = "?id=1809&cId=48658";
    String OTHERCOLLE = "?id=1809&cId=48655";
    String buildingLIST;


    public BuildingInfo() throws IOException {
        super("?id=1809&cId=48654");
        this.buildingLIST = "";
        getTotlist();
//        saveBuildListFile();
    }

    public void getTotlist() throws IOException {
        this.buildingLIST += getList(TRINITY);
        this.buildingLIST += getList(UNIOFMICHAEL);
        this.buildingLIST += getList(VICTORIAUNI);
        this.buildingLIST += getList(OTHERCOLLE);
        this.buildingLIST += getList(this.url,"//ul[@role='list']/li[@role='listitem']/a");
    }

    public void saveBuildListFile() throws IOException {
        //create file
        String filename = "buildingList.txt";
        File file = new File("./resources/"+filename);

        //Save file
        saveListFile(file, this.buildingLIST);

    }
}
