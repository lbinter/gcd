package at.binter.gcd.model.elements;

import at.binter.gcd.util.ParsedFunction;
import at.binter.gcd.util.Tools;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Function {
    protected String name;
    protected String function;
    protected final List<String> variables = new ArrayList<>();
    protected final List<String> parameters = new ArrayList<>();

    public abstract String getAssignmentSymbol();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        if (StringUtils.isBlank(function)) {
            this.function = "";
        } else {
            ParsedFunction parsedFunction;
            if (function.contains(getAssignmentSymbol())) {
                String[] split = Tools.split(function.replace("\"", "").trim(), getAssignmentSymbol());
                setName(split[0].replace("[t]", "").trim());
                parsedFunction = new ParsedFunction(getName(), split[1].trim(), getAssignmentSymbol());
            } else {
                parsedFunction = new ParsedFunction(getName(), function.trim(), getAssignmentSymbol());
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