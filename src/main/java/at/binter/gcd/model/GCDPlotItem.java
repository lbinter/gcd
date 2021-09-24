package at.binter.gcd.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GCDPlotItem<T extends Comparable<T>> implements Comparable<GCDPlotItem<T>> {
    private final BooleanProperty addDepended = new SimpleBooleanProperty(false);
    private final T item;

    public GCDPlotItem(T item) {
        this.item = item;
    }

    public boolean isAddDepended() {
        return addDepended.get();
    }

    public BooleanProperty addDependedProperty() {
        return addDepended;
    }

    public void setAddDepended(boolean addDepended) {
        this.addDepended.set(addDepended);
    }

    public T getItem() {
        return item;
    }

    @Override
    public String toString() {
        return item.toString();
    }

    @Override
    public int compareTo(GCDPlotItem<T> o) {
        return getItem().compareTo(o.getItem());
    }
}