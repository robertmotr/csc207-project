package Tests;

import static org.junit.jupiter.api.Assertions.*;

import Features.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class WebScrapTests {

    @Test
    public void initBuildList() throws IOException {
        PlaceInfo initBuild = new BuildingInfo();
        ArrayList<Place> buildingInfo = initBuild.getTotlist();
        Place convoHall = Factory.createPlace("Convocation Hall", "?id=1809&cId=48654&mId=494494");
        for(Place p : buildingInfo){
            if(p.name.equals("Convocation Hall")){
                assertEquals(p.toString(),convoHall.toString());
            }
        }
    }

    @Test
    public void initFoodPlaceList() throws IOException{
        PlaceInfo initFood = new FoodInfo();
        ArrayList<Place> foodPlaceInfo = initFood.getTotlist();
        Place wcjcl = Factory.createFoodPlace("Wolfond Centre for Jewish Campus Life", "?id=1809&cId=48675&mId=491183","Kosher Food Available");
        assertEquals(foodPlaceInfo.get(0).toString(), wcjcl.toString());
    }

    @Test
    public void initStudyList() throws IOException{
        PlaceInfo initStudy = new StudyInfo();
        ArrayList<Place> studyInfo = initStudy.getTotlist();
        Place rb = Factory.createPlace("Robarts Library (John P. Robarts Research Library)","?id=1809&cId=48697&mId=491343");
        assertEquals(studyInfo.get(34).toString(), rb.toString());
    }
}
