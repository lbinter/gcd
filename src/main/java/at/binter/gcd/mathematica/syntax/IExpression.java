package at.binter.gcd.mathematica.syntax;

import at.binter.gcd.mathematica.HTMLWrapper;

public interface IExpression extends HTMLWrapper {
    String getCssClass();

    String getExpression();
}