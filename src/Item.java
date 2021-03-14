import java.math.BigDecimal;
import java.math.RoundingMode;

class Item {
    private final BigDecimal mass, price, greed;
    Item(BigDecimal mass, BigDecimal price){
        this.mass = mass;
        this.price = price;
        greed = price.divide(mass, 10, RoundingMode.HALF_UP);
    }

    BigDecimal getPrice() { return price; }
    BigDecimal getMass() { return mass; }
    BigDecimal getGreed() { return greed; }
}