package Map;

public class Place {
    public String url;
    public String name;

    public Place(String name, String url){
        this.name = name;
        this.url = url;
    }

    @Override
    public String toString() {
        return this.name + "," + this.url + "\n";
    }

    public String getName() {return this.name;}
}
