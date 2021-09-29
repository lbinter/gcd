module at.binter.gcd {
    requires org.slf4j;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.bootstrapfx.core;

    requires org.apache.commons.lang3;

    requires jakarta.xml.bind;
    requires com.sun.xml.bind;

    requires JLink;
    requires java.desktop;

    opens at.binter.gcd to javafx.fxml, javafx.controls;
    opens at.binter.gcd.model.xml to jakarta.xml.bind;
    opens at.binter.gcd.controller to javafx.fxml, javafx.controls;

    exports at.binter.gcd;
    exports at.binter.gcd.util;
    exports at.binter.gcd.model;
    exports at.binter.gcd.model.xml;
    exports at.binter.gcd.model.elements;
    exports at.binter.gcd.mathematica;
    exports at.binter.gcd.mathematica.elements;
    exports at.binter.gcd.mathematica.syntax;
    exports at.binter.gcd.mathematica.syntax.unary;
    exports at.binter.gcd.mathematica.syntax.binary;
    exports at.binter.gcd.mathematica.syntax.function;
    exports at.binter.gcd.mathematica.syntax.group;
    exports at.binter.gcd.controller;
}