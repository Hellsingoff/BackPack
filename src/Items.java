import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Items implements Comparable<Items> {
    private final Map<Item, Integer> list;
    private BigDecimal price, mass;

    Items() {
        list = new HashMap<>();
        price = BigDecimal.ZERO;
        mass = BigDecimal.ZERO;
    }

    Items(Items items) {
        list = items.getList();
        price = items.getPrice();
        mass = items.getMass();
    }

    public void add(Item item) {
        if (list.containsKey(item))
            list.put(item, list.get(item) + 1);
        else list.put(item, 1);
        price = price.add(item.getPrice());
        mass = mass.add(item.getMass());
    }

    public void add(Item item, int num) {
        if (list.containsKey(item))
            list.put(item, list.get(item) + num);
        else list.put(item, num);
        price = price.add(item.getPrice().multiply(BigDecimal.valueOf(num)));
        mass = mass.add(item.getMass().multiply(BigDecimal.valueOf(num)));
    }

    public StringBuilder stringBuilder() {
        System.out.println("----------------------------");
        System.out.println("Суммарная ценность: " + price);
        System.out.println("Суммарная масса: " + mass);
        for (Item item : list.keySet()) {
            System.out.println("Предмет весом " + item.getMass() + " и стоимостью " +
                    item.getPrice() + " - " + list.get(item) + " штук.");
        }
    }

    public int compareTo(Items items) {
        return items.getPrice().compareTo(price);
    }

    public Map<Item, Integer> getList() { return list; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getMass() { return mass; }
}
