import java.math.BigDecimal;
import java.math.RoundingMode;

class Item implements Comparable<Item> {
    private final BigDecimal mass, price, greed;

    Item(BigDecimal mass, BigDecimal price){
        this.mass = mass;
        this.price = price;
        greed = price.divide(mass, 10, RoundingMode.HALF_UP);
    }

    public int compareTo(Item item) {
        if (item.getMass().equals(mass))
            return -price.compareTo(item.getPrice());
        return mass.compareTo(item.getMass());
    }

    BigDecimal getPrice() { return price; }
    BigDecimal getMass() { return mass; }
    BigDecimal getGreed() { return greed; }
}