package Map;

import java.io.*;

public class FoodInfo extends PlaceInfo{
    String foodTypeList;
    String foodPlaceList;

    public FoodInfo() throws IOException {
        super("?id=1809&cId=48659");
        this.foodTypeList = getList(this.url);
        this.foodPlaceList = "";
        getFoodList();
//        saveFoodListFile();
//        saveFoodTypeFile();
    }

    public void getFoodList() throws IOException {
        //Go through each of the food types and get list of places
        for(String link : this.foodTypeList.split("\n")){
            if (!link.equals("")){
                this.foodPlaceList += getList(link.split(",")[1]);
            }
        }
    }


    public void saveFoodListFile() throws IOException {
        //create file
        String filename = "foodPlaceList.txt";
        File file = new File("./resources/"+filename);

        saveListFile(file, this.foodPlaceList);

    }

    public void saveFoodTypeFile() throws IOException{
        //create file
        String filename = "foodTypeList.txt";
        File file = new File("./resources/"+filename);

        //Save file
        saveListFile(file, getList(this.url));
    }
}
