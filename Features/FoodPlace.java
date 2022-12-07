package Features;

public class FoodPlace extends Place {

    public String type;

    /**
     * Constructor
     * @param name
     * @param type
     * @param url
     */
    public FoodPlace(String name, String type, String url){
        super(name, url);
        this.type = type;
    }

    /**
     * to string method
     * @return
     */
    @Override
    public String toString() {
        return this.name + "," + this.url + "," + this.type + "\n";
    }
}
