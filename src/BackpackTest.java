import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BackpackTest {

    @Test
    void fillTheBackpack() {
        Item item0 = new Item(BigDecimal.valueOf(11), BigDecimal.valueOf(15));
        Item item1 = new Item(BigDecimal.valueOf(3.5), BigDecimal.valueOf(4));
        Item item2 = new Item(BigDecimal.valueOf(1), BigDecimal.valueOf(1));

        final ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item0);
        itemList.add(item1);
        itemList.add(item2);

        final ArrayList<Items> results = new ArrayList<>();

        Backpack.fillTheBackpack(new Items(), itemList, results);
        Collections.sort(results);
        System.out.println(results.get(0).stringBuilder());
    }

    @Test
    void removeUseless() {

    }

    @Test
    void topResultToString() {

    }

    @Test
    void greed() {

    }
}