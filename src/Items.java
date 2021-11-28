import java.util.HashMap;
import java.util.Map;

public class Items {
    private Map<Item, Integer> list;
    private int price, mass;

    Items() {
        list = new HashMap<>();
        price = 0;
        mass = 0;
    }

    // конструктор для создания копии объекта (immutable поведение)
    Items(Items items) {
        list = new HashMap<>(items.getList());
        price = items.getPrice();
        mass = items.getMass();
    }

    // добавление одного предмета в комплект
    public void add(Item item) {
        if (list.containsKey(item))
            list.put(item, list.get(item) + 1);
        else list.put(item, 1);
        price += item.getPrice();
        mass += item.getMass();
    }

    //добавление нескольких предметов в комплект
    public void add(Item item, int num) {
        if (list.containsKey(item))
            list.put(item, list.get(item) + num);
        else list.put(item, num);
        price += item.getPrice() * num;
        mass += item.getMass() * num;
    }

    // метод для создания копирования другого объекта (mutable поведение)
    public void clone(Items items) {
        this.mass = items.getMass();
        this.price = items.getPrice();
        this.list = new HashMap<>(items.getList());
    }

    // приведение к строке для вывода результата
    public String toString() {
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("Оптимальное заполнение рюкзака:\n----------------------------\nСуммарная ценность: ")
                .append(price)
                .append("\nСуммарная масса: ")
                .append(mass);
        for (Item item : list.keySet())
            toPrint.append("\nПредмет весом ")
                    .append(item.getMass())
                    .append(" и стоимостью ")
                    .append(item.getPrice())
                    .append(" - ")
                    .append(list.get(item))
                    .append(" штук.");
        return toPrint.toString();
    }

    public Map<Item, Integer> getList() { return list; }
    public int getPrice() { return price; }
    public int getMass() { return mass; }
}
