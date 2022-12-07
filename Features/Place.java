package Features;

public class Place {
    public String url;
    public String name;

    /**
     * Constructor
     * @param name
     * @param url
     */
    public Place(String name, String url){
        this.name = name;
        this.url = url;
    }

    /**
     * To string method
     * @return
     */
    @Override
    public String toString() {
        return this.name + "," + this.url + "\n";
    }

    /**
     *
     * @return name of place
     */
    public String getName() {return this.name;}
}
