class Item {
    private double mass, price, greed;
    private int imass;
    Item(double m, double p){
        mass = m;
        price = p;
        imass = (int) (mass * 1000);
        greed = price / mass;
    }

    double getPrice() { return price; }
    double getMass() { return mass; }
    int getIMass() { return imass; }
    double getGreed() { return greed; }
}