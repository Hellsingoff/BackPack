import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemsTest {
    private static Item item0, item1, item2;

    // создание нескольких предметов для тестирования комплектов
    @BeforeAll
    static void createTestItems() {
        item0 = new Item(4, 5);
        item1 = new Item(2, 1);
        item2 = new Item(1, 100);
    }

    // тест добавления 1 предмета в комплект
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

    // тест добавления нескольких предметов в комплект
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
}