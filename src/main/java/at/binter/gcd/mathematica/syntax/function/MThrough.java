package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.elements.MVariable;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import at.binter.gcd.mathematica.syntax.RowBox;
import at.binter.gcd.mathematica.syntax.group.MParentheses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class MThrough extends MFunction implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MThrough.class);
    public static final String function = "Through";
    public static final String htmlTag = null;

    private String target;

    public MThrough(String target) {
        this.target = target;
    }

    @Override
    public String getFunction() {
        return function;
    }

    @Override
    public String getHtmlTag() {
        return htmlTag;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String getMathematicaExpression() {
        RowBox targetBox = new RowBox();

        targetBox.add(new MVariable(getFunction()));
        targetBox.add(new MVariable("@*"));
        targetBox.add(new MVariable(target));

        targetBox.add(new MExpression("\"[\""));
        if (isLinebreakAfterFunction()) {
            targetBox.add(linebreak);
        }
        Iterator<IExpression> it = getParameters().iterator();
        boolean addDelimiter = false;
        while (it.hasNext()) {
            IExpression expression = it.next();
            if (linebreak == expression) {
                targetBox.add(linebreak);
            } else {
                if (addDelimiter) {
                    targetBox.add(new MExpression("\",\""));
                    if (isLinebreakAfterParameter()) {
                        targetBox.add(linebreak);
                    }
                }
                targetBox.add(expression);
                addDelimiter = true;
            }
        }
        targetBox.add(new MExpression("\"]\""));

        MParentheses parentheses = new MParentheses(targetBox);

        RowBox box2 = new RowBox(parentheses);
        box2.add(new MExpression("\"[\""));
        box2.add(new MExpression("\"t\""));
        box2.add(new MExpression("\"]\""));

        return box2.getMathematicaExpression();
    }
}