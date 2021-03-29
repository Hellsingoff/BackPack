import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Backpack {
    private static BigDecimal bp = BigDecimal.valueOf(30);
    private static final ArrayList<Items> results = new ArrayList<>();
    private static final ArrayList<Item> items = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Введите параметры вещей для размещения в рюкзаке и его грузоподъемность.\n" +
                "Значения округляются до тысячных долей.\n" +
                "Для добавления предмета - \"вес цена\", для изменения грузоподъемности - \"вес\".\n" +
                "Управление: \"Выход\", \"Начать\" расчет, \"Печать\" параметров на экран, прочитать \"Файл\".");
        Scanner input = new Scanner(System.in);
        inputItems(input);
        removeUseless();
        final Items greedFilled = greed();
        for (int n = items.size() - 1; n >= 0; n--)
            fillTheBackpack(n, new Items(greedFilled));
        topResultPrint();
        System.out.print("Нажмите Enter для завершения.");
        input.nextLine();
        input.close();
    }

    // проверено, тестировать правильность выбора
    private static void topResultPrint() {
        System.out.println("Оптимальное заполнение рюкзака:");
        Collections.sort(results);
        results.get(0).print();
    }

    // проверено, тест формулу
    private static Items greed() {
        int min = Integer.MAX_VALUE;
        Item topItem = new Item(BigDecimal.ONE, BigDecimal.ZERO);
        for (Item item : items)
            if (item.getGreed().compareTo(topItem.getGreed()) >= 0)
                topItem = item;
        int greedFilled = bp.divide(topItem.getMass(), RoundingMode.FLOOR).intValue();
        BigDecimal topGreed = topItem.getGreed();
        for (Item item : items) {
            BigDecimal temp = topGreed.multiply(BigDecimal.valueOf(2)).subtract(item.getGreed());
            int num = topGreed.divide(temp, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(greedFilled)).intValue();
            if (num < min) min = num;
        }
        Items result = new Items();
        if (min > 0) result.add(topItem, min);
        return result;
    }

    // проверено, протестировать перебор
    private static void fillTheBackpack(int n, Items path) {
        for (; n >= 0; n--) {
            BigDecimal availableMass = bp.subtract(path.getMass());
            if (availableMass.compareTo(items.get(n).getMass()) >= 0) {
                path.add(items.get(n));
                fillTheBackpack(n, path);
            }
            else if (availableMass.compareTo(items.get(0).getMass()) >= 0)
                fillTheBackpack(n - 1, new Items(path));
            else {
                results.add(path);
                break;
            }
        }
    }

    // проверено, тест сортировки и удаления
    static void removeUseless() {
        Collections.sort(items);
        for (int n = items.size() - 1; n >= 0; n--) {
            BigDecimal firstMass = items.get(n).getMass();
            if (firstMass.compareTo(bp) > 0 || items.get(n).getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                items.remove(n);
                continue;
            }
            for (int j = n - 1; j >= 0; j--) {
                boolean useless1 = firstMass.divide(items.get(j).getMass(), RoundingMode.FLOOR)
                        .multiply(items.get(j).getPrice()).compareTo(items.get(n).getPrice()) >= 0;
                boolean useless2 = firstMass.equals(items.get(j).getMass());
                boolean useless3 = items.get(n).getPrice().compareTo(items.get(j).getPrice()) < 0;
                if (useless1 || useless2 || useless3)
                    items.remove(n);
            }
        }
        if (items.size() == 0) {
            System.out.println("В рюкзак ничего нельзя добавить.");
            System.exit(0);
        }
    }

    // проверено, тест добавления вещей, убрать открытие файла
    private static void inputItems(Scanner input) throws IOException {
        String inputStr;
        label:
        while (true) {
            inputStr = input.nextLine().toLowerCase().trim();
            switch (inputStr) {
                case "файл":
                    String path = new File("").getAbsolutePath() + File.separator + "Backpack.txt";
                    try {
                        Scanner reader = new Scanner(new File(path));
                        if (reader.hasNextBigDecimal())
                            bp = reader.nextBigDecimal();
                        while (reader.hasNextLine()) {
                            String line = reader.nextLine();
                            if (line.isBlank()) continue;
                            String[] item = line.split(" ");
                            BigDecimal mass = BigDecimal.valueOf(Double.parseDouble(item[0]));
                            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(item[1]));
                            items.add(new Item(mass, price));
                        }
                        reader.close();
                        Desktop.getDesktop().edit(new File(path)); //test
                    } catch (FileNotFoundException e) {
                        System.out.println("Файл Backpack.txt в директории программы не найден!");
                    }
                    break;
                case "начать":
                    if (items.size() > 0) break label;
                    else System.out.println("Сначала добавьте предметы!");
                    break;
                case "выход":
                    System.exit(0);
                case "печать":
                    printItems();
                    break;
                default:
                    String item = inputStr.replace(",", ".").replaceAll("[^0-9 .]", "");
                    String[] arr = item.split(" ");
                    if (arr.length == 2) {
                        try {
                            BigDecimal mass = BigDecimal.valueOf(Double.parseDouble(arr[0]));
                            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(arr[1]));
                            if (mass.compareTo(BigDecimal.ZERO) <= 0) {
                                System.out.println("Масса должна быть больше 0.");
                                continue label;
                            }
                            items.add(new Item(mass, price));
                            System.out.println("Добавлено: масса " + mass + " цена " + price);
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка! Введены не числа?");
                        }
                    } else if (arr.length == 1) {
                        bp = BigDecimal.valueOf(Double.parseDouble(arr[0]));
                        System.out.println("Грузоподъемность рюкзака установлена на " + bp);
                    } else System.out.println("Ошибка. Введены не 2 числа!");
                    System.out.println("Сейчас добавлено " + items.size() + " предметов.");
                    break;
            }
        }
    }

    private static void printItems() {
        if (items.size() == 0)
            System.out.println("Предметы не добавлены.\nМакс вес: " + bp);
        else {
            StringBuilder print = new StringBuilder();
            print.append("-----------------------\n")
                    .append("|    Вес   |   Цена   |\n")
                    .append("-----------------------\n");
            Formatter f = new Formatter();
            for (Item i : items) {
                f.format("|%10.5f|%10.5f|%n", i.getMass(), i.getPrice());
            }
            f.format("| Макс вес: %10.5f|%n", bp);
            print.append(f);
            f.close();
            System.out.println(print);
        }
    }
}