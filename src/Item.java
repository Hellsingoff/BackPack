import java.math.BigDecimal;

class Item {
    private final BigDecimal mass, price, greed;
    Item(BigDecimal mass, BigDecimal price){
        this.mass = mass;
        this.price = price;
        greed = price.divide(mass);
    }

    BigDecimal getPrice() { return price; }
    BigDecimal getMass() { return mass; }
    BigDecimal getGreed() { return greed; }
}