package Map;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildingInfo extends PlaceInfo {
    String TRINITY = "?id=1809&cId=48656";
    String UNIOFMICHAEL = "?id=1809&cId=48657";
    String VICTORIAUNI = "?id=1809&cId=48658";
    String OTHERCOLLE = "?id=1809&cId=48655";
    String BUILDINGWEBURL = "?id=1809&cId=48654";
    ArrayList<Place> buildingLIST = new ArrayList<>();
    String tot = "";

    public BuildingInfo(){

    }

    private void updateList(List<HtmlAnchor> l){
        for(HtmlAnchor build : l){
            String name = build.asNormalizedText();
            String url = build.getHrefAttribute();
            this.buildingLIST.add(Factory.createPlace(name, url));
        }
    }

    @Override
    public ArrayList<Place> getTotlist() throws IOException {
        List<HtmlAnchor> trinity = getAnchorsofNamesURL(TRINITY);
        List<HtmlAnchor> micouni = getAnchorsofNamesURL(UNIOFMICHAEL);
        List<HtmlAnchor> vicuni = getAnchorsofNamesURL(VICTORIAUNI);
        List<HtmlAnchor> othcol = getAnchorsofNamesURL(OTHERCOLLE);
        List<HtmlAnchor> othbuil = getAnchorsofNamesURL(BUILDINGWEBURL, "//ul[@role='list']/li[@role='listitem']/a");
        //Get building from trinity
        updateList(trinity);
        //Get building from uni of michael
        updateList(micouni);
        //Get building from vic uni
        updateList(vicuni);
        //Get building from oth college
        updateList(othcol);
        //Get remaining building
        updateList(othbuil);

        return this.buildingLIST;
    }

    public void saveBuildListFile() throws IOException {
        //Go through buildlist
        for(Place bui : this.buildingLIST){
            this.tot += bui.toString();
        }

        //create file
        String filename = "buildingList.txt";
        File file = new File("./resources/"+filename);

        //Save file
        saveListFile(file, this.tot);

    }
}
