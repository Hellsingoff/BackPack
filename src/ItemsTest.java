import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemsTest {
    private static Item item0, item1, item2;

    @BeforeAll
    static void createTestItems() {
        item0 = new Item(BigDecimal.valueOf(4), BigDecimal.valueOf(5));
        item1 = new Item(BigDecimal.valueOf(2), BigDecimal.valueOf(1));
        item2 = new Item(BigDecimal.valueOf(1), BigDecimal.valueOf(100));
    }

    @Test
    void add() {
        Items items = new Items();
        items.add(item0);
        items.add(item1);
        items.add(item1);
        items.add(item2);

        assertEquals(items.getMass().compareTo(BigDecimal.valueOf(9)), 0);
        assertEquals(items.getPrice().compareTo(BigDecimal.valueOf(107)), 0);
    }

    @Test
    void multipleAdd() {
        Items items = new Items();
        items.add(item0, 2);
        items.add(item1, 15);
        items.add(item2, 1);
        items.add(item0, 4);

        assertEquals(items.getMass().compareTo(BigDecimal.valueOf(55)), 0);
        assertEquals(items.getPrice().compareTo(BigDecimal.valueOf(145)), 0);
    }

    @Test
    void compareTo() {
        Items items0 = new Items();
        items0.add(item0);
        Items items1 = new Items(items0);

        assertEquals(items0.compareTo(items1), 0);

        items0.add(item2);

        assertEquals(items0.compareTo(items1), -1);

        items1.add(item1, 101);

        assertEquals(items0.compareTo(items1), 1);
    }
}