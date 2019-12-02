import java.util.Comparator;

class MassComparator implements Comparator<Item> {
    public int compare(Item i1, Item i2) {
        if (i1.getIMass() == i2.getIMass()) {
            if (i1.getPrice() == i2.getPrice())
                return 0;
            else if (i1.getPrice() > i2.getPrice())
                return 1;
            else
                return -1;
        }
        else if (i1.getIMass() < i2.getIMass())
            return 1;
        else
            return -1;
    }
}