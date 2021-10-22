package at.binter.gcd.model.elements;

import at.binter.gcd.model.HasParameterStringList;
import at.binter.gcd.model.HasVariableStringList;
import at.binter.gcd.model.VariableParameterList;
import at.binter.gcd.util.ParsedFunction;
import at.binter.gcd.util.Tools;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

import static at.binter.gcd.util.GuiUtils.sanitizeString;

public abstract class Function implements HasVariableStringList, HasParameterStringList {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty function = new SimpleStringProperty();
    private final VariableParameterList variableParameterList = new VariableParameterList();
    private String mathematicaFunction;
    private boolean mathematicaFunctionSet = false;
    private String mathematicaString;
    private boolean mathematicaStringSet = false;
    protected String parameter;

    public abstract String getAssignmentSymbol();

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty functionProperty() {
        return function;
    }

    public String getName() {
        return nameProperty().get();
    }

    public void setName(String name) {
        if (name == null) return;
        name = sanitizeString(name);
        if (name.contains("[t]")) {
            name = name.replace("[t]", "");
            setParameter("t");
            // TODO fix that only last occurrence and greek \[greek] works
        }
        nameProperty().set(name);
    }

    public String getFunction() {
        return functionProperty().get();
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = sanitizeString(parameter);
    }

    public String getCompleteName() {
        return getName() + "[" + parameter + "]";
    }

    public void setFunction(String function) {
        mathematicaFunction = null;
        mathematicaFunctionSet = false;
        if (StringUtils.isBlank(function)) {
            functionProperty().set("");
        } else {
            ParsedFunction parsedFunction;
            if (function.contains(getAssignmentSymbol())) {
                String[] split = Tools.split(function.replace("\"", "").trim(), getAssignmentSymbol());
                setName(split[0]);
                parsedFunction = new ParsedFunction(split[1]);
            } else {
                parsedFunction = new ParsedFunction(function);
            }
            fillVariables(parsedFunction.variables);
            fillParameters(parsedFunction.parameters);
            functionProperty().set(parsedFunction.function);
        }
    }

    public VariableParameterList getVariableParameterList() {
        return variableParameterList;
    }

    public String getMathematicaFunction() {
        return mathematicaFunction;
    }

    public void setMathematicaFunction(String mathematicaFunction) {
        if (StringUtils.isBlank(mathematicaFunction)) {
            return;
        }
        this.mathematicaFunction = mathematicaFunction;
        mathematicaFunctionSet = true;
    }

    public boolean isMathematicaFunctionSet() {
        return mathematicaFunctionSet;
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

}