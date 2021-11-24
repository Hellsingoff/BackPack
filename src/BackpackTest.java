import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BackpackTest {

    @Test
    void fillTheBackpack() {
        final ArrayList<Item> itemList = new ArrayList<>();
        Item item0 = new Item(BigDecimal.valueOf(11), BigDecimal.valueOf(15));
        Item item1 = new Item(BigDecimal.valueOf(3.5), BigDecimal.valueOf(4));
        Item item2 = new Item(BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        itemList.add(item0);
        itemList.add(item1);
        itemList.add(item2);
        Collections.sort(itemList);
        Items result = new Items();
        Backpack.fillTheBackpack(new Items(), itemList, result);
        assertEquals(result.getPrice(), BigDecimal.valueOf(39));
    }

    @Test
    void removeUseless() {
        for (int n = 0; n < 10; n++) {
            final ArrayList<Item> itemList = new ArrayList<>();
            Items result = new Items();
            Items cleanResult = new Items();
            for (int i = 0; i < 10; i++)
                itemList.add(new Item(BigDecimal.valueOf(Math.random()*30+10), BigDecimal.valueOf(Math.random()*30+10)));
            ArrayList<Item> clearedList = new ArrayList<>(itemList);
            Backpack.removeUseless(clearedList);
            Backpack.fillTheBackpack(new Items(), clearedList, cleanResult);
            Backpack.fillTheBackpack(new Items(), itemList, result);
            System.out.println(itemList.size() + " " + clearedList.size());
            assertEquals(result.getPrice(), cleanResult.getPrice());
        }
    }

    @Test
    void greed() {

    }
}