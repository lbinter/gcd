package at.binter.gcd.controller;

public interface Editor<T> {
    void createEditor(T dataObject);

    boolean hasData();

    T createDataObject();
}