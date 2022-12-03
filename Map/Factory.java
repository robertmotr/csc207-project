package Map;

import java.io.IOException;

public class Factory {

    public PlaceInfo createInfo(String type) throws IOException {
        if(type == "building") return new BuildingInfo();
        if(type == "study") return new StudyInfo();
        if(type == "food") return new FoodInfo();
        return null;
    }
}
