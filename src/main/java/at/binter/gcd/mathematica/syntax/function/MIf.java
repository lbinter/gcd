package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.mathematica.syntax.IExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MIf extends MBase implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MIf.class);
    public static final String function = "If";
    public static final String delimiter = ", ";

    private IExpression condition;
    private IExpression expressionTrue;
    private IExpression expressionFalse;

    private boolean linebreakBeforeFunction = false;
    private boolean linebreakAfterCondition = false;
    private boolean linebreakAfterTrue = false;
    private boolean linebreakAfter = false;

    public MIf() {
    }

    public MIf(IExpression condition) {
        setCondition(condition);
    }

    public MIf(IExpression condition, IExpression expressionTrue) {
        setCondition(condition);
        setExpressionTrue(expressionTrue);
    }

    public MIf(IExpression condition, IExpression expressionTrue, IExpression expressionFalse) {
        setCondition(condition);
        setExpressionTrue(expressionTrue);
        setExpressionFalse(expressionFalse);
    }

    public IExpression getCondition() {
        return condition;
    }

    public void setCondition(IExpression condition) {
        this.condition = condition;
    }

    public IExpression getExpressionTrue() {
        return expressionTrue;
    }

    public void setExpressionTrue(IExpression expressionTrue) {
        this.expressionTrue = expressionTrue;
    }

    public IExpression getExpressionFalse() {
        return expressionFalse;
    }

    public void setExpressionFalse(IExpression expressionFalse) {
        this.expressionFalse = expressionFalse;
    }

    public boolean isLinebreakBeforeFunction() {
        return linebreakBeforeFunction;
    }

    public void setLinebreakBeforeFunction(boolean linebreakBeforeFunction) {
        this.linebreakBeforeFunction = linebreakBeforeFunction;
    }

    public boolean isLinebreakAfterCondition() {
        return linebreakAfterCondition;
    }

    public void setLinebreakAfterCondition(boolean linebreakAfterCondition) {
        this.linebreakAfterCondition = linebreakAfterCondition;
    }

    public boolean isLinebreakAfterTrue() {
        return linebreakAfterTrue;
    }

    public void setLinebreakAfterTrue(boolean linebreakAfterTrue) {
        this.linebreakAfterTrue = linebreakAfterTrue;
    }

    public boolean isLinebreakAfter() {
        return linebreakAfter;
    }

    public void setLinebreakAfter(boolean linebreakAfter) {
        this.linebreakAfter = linebreakAfter;
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        int intent = 0;
        if (linebreakBeforeFunction) {
            builder.linebreak();
            builder.increaseIndent(4);
            intent += 4;
        }
        builder.write(function);
        builder.write("[");
        condition.toHTML(builder);
        builder.write(delimiter);
        if (linebreakAfterCondition) {
            builder.linebreak();
            builder.increaseIndent(4);
            intent += 4;
        }
        expressionTrue.toHTML(builder);
        if (expressionFalse != null) {
            builder.write(delimiter);
            if (linebreakAfterTrue) {
                builder.linebreak();
                if (!linebreakAfterCondition) {
                    builder.increaseIndent(4);
                    intent += 4;
                }
            }
            expressionFalse.toHTML(builder);
        }
        builder.decreaseIndent(intent);
        builder.write("]");
        if (linebreakAfter) {
            builder.linebreak();
        }
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        return toHTML();
    }

    @Override
    public String getMathematicaExpression() {
        // TODO implement me
        return "";
    }
}