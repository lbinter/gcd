package at.binter.gcd.mathematica.syntax.group;

import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.mathematica.syntax.IExpression;

public abstract class MGroup extends MBase implements IExpression {
    public abstract String getGroupStartSymbol();

    public abstract String getGroupCloseSymbol();
}