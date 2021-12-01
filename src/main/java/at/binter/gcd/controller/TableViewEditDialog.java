package at.binter.gcd.controller;

import at.binter.gcd.GCDApplication;
import at.binter.gcd.model.Updatable;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

public record TableViewEditDialog<T extends Updatable<T>>(TableView<T> tableView,
                                                          ObservableList<T> list,
                                                          Editor<T> editor, GCDApplication gcd) {

    public void editSelectedValue() {
        final int selectedIndex = getGCDModelSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        T selected = list.get(selectedIndex);
        editor.showEditor(selected);

        if (editor.hasData()) {
            T modified = editor.createDataObject();
            selected.update(modified);
            gcd.gcdController.model.setSavedToFile(false);
            tableView.refresh();
        }
    }

    public T addNewItem() {
        editor.showEditor(null);
        if (editor.hasData()) {
            T obj = editor.createDataObject();
            list.add(obj);
            return obj;
        }
        return null;
    }

    public void askUserRemoveAlert(String i18nName) {
        final int selectedIndex = getGCDModelSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        T selected = list.get(selectedIndex);
        String title = gcd.getString("list.remove.question.title");
        String message = gcd.getString("list.remove.question.message", gcd.getString(i18nName), selected.toString());
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText(gcd.getString("button.yes"));
        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText(gcd.getString("button.no"));
        alert.showAndWait().filter(response -> response == ButtonType.YES)
                .ifPresent(response -> list.remove(selectedIndex));

    }

    public int getGCDModelSelectedIndex() {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return -1;
        }
        if (tableView.getItems() instanceof SortedList) {
            return ((SortedList<T>) tableView.getItems()).getSourceIndex(selectedIndex);
        } else {
            return selectedIndex;
        }
    }
}