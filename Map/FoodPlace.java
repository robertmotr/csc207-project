package Map;

public class FoodPlace extends Place{

    public String type;

    public FoodPlace(String name, String type, String url){
        super(name, url);
        this.type = type;
    }

    @Override
    public String toString() {
        return this.name + "," + this.url + "," + this.type + "\n";
    }
}
