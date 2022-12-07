/**
 * Static Factory pattern
 */

package Features;

import java.io.IOException;

public class Factory {

    /**
     * Create Place
     * @param name
     * @param url
     * @return
     */
    public static Place createPlace(String name, String url){
        return new Place(name, url);
    }

    /**
     * Create Food Place
     * @param name
     * @param url
     * @param type
     * @return
     * @throws IOException
     */
    public static Place createFoodPlace(String name, String url, String type) throws IOException {
        return new FoodPlace(name, type, url);
    }
}
