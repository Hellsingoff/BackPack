import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class Backpack {
    private static int bp = 30000;
    private static String result = "";

    public static void main(String[] args) throws IOException {
        System.out.println("Введите параметры вещей для размещения в рюкзаке и его грузоподъемность.\n" +
                "Значения округляются до тысячных долей.\n" +
                "Для добавления предмета - \"вес цена\", для изменения грузоподъемности - \"вес\".\n" +
                "Управление: \"Выход\", \"Начать\" расчет, \"Печать\" параметров на экран, прочитать \"Файл\".");
        Scanner input = new Scanner(System.in);
        ArrayList<Item> items = inputItems(input);
        removeUseless(items);
        int g = greed(items);
        Set<String> results = new HashSet<>();
        for (int n = 0; n < items.size(); n++)
            fillTheBackpack(g, n, result, items, results);
        double[] print = topResult(results, items);
        resultPrint(print, items);
        System.out.print("Нажмите Enter для завершения.");
        input.nextLine();
        input.close();
    }

    private static void resultPrint(double[] print, ArrayList<Item> items) {
        System.out.println("---------------------------------\n" +
                "Оптимальное заполнение рюкзака:");
        for (int n = 0; n < print.length - 2; n++) {
            if (print[n] > 0)
                System.out.println((int) print[n] + " предметов: " + items.get(n).getMass() +
                        " вес " + items.get(n).getPrice() + " цена.");
        }
        System.out.println("Итоговая ценность: " + Math.round(print[print.length - 2] * 1000000)/1000000.0 +
                "\nЗаполненность весом: " + print[print.length - 1]/1000.0 + " из " + bp/1000.0);
        System.out.println("---------------------------------");
    }

    private static int greed(ArrayList<Item> items) {
        double topGreed = 0;
        int min = Integer.MAX_VALUE, mass = 0;
        Integer item = null;
        for (int m = 0; m < items.size(); m++)
            if (items.get(m).getGreed() > topGreed) {
                topGreed = items.get(m).getGreed();
                item = m;
                mass = items.get(m).getIMass();
            }
        int greedFilled = Math.floorDiv(bp, mass);
        for (Item value : items) {
            int greed = (int) Math.floor((topGreed / (topGreed * 2 - value.getGreed())) * greedFilled);
            if (greed < min) min = greed;
        }
        if (min > 0)
            result = new String(new char[min]).replace("\0", item.toString());
        return bp - min * mass;
    }

    private static double[] topResult(Set<String> results, ArrayList<Item> items) {
        double topPrice = 0;
        long topMass = 0;
        double[] topPath = null;
        for (String s : results) {
            double price = 0;
            long imass = 0;
            double[] path = new double[items.size() + 2];
            for (int i = 0; i < s.length(); i++) {
                int n = Integer.parseInt(s.substring(i, i + 1));
                path[n]++;
                price += items.get(n).getPrice();
                imass += items.get(n).getIMass();
            }
            if (price > topPrice) {
                topPrice = price;
                topMass = imass;
                topPath = path;
            }
        }
        topPath[items.size()] = topPrice;
        topPath[items.size() + 1] = topMass;
        return topPath;
    }

    private static void fillTheBackpack(int bp, int i, String list, ArrayList<Item> items, Set<String> results) {
        for (int n = i; n < items.size(); n++) {
            int imass = items.get(n).getIMass();
            if (bp >= imass) {
                list = list + n;
                bp -= imass;
                fillTheBackpack(bp, n, list, items, results);
            }
            else if (bp >= items.get(items.size() - 1).getIMass())
                fillTheBackpack(bp, n + 1, list, items, results);
            else {
                results.add(list);
                break;
            }
        }
    }

    static void removeUseless(ArrayList<Item> items) {
        MassComparator massComparator = new MassComparator();
        items.sort(massComparator);
        ArrayList<Item> removeItems = new ArrayList<Item>();
        for (int n = 0; n < items.size() - 1; n++) {
            for (int j = n + 1; j < items.size(); j++) {
                boolean useless1 = Math.floor(items.get(n).getIMass()/items.get(j).getIMass())
                        *items.get(j).getPrice() >= items.get(n).getPrice();
                boolean useless2 = (items.get(n).getIMass() == items.get(j).getIMass());
                boolean useless3 = items.get(n).getIMass() > bp;
                boolean useless4 = items.get(n).getPrice() < items.get(j).getPrice();
                if (useless1 || useless2 || useless3 || useless4)
                    removeItems.add(items.get(n));
            }
        }
        if (items.get(items.size() - 1).getIMass() > bp)
            removeItems.add(items.get(items.size() - 1));
        items.removeAll(removeItems);
        if (items.size() == 0) {
            System.out.println("В рюкзак ничего нельзя добавить.");
            System.exit(0);
        }
    }

    private static ArrayList<Item> inputItems(Scanner input) throws IOException {
        ArrayList<Item> items = new ArrayList<Item>();
        String inputStr = "";
        while (true) {
            inputStr = input.nextLine().toLowerCase().trim();
            if (inputStr.equals("файл")) {
                String path = new File("").getAbsolutePath() + "\\Backpack.txt";
                File backPack = new File(path);
                try {
                    BufferedReader back = new BufferedReader(new FileReader(path));
                    String line = back.readLine();
                    if ((line != null) && (!line.isEmpty())) {
                        String[] arrLine = line.split(";");
                        bp = Integer.parseInt(arrLine[0]) * 1000;
                        for (int s = 1; s < arrLine.length; s++) {
                            String[] item = arrLine[s].split(" ");
                            double mass = Math.round(Double.parseDouble(item[0]) * 1000) / 1000.0;
                            double price = Math.round(Double.parseDouble(item[1]) * 1000) / 1000.0;
                            items.add(new Item(mass, price));
                        }
                        back.close();
                        Desktop.getDesktop().edit(backPack);
                    } else {
                        System.out.println("Файл Backpack.txt пуст!");
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Файл Backpack.txt в директории программы не найден!");
                }
            }
            else if (inputStr.equals("начать")) {
                if (items.size() > 0) break;
                else System.out.println("Сначала добавьте предметы!");
            }
            else if (inputStr.equals("выход")) System.exit(0);
            else if (inputStr.equals("печать"))
                System.out.print(printItems(items));
            else {
                String toToken = inputStr.replace(",", ".").replaceAll("[^0-9 .]", "");
                StringTokenizer token = new StringTokenizer(toToken, " ");
                ArrayList<String> arr = new ArrayList<String>();
                while (token.hasMoreTokens()) {
                    String tmp = token.nextToken();
                    if (tmp.length() > 0) arr.add(tmp);
                }
                if (arr.size() == 2) {
                    if (Double.parseDouble(arr.get(0)) < 0.001) {
                        System.out.println("Минимально допустимый вес 0,001.");
                    }
                    else {
                        double mass = Math.round(Double.parseDouble(arr.get(0)) * 1000) / 1000.0;
                        double price = Math.round(Double.parseDouble(arr.get(1)) * 1000) / 1000.0;
                        items.add(new Item(mass, price));
                        System.out.println("Добавлено: масса " + mass + " цена " + price);
                    }
                } else if (arr.size() == 1) {
                    if (Double.parseDouble(arr.get(0)) >= 0.001) {
                        bp = (int) (Double.parseDouble(arr.get(0)) * 1000);
                        System.out.println("Грузоподъемность рюкзака установлена на " + bp/1000.0);
                    } else {
                        System.out.println("Грузоподъемность не должна быть ниже 0.001.");
                    }
                }
                else System.out.println("Ошибка. Введены не 2 числа!");
                System.out.println("Сейчас добавлено " + items.size() + " предметов.");
            }
        }
        return items;
    }
    private static String printItems(ArrayList<Item> items) {
        String result;
        if (items.size() == 0)
            result = "Предметы не добавлены.\nМакс вес: " + bp/1000.0;
        else {
            result = "-----------------------\n" + "|    Вес   |   Цена   |\n" +
                    "-----------------------\n";
            Formatter f = new Formatter();
            for (Item i : items) {
                f.format("|%10.3f|%10.3f|%n", i.getMass(), i.getPrice());
            }
            result += f;
            f.close();
            Formatter b = new Formatter();
            b.format("| Макс вес: %10.3f|%n", bp/1000.0);
            result += "-----------------------\n" + b + "-----------------------\n";
            b.close();
        }
        return result;
    }
}