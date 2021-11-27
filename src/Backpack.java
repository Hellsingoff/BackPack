import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Backpack {
    private static int bp = 30;

    public static void main(String[] args) {
        System.out.println("Введите параметры вещей для размещения в рюкзаке и его грузоподъемность.\n" +
                "Значения округляются до тысячных долей.\n" +
                "Для добавления предмета - \"вес цена\", для изменения грузоподъемности - \"вес\".\n" +
                "Управление: \"Выход\", \"Начать\" расчет, \"Печать\" параметров на экран, прочитать \"Файл\".");
        Scanner input = new Scanner(System.in);
        final ArrayList<Item> itemList = inputItems(input);
        removeUseless(itemList);
        final Items greedFilled = greed(itemList);
        Items result = fillTheBackpack(greedFilled, itemList);
        System.out.println(result);
        System.out.print("----------------------------\nНажмите Enter для завершения.");
        input.nextLine();
        input.close();
    }

    protected static Items fillTheBackpack(Items greedFilled, ArrayList<Item> itemList){
        Items result = new Items();
        fillTheBackpack(greedFilled, itemList, itemList.size() - 1, result);
        return result;
    }

    protected static void fillTheBackpack(Items greedFilled, ArrayList<Item> itemList, int n, Items result){
        for (; n >= 0; n--) {
            Items items = new Items(greedFilled);
            for (int j = n; j >= 0; j--) {
                int availableMass = bp - items.getMass();
                if (availableMass >= itemList.get(j).getMass()) {
                    items.add(itemList.get(j));
                    fillTheBackpack(items, itemList, j, result);
                } else if (availableMass >= itemList.get(0).getMass())
                    fillTheBackpack(items, itemList, j - 1, result);
                else {
                    if (result.getPrice() < items.getPrice())
                        result.clone(items);
                    break;
                }
            }
        }
    }

    static Items greed(ArrayList<Item> items) {
        int min = Integer.MAX_VALUE;
        Item topItem = new Item(1, 1);
        for (Item item : items)
            if (item.getGreed() >= topItem.getGreed())
                topItem = item;
        int greedFilled = bp/topItem.getMass();
        double topGreed = topItem.getGreed();
        for (Item item : items) {
            double temp = topGreed*2 - item.getGreed();
            int num = (int) (topGreed / temp * greedFilled);
            if (num < min) min = num - 1;
        }
        Items result = new Items();
        if (min > 0 && min < Integer.MAX_VALUE) result.add(topItem, min);
        return result;
    }

    static void removeUseless(ArrayList<Item> items) {
        Collections.sort(items);
        outsideLoop:
        for (int n = items.size() - 1; n >= 0; n--) {
            int firstMass = items.get(n).getMass();
            if (firstMass > bp || items.get(n).getPrice() <= 0) {
                items.remove(n);
                continue;
            }
            for (int j = n - 1; j >= 0; j--) {
                boolean useless1 = firstMass/items.get(j).getMass()*items.get(j).getPrice() >= items.get(n).getPrice();
                boolean useless2 = items.get(n).getPrice() < items.get(j).getPrice();
                if (useless1 || useless2) {
                    items.remove(n);
                    continue outsideLoop;
                }
            }
        }
        if (items.size() == 0) {
            System.out.println("В рюкзак ничего нельзя добавить.");
            System.exit(0);
        }
    }

    private static ArrayList<Item> inputItems(Scanner input) {
        ArrayList<Item> itemList = new ArrayList<>();
        String inputStr;
        label:
        while (true) {
            inputStr = input.nextLine().toLowerCase().trim();
            switch (inputStr) {
                case "файл":
                    String path = new File("").getAbsolutePath() + File.separator + "backpack.txt";
                    try {
                        Scanner reader = new Scanner(new File(path));
                        if (reader.hasNextInt())
                            bp = reader.nextInt();
                        while (reader.hasNextLine()) {
                            String line = reader.nextLine();
                            if (line.isBlank()) continue;
                            String[] item = line.split(" ");
                            itemList.add(new Item(Integer.parseInt(item[0]), Integer.parseInt(item[1])));
                        }
                        reader.close();
                        break label;
                    } catch (FileNotFoundException e) {
                        System.out.println("Файл Backpack.txt в директории программы не найден!");
                    }
                    break;
                case "начать":
                    if (itemList.size() > 0) break label;
                    else System.out.println("Сначала добавьте предметы!");
                    break;
                case "выход":
                    System.exit(0);
                case "печать":
                    printItems(itemList);
                    break;
                default:
                    String item = inputStr.replace(",", ".").replaceAll("[^0-9 ]", "");
                    if (item.equals("")){
                        System.out.println("Введено не число!");
                        break;
                    }
                    String[] arr = item.split(" ");
                    if (arr.length == 2) {
                        try {
                            int mass = Integer.parseInt(arr[0]);
                            int price = Integer.parseInt(arr[1]);
                            if (mass <= 0) {
                                System.out.println("Масса должна быть больше 0.");
                                break;
                            }
                            itemList.add(new Item(mass, price));
                            System.out.println("Добавлено: масса " + mass + " цена " + price);
                            System.out.println("Сейчас добавлено " + itemList.size() + " предметов.");
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка! Введены не числа?");
                        }
                    } else if (arr.length == 1) {
                        bp = Integer.parseInt(arr[0]);
                        System.out.println("Грузоподъемность рюкзака установлена на " + bp);
                    } else {
                        System.out.println("Ошибка. Введены не 2 числа!");
                    }
            }
        }
        return itemList;
    }

    private static void printItems(ArrayList<Item> items) {
        if (items.size() == 0)
            System.out.println("Предметы не добавлены.\nМакс вес: " + bp);
        else {
            StringBuilder print = new StringBuilder();
            print.append("-----------------------\n")
                    .append("|    Вес   |   Цена   |\n")
                    .append("-----------------------\n");
            Formatter f = new Formatter();
            for (Item i : items) {
                f.format("|%10d|%10d|%n", i.getMass(), i.getPrice());
            }
            f.format("-----------------------%n| Макс вес: %10d|%n-----------------------", bp);
            print.append(f);
            f.close();
            System.out.println(print);
        }
    }
}