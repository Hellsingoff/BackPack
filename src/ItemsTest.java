import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemsTest {
    private static Item item0, item1, item2;

    @BeforeAll
    static void createTestItems() {
        item0 = new Item(4, 5);
        item1 = new Item(2, 1);
        item2 = new Item(1, 100);
    }

    @Test
    void add() {
        Items items = new Items();
        items.add(item0);
        items.add(item1);
        items.add(item1);
        items.add(item2);

        assertEquals(items.getMass(),9);
        assertEquals(items.getPrice(),107);
    }

    @Test
    void multipleAdd() {
        Items items = new Items();
        items.add(item0, 2);
        items.add(item1, 15);
        items.add(item2, 1);
        items.add(item0, 4);

        assertEquals(items.getMass(), 55);
        assertEquals(items.getPrice(), 145);
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