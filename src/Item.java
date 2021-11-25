class Item implements Comparable<Item> {
    private final int mass, price;
    private final double greed;

    Item(int mass, int price){
        this.mass = mass;
        this.price = price;
        greed = (double) price/mass;
    }

    public int compareTo(Item item) {
        if (item.getMass() == mass)
            return Integer.compare(item.getPrice(), price);
        return Integer.compare(mass, item.getMass());
    }

    int getPrice() { return price; }
    int getMass() { return mass; }
    double getGreed() { return greed; }
}