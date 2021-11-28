import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BackpackTest {
    // тест наполнения рюкзака перебором
    @Test
    void fillTheBackpack() {
        final ArrayList<Item> itemList = new ArrayList<>();
        Item item0 = new Item(11, 15);
        Item item1 = new Item(5, 6);
        Item item2 = new Item(1, 1);
        itemList.add(item0);
        itemList.add(item1);
        itemList.add(item2);
        Collections.sort(itemList);
        Items result = Backpack.fillTheBackpack(new Items(), itemList);
        assertEquals(result.getPrice(), 39);
        System.out.println("Тест наполнения успешно пройден:\n" + result +"\n------------------------");
    }

    // тест удаления бесполезных вещей
    @Test
    void removeUseless() {
        int tests = 1000;
        int itemsNumber = 10;
        int itemsCounter = 0;
        for (int n = 0; n < tests; n++) {
            final ArrayList<Item> itemList = new ArrayList<>();
            for (int i = 0; i < itemsNumber; i++)
                itemList.add(new Item((int) (Math.random()*30.999) + 1, (int) (Math.random()*30.999) + 1));
            ArrayList<Item> clearedList = new ArrayList<>(itemList);
            Backpack.removeUseless(clearedList);
            Items cleanResult = Backpack.fillTheBackpack(new Items(), clearedList);
            Items result = Backpack.fillTheBackpack(new Items(), itemList);
            itemsCounter += clearedList.size();
            assertEquals(result.getPrice(), cleanResult.getPrice());
        }
        System.out.println("Тест удаления успешно пройден.\n" +
                "Удалено " + (1 - ((double) itemsCounter / (tests*itemsNumber))) * 100 + "% предметов\n" +
                "Максимальная ценность до удаления и после удаления совпала в каждом тесте\n------------------------");
    }

    // тест частичного наполнения рюкзака по формуле, оценка его погрешности
    @Test
    void greed() {
        int tests = 1000;
        int itemsNumber = 10;
        int greedMass = 0;
        int mass = 0;
        int errors = 0;
        for (int n = 0; n < tests; n++) {
            final ArrayList<Item> itemList = new ArrayList<>();
            for (int i = 0; i < itemsNumber; i++)
                itemList.add(new Item((int) (Math.random()*30.999 + 1), (int) (Math.random()*30.999 + 1)));
            Backpack.removeUseless(itemList);
            Items greedFilled = Backpack.greed(itemList);
            Items result = Backpack.fillTheBackpack(new Items(), itemList);
            Items greedResult = Backpack.fillTheBackpack(greedFilled, itemList);
            greedMass += greedFilled.getMass();
            mass += result.getMass();
            if (result.getPrice() != greedResult.getPrice()) errors++;
            assertFalse(0.02 < (double) errors/tests);
        }
        System.out.println("Тест жадности успешно пройден.\n" +
                "Жадно наполнено " + ((double) greedMass/mass) * 100 + "% ёмкости.\n" +
                "Жадность дала ошибку в " + (double) errors/tests * 100 + "% случаев\n------------------------");
    }
}