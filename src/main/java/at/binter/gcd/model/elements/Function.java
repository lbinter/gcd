package at.binter.gcd.model.elements;

import at.binter.gcd.util.ParsedFunction;
import at.binter.gcd.util.Tools;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Function {
    protected final StringProperty name = new SimpleStringProperty();
    protected String parameter;
    protected String function;
    protected final List<String> variables = new ArrayList<>();
    protected final List<String> parameters = new ArrayList<>();

    public abstract String getAssignmentSymbol();

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return nameProperty().get();
    }

    public void setName(String name) {
        name = name.trim().replaceAll(" +", " ");
        if (name.contains("[t]")) {
            name = name.replace("[t]", "");
            setParameter("t");
            // TODO fix that only last occurence and greek \[greek] works
        }
        nameProperty().set(name);
    }

    public String getFunction() {
        return function;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setFunction(String function) {
        if (StringUtils.isBlank(function)) {
            this.function = "";
        } else {
            ParsedFunction parsedFunction;
            if (function.contains(getAssignmentSymbol())) {
                String[] split = Tools.split(function.replace("\"", "").trim(), getAssignmentSymbol());
                setName(split[0]);
                parsedFunction = new ParsedFunction(getName(), split[1], getAssignmentSymbol());
            } else {
                parsedFunction = new ParsedFunction(getName(), function, getAssignmentSymbol());
            }
            this.function = parsedFunction.function;
            variables.addAll(parsedFunction.sortedVariables);
            parameters.addAll(parsedFunction.sortedParameters);
        }
    }

    public List<String> getVariables() {
        return variables;
    }

    public List<String> getParameters() {
        return parameters;
    }
}