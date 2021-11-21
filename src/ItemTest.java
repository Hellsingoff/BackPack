import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void compareTo() {
        Item item0 = new Item(BigDecimal.valueOf(4), BigDecimal.valueOf(5));
        Item item1 = new Item(BigDecimal.valueOf(6), BigDecimal.valueOf(5));
        Item item2 = new Item(BigDecimal.valueOf(5), BigDecimal.valueOf(4));
        Item item3 = new Item(BigDecimal.valueOf(5), BigDecimal.valueOf(6));
        Item item4 = new Item(BigDecimal.valueOf(5), BigDecimal.valueOf(5));
        Item item5 = new Item(BigDecimal.valueOf(5), BigDecimal.valueOf(5));

        assertEquals(item0.compareTo(item1), -1);
        assertEquals(item2.compareTo(item3), 1);
        assertEquals(item4.compareTo(item5), 0);
    }
}