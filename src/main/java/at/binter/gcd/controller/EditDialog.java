package at.binter.gcd.controller;

import at.binter.gcd.GCDApplication;
import at.binter.gcd.model.Updatable;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;

public record EditDialog<T extends Updatable<T>>(ListView<T> listView,
                                                 ObservableList<T> list,
                                                 Editor<T> editor, GCDApplication gcd) {

    public void editSelectedValue() {
        final int selectedIndex = getGCDModelSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        T selected = list.get(selectedIndex);
        editor.createEditor(selected);

        if (editor.hasData()) {
            T modified = editor.createDataObject();
            selected.update(modified);
            listView.refresh();
        }
    }

    public void addNewItem() {
        editor.createEditor(null);
        if (editor.hasData()) {
            list.add(editor.createDataObject());
        }
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
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return -1;
        }
        if (listView.getItems() instanceof SortedList) {
            return ((SortedList<T>) listView.getItems()).getSourceIndex(selectedIndex);
        } else {
            return selectedIndex;
        }
    }
}