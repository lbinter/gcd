package at.binter.gcd.model.elements;

import at.binter.gcd.model.*;
import at.binter.gcd.util.ParsedFunction;
import at.binter.gcd.util.Tools;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

import static at.binter.gcd.model.Status.INVALID;
import static at.binter.gcd.model.Status.VALID;
import static at.binter.gcd.util.GuiUtils.sanitizeString;

public class Constraint implements Updatable<Constraint>, Comparable<Constraint>, HasVariableStringList, HasParameterStringList {
    private int id;
    private String name;
    private final StringProperty condition = new SimpleStringProperty();
    private String mathematicaCondition;
    private boolean mathematicaConditionSet = false;
    private String mathematicaString;
    private boolean mathematicaStringSet = false;
    private String description;

    private final VariableParameterList variableParameterList = new VariableParameterList();

    @Override
    public void update(Constraint modified) {
        if (modified.getId() != -1) {
            setId(modified.getId());
        }
        setName(modified.name);
        setCondition(modified.getCondition());
        setDescription(modified.getDescription());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameOrId() {
        if (StringUtils.isBlank(name)) {
            return String.format("%2d", id);
        }
        return name;
    }

    public StringProperty conditionProperty() {
        return condition;
    }

    public String getCondition() {
        return conditionProperty().get();
    }

    public void setCondition(String condition) {
        mathematicaCondition = null;
        mathematicaConditionSet = false;
        ParsedFunction parsedFunction;
        if (condition.contains(":=")) {
            String[] split = Tools.split(condition.replace("\"", "").trim(), ":=");
            parsedFunction = new ParsedFunction(split[1]);
        } else {
            parsedFunction = new ParsedFunction(condition);
        }
        fillVariables(parsedFunction.variables);
        fillParameters(parsedFunction.parameters);
        conditionProperty().set(parsedFunction.function);
    }

    public String getMathematicaCondition() {
        return mathematicaCondition;
    }

    public void setMathematicaCondition(String mathematicaCondition) {
        if (StringUtils.isBlank(mathematicaCondition)) {
            return;
        }
        this.mathematicaCondition = mathematicaCondition;
        mathematicaConditionSet = true;
    }

    public boolean isMathematicaConditionSet() {
        return mathematicaConditionSet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = sanitizeString(description);
    }

    public void fillParameters(Set<String> newParameters) {
        variableParameterList.fillParameters(newParameters);
    }

    public void fillVariables(Set<String> newVariables) {
        variableParameterList.fillVariables(newVariables);
    }

    @Override
    public List<String> getVariables() {
        return variableParameterList.getVariables();
    }

    @Override
    public Set<String> getVariablesRemoved() {
        return variableParameterList.getVariablesRemoved();
    }

    @Override
    public Set<String> getVariablesAdded() {
        return variableParameterList.getVariablesAdded();
    }

    @Override
    public List<String> getParameters() {
        return variableParameterList.getParameters();
    }

    @Override
    public Set<String> getParametersRemoved() {
        return variableParameterList.getParametersRemoved();
    }

    @Override
    public Set<String> getParametersAdded() {
        return variableParameterList.getParametersAdded();
    }

    @Override
    public String toString() {
        return getNameOrId() + ": " + getCondition();
    }

    @Override
    public int compareTo(Constraint o) {
        int nameCompare = StringUtils.compare(getName(), o.getName());
        return nameCompare == 0 ? Integer.compare(getId(), o.getId()) : nameCompare;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Constraint) {
            return compareTo((Constraint) obj) == 0;
        }
        return false;
    }

    public String toStringRaw() {
        return id + ":=" + getCondition();
    }

    public String getMathematicaString() {
        return mathematicaString;
    }

    public void setMathematicaString(String mathematicaString) {
        if (StringUtils.isBlank(mathematicaString)) {
            return;
        }
        this.mathematicaString = mathematicaString;
        mathematicaStringSet = true;
    }

    public boolean isMathematicaStringSet() {
        return mathematicaStringSet;
    }

    public Status getStatus() {
        return StringUtils.isNotBlank(getCondition()) ? VALID : INVALID;
    }
}