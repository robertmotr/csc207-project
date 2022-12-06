package Map;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodInfo extends PlaceInfo{
    final String FOODTYPEURL = "?id=1809&cId=48659";
    private String tot = "";
    ArrayList<Place> foodPlaceLIST = new ArrayList<>();

    private void updatetyList(List<HtmlAnchor> l){
        for(HtmlAnchor build : l){
            String name = build.asNormalizedText();
            String url = build.getHrefAttribute();
            this.foodTypeLIST.put(name, url);
        }
    }

    private void updateFoodPlaceList(List<HtmlAnchor> l, String type) throws IOException {
        for(HtmlAnchor anch : l){
            this.foodPlaceLIST.add(Factory.createFoodPlace(anch.asNormalizedText(),anch.getHrefAttribute(), type));
        }
    }

    public void getfoodTypeLIST() throws IOException {
        foodTypeLIST = new HashMap<String,String>();
        List<HtmlAnchor> foodty = getAnchorsofNamesURL(FOODTYPEURL);
        updatetyList(foodty);
    }

    @Override
    public ArrayList<Place> getTotlist() throws IOException {
        getfoodTypeLIST();
        for(String type : foodTypeLIST.keySet()){
            List<HtmlAnchor> placeoftype = getAnchorsofNamesURL(foodTypeLIST.get(type));
            updateFoodPlaceList(placeoftype, type);
        }

        return this.foodPlaceLIST;

    }

    public void saveFoodListFile() throws IOException {
        //Go through buildlist
        for(Place bui : this.foodPlaceLIST){
            this.tot += bui.toString();
        }

        //create file
        String filename = "foodPlaceList.txt";
        File file = new File("./resources/"+filename);

        saveListFile(file, this.tot);

    }

    public void saveFoodTypeFile() throws IOException{
        //create file
        String filename = "foodTypeList.txt";
        File file = new File("./resources/"+filename);

        //Save file
        saveListFile(file, getList(FOODTYPEURL));
    }
}
