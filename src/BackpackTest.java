import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BackpackTest {
    private static final ArrayList<Item> itemList = new ArrayList<>();

    @BeforeAll
    static void createTestItems() {
        Item item0 = new Item(BigDecimal.valueOf(11), BigDecimal.valueOf(15));
        Item item1 = new Item(BigDecimal.valueOf(3.5), BigDecimal.valueOf(4));
        Item item2 = new Item(BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        itemList.add(item0);
        itemList.add(item1);
        itemList.add(item2);
        Collections.sort(itemList);
    }

    @Test
    void fillTheBackpack() {
        final ArrayList<Items> results = new ArrayList<>();
        Backpack.fillTheBackpack(new Items(), itemList, 0, results);
        Collections.sort(results);
        assertEquals(results.get(0).getPrice(), BigDecimal.valueOf(39));
    }

    @Test
    void removeUseless() {
        final ArrayList<Items> results = new ArrayList<>();
        ArrayList<Item> clearedList = new ArrayList<Item>(itemList);
        Backpack.removeUseless(clearedList);
        Backpack.fillTheBackpack(new Items(), clearedList, 0, results);
        Collections.sort(results);
        System.out.println(results.get(0).stringBuilder());
    }

    @Test
    void topResultToString() {

    }

    @Test
    void greed() {

    }
}