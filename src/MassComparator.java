import java.util.Comparator;

class MassComparator implements Comparator<Item> {
    public int compare(Item i1, Item i2) {
        if (i1.getMass().equals(i2.getMass()))
            return i1.getPrice().compareTo(i2.getPrice());
        return i1.getMass().compareTo(i2.getMass());
    }
}