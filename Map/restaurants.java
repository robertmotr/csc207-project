package Map;

class Restaurant{
    private Boolean halalType;
    private Boolean beverageType;
    private Boolean glutenFreeType;
    private Boolean kosherType;
    private Boolean veganType;
    private Boolean vegetarianType;

    public Restaurant(String type) {
        if (type == "Beverages"){
            this.beverageType = true;
        }
        if (type == "")
        this.halalType = halalType;
        this.beverageType = beverageType;
        this.glutenFreeType = glutenFreeType;
        this.kosherType=kosherType;
        this.veganType = veganType;
        this.vegetarianType = vegetarianType;
    }
    public Boolean getHalalType() {
        return halalType;
    }
    public Boolean getBeverageType() {
        return beverageType;
    }
    public Boolean getGlutenFreeType() {
        return glutenFreeType;
    }
    public Boolean getKosherType() {
        return kosherType;
    }
    public Boolean getVeganType(){
        return veganType;
    }
    public Boolean getVegetarianType(){
        return vegetarianType;
    }

}