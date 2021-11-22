import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Backpack {
    private static BigDecimal bp = BigDecimal.valueOf(30);

    public static void main(String[] args) throws IOException {
        System.out.println("Введите параметры вещей для размещения в рюкзаке и его грузоподъемность.\n" +
                "Значения округляются до тысячных долей.\n" +
                "Для добавления предмета - \"вес цена\", для изменения грузоподъемности - \"вес\".\n" +
                "Управление: \"Выход\", \"Начать\" расчет, \"Печать\" параметров на экран, прочитать \"Файл\".");
        final ArrayList<Item> itemList = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        inputItems(input, itemList);
        removeUseless(itemList);
        final Items greedFilled = greed(itemList);
        final ArrayList<Items> results = new ArrayList<>();
        fillTheBackpack(greedFilled, itemList, results);
        System.out.println(topResultToString(results));
        System.out.print("----------------------------\nНажмите Enter для завершения.");
        input.nextLine();
        input.close();
    }

    private static void fillTheBackpack(Items greedFilled, ArrayList<Item> itemList, ArrayList<Items> results) {
        for (int n = itemList.size() - 1; n >= 0; n--)
            iterate(new Items(greedFilled), itemList, n, results);
    }

    private static String topResultToString(ArrayList<Items> results) {
        Collections.sort(results);
        return "Оптимальное заполнение рюкзака:\n" + results.get(0).stringBuilder();
    }

    private static Items greed(ArrayList<Item> items) {
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

    private static void iterate(Items path, ArrayList<Item> itemList, int n, ArrayList<Items> results) {
        for (; n >= 0; n--) {
            BigDecimal availableMass = bp.subtract(path.getMass());
            if (availableMass.compareTo(itemList.get(n).getMass()) >= 0) {
                path.add(itemList.get(n));
                iterate(path, itemList, n, results);
            }
            else if (availableMass.compareTo(itemList.get(0).getMass()) >= 0)
                iterate(new Items(path), itemList, n - 1, results);
            else {
                results.add(path);
                break;
            }
        }
    }

    static void removeUseless(ArrayList<Item> items) {
        Collections.sort(items);
        outsideLoop:
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
                if (useless1 || useless2 || useless3) {
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

    private static void inputItems(Scanner input, ArrayList<Item> items) throws IOException {
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
                    printItems(items);
                    break;
                default:
                    String item = inputStr.replace(",", ".").replaceAll("[^0-9 .]", "");
                    if (item.equals("")){
                        System.out.println("Введено не число!");
                        break;
                    }
                    String[] arr = item.split(" ");
                    if (arr.length == 2) {
                        try {
                            BigDecimal mass = BigDecimal.valueOf(Double.parseDouble(arr[0]));
                            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(arr[1]));
                            if (mass.compareTo(BigDecimal.ZERO) <= 0) {
                                System.out.println("Масса должна быть больше 0.");
                                break;
                            }
                            items.add(new Item(mass, price));
                            System.out.println("Добавлено: масса " + mass + " цена " + price);
                            System.out.println("Сейчас добавлено " + items.size() + " предметов.");
                        } catch (NumberFormatException e) {
                            System.out.println("Ошибка! Введены не числа?");
                        }
                    } else if (arr.length == 1) {
                        bp = BigDecimal.valueOf(Double.parseDouble(arr[0]));
                        System.out.println("Грузоподъемность рюкзака установлена на " + bp);
                    } else {
                        System.out.println("Ошибка. Введены не 2 числа!");
                    }
            }
        }
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
                f.format("|%10.5f|%10.5f|%n", i.getMass(), i.getPrice());
            }
            f.format("-----------------------%n| Макс вес: %10.5f|%n-----------------------", bp);
            print.append(f);
            f.close();
            System.out.println(print);
        }
    }
}