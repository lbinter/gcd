package at.binter.gcd.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Pair;

public class CheckedListViewCheckObserver<T> extends SimpleObjectProperty<Pair<T, Boolean>> {
    public BooleanProperty getObserverForObject(T object) {
        BooleanProperty value = new SimpleBooleanProperty(false);
        value.addListener((observable, oldValue, newValue) -> {
            CheckedListViewCheckObserver.this.set(new Pair<>(object, newValue));
        });
        return value;
    }
}