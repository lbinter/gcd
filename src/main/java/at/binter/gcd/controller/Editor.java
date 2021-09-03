package at.binter.gcd.controller;

public interface Editor<T> {
    void showEditor(T dataObject);

    boolean hasData();

    T createDataObject();
}