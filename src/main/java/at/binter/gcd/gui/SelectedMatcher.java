package at.binter.gcd.gui;


import java.util.List;
import java.util.function.Predicate;

public class SelectedMatcher<T> implements Predicate<T> {

    private final List<T> selected;

    public SelectedMatcher(List<T> selected) {
        this.selected = selected;
    }

    @Override
    public boolean test(T item) {
        if (item == null) return false;
        if (selected == null || selected.isEmpty()) {
            return true;
        }
        return !selected.contains(item);
    }
}