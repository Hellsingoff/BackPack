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
        Items[] result = new Items[] {new Items()};
        Backpack.fillTheBackpack(new Items(), itemList, result);
        assertEquals(result[0].getPrice(), BigDecimal.valueOf(39));
        System.out.println("Тест наполнения успешно пройден:\n"+result[0].stringBuilder()+"\n------------------------");
    }

    @Test
    void removeUseless() {
        int tests = 10;
        int itemsNumber = 10;
        int itemsCounter = 0;
        for (int n = 0; n < tests; n++) {
            final ArrayList<Item> itemList = new ArrayList<>();
            Items[] result = new Items[] {new Items()};
            Items[] cleanResult = new Items[] {new Items()};
            for (int i = 0; i < itemsNumber; i++)
                itemList.add(new Item(BigDecimal.valueOf(Math.random()*30+10), BigDecimal.valueOf(Math.random()*30+10)));
            ArrayList<Item> clearedList = new ArrayList<>(itemList);
            Backpack.removeUseless(clearedList);
            Backpack.fillTheBackpack(new Items(), clearedList, cleanResult);
            Backpack.fillTheBackpack(new Items(), itemList, result);
            itemsCounter += clearedList.size();
            assertEquals(result[0].getPrice(), cleanResult[0].getPrice());
        }
        System.out.println("Тест удаления успешно пройден.\n" +
                "Удалено " + (1 - ((double) itemsCounter / (tests*itemsNumber))) * 100 + "% предметов\n" +
                "Максимальная ценность до удаления и после удаления совпала в каждом тесте\n------------------------");
    }

    @Test
    void greed() {

    }
}