package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.mathematica.elements.MVariable;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import at.binter.gcd.mathematica.syntax.RowBox;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class MFunction extends MBase implements IExpression {
    public static final IExpression linebreak = new MExpression("empty");
    protected final List<IExpression> parameters = new ArrayList<>();
    private String delimiter = ", ";
    private boolean linebreakAfterFunction = false;
    private boolean linebreakAfterParameter = false;

    public MFunction() {
    }

    public MFunction(IExpression... parameters) {
        super();
        addParameters(parameters);
    }

    public abstract String getFunction();

    public abstract String getHtmlTag();

    public List<IExpression> getParameters() {
        return parameters;
    }

    public void addParameters(IExpression... params) {
        if (params == null || params.length == 0) return;
        for (IExpression e : params) {
            addParameter(e);
        }
    }

    public void addParameters(String... params) {
        if (params == null || params.length == 0) return;
        for (String s : params) {
            addParameter(s);
        }
    }

    public void addParameter(IExpression expr) {
        if (expr == null) return;
        parameters.add(expr);
    }

    public void addParameter(String expr) {
        if (StringUtils.isBlank(expr)) return;
        parameters.add(new MExpression("parameter", expr));
    }

    public void addLinebreak() {
        parameters.add(linebreak);
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isLinebreakAfterFunction() {
        return linebreakAfterFunction;
    }

    public boolean isLinebreakAfterParameter() {
        return linebreakAfterParameter;
    }

    public void setLinebreakAfterParameter(boolean linebreakAfterParameter) {
        this.linebreakAfterParameter = linebreakAfterParameter;
    }

    public void setLinebreakAfterFunction(boolean linebreakAfterFunction) {
        this.linebreakAfterFunction = linebreakAfterFunction;
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
    public void toHTML(HTMLBuilder builder) {
        builder.write(getFunction());
        builder.write("[");
        if (linebreakAfterFunction) {
            builder.linebreak();
            builder.increaseIndent(4);
        }
        Iterator<IExpression> it = getParameters().iterator();
        boolean increaseIndent = true;
        while (it.hasNext()) {
            IExpression expression = it.next();
            if (linebreak == expression) {
                builder.linebreak();
            } else {
                expression.toHTML(builder);
                if (it.hasNext()) {
                    builder.write(delimiter);
                    if (linebreakAfterParameter) {
                        builder.linebreak();
                        if (!linebreakAfterFunction && increaseIndent) {
                            increaseIndent = false;
                            builder.increaseIndent(4);
                        }
                    }
                }
            }
        }
        if (linebreakAfterFunction) {
            builder.decreaseIndent(4);
        }
        if (!increaseIndent) {
            builder.decreaseIndent(4);
        }
        builder.write("]");
    }

    @Override
    public String getMathematicaExpression() {
        boolean isMThrough = this instanceof MThrough;
        RowBox box = new RowBox();
        if (this instanceof MQuietNDSolve) {
            RowBox ndSolve = new RowBox();
            ndSolve.add(new MVariable("Quiet"));
            ndSolve.add(new MVariable("@"));
            ndSolve.add(new MVariable("NDSolve"));
            box.add(ndSolve);
        } else if (isMThrough) {
            String target = ((MThrough) this).getTarget();
            RowBox through = new RowBox();
            RowBox targetBox = new RowBox();
            targetBox.add(new MVariable(target));
            box.add(new MExpression("\"(\""));
            through.add(new MVariable(getFunction()));
            through.add(new MVariable("@*"));
            box.add(through);
            box.add(targetBox);
        } else {
            box.add(new MExpression(getFunction()));
        }
        box.add(new MExpression("\"[\""));
        if (linebreakAfterFunction) {
            box.add(linebreak);
        }
        Iterator<IExpression> it = getParameters().iterator();
        boolean addDelimiter = false;
        while (it.hasNext()) {
            IExpression expression = it.next();
            if (linebreak == expression) {
                box.add(linebreak);
            } else {
                if (addDelimiter) {
                    box.add(new MExpression("\",\""));
                    if (linebreakAfterParameter) {
                        box.add(linebreak);
                    }
                }
                box.add(expression);
                addDelimiter = true;
            }
        }
        box.add(new MExpression("\"]\""));
        if (isMThrough) {
            box.add(new MExpression("\")\""));
            box.add(new MExpression("\"[\""));
            box.add(new MExpression("\"t\""));
            box.add(new MExpression("\"]\""));
        }
        return box.getMathematicaExpression();
    }
}