package at.binter.gcd.model;

import javafx.css.PseudoClass;

public enum Status {
    VALID("valid"),
    VALID_HAS_VALUES("valid-has-values"),
    VALID_HAS_FUNCTION("valid-has-function"),
    VALID_AUTOMATIC("valid-automatic"),
    INVALID("invalid");

    public final String cssClass;
    public final PseudoClass pseudoClass;

    Status(String cssClass) {
        this.cssClass = cssClass;
        pseudoClass = PseudoClass.getPseudoClass(cssClass);
    }
}