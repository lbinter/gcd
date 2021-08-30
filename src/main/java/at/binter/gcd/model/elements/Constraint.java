package at.binter.gcd.model.elements;

import at.binter.gcd.model.HasParameterStringList;
import at.binter.gcd.model.HasVariableStringList;
import at.binter.gcd.model.Updatable;
import at.binter.gcd.util.ParsedFunction;
import at.binter.gcd.util.Tools;

import java.util.ArrayList;
import java.util.List;

public class Constraint implements Updatable<Constraint>, Comparable<Constraint>, HasVariableStringList, HasParameterStringList {
    public int id;
    public String condition;
    private String description;
    protected final List<String> variables = new ArrayList<>();
    protected final List<String> parameters = new ArrayList<>();

    @Override
    public void update(Constraint modified) {
        if (modified.getId() != -1) {
            setId(modified.getId());
        }
        setCondition(modified.getCondition());
        setDescription(modified.getDescription());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        ParsedFunction parsedFunction;
        if (condition.contains(":=")) {
            String[] split = Tools.split(condition.replace("\"", "").trim(), ":=");
            parsedFunction = new ParsedFunction(split[1]);
        } else {
            parsedFunction = new ParsedFunction(condition);
        }
        this.condition = parsedFunction.function;
        variables.addAll(parsedFunction.sortedVariables);
        parameters.addAll(parsedFunction.sortedParameters);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<String> getVariables() {
        return variables;
    }

    @Override
    public List<String> getParameters() {
        return parameters;
    }


    @Override
    public String toString() {
        return String.format("%2d", id) + ": " + condition;
    }

    @Override
    public int compareTo(Constraint o) {
        return condition.compareTo(o.getCondition());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Constraint) {
            return compareTo((Constraint) obj) == 0;
        }
        return false;
    }
}