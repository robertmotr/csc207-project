package Map;

import java.util.ArrayList;
import java.util.List;

public interface filterDesignPattern{
    List<FoodInfo> doFilter(List<FoodInfo> restaurants);
}

class beverageFilter implements filterDesignPattern{
    public List<FoodInfo> doFilter(List<FoodInfo> restaurants){
        List<FoodInfo> beverages = new ArrayList<FoodInfo>();

        return beverages;
    }
}
