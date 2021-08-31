package at.binter.gcd.model;

import java.util.*;
import java.util.stream.Collectors;

public class VariableParameterList implements HasVariableStringList, HasParameterStringList {
    private final List<String> variables = new ArrayList<>();
    private final Set<String> variablesRemoved = new HashSet<>();
    private final Set<String> variablesAdded = new HashSet<>();

    private final List<String> parameters = new ArrayList<>();
    private final Set<String> parametersRemoved = new HashSet<>();
    private final Set<String> parametersAdded = new HashSet<>();

    public void fillParameters(Set<String> newParameters) {
        parametersRemoved.clear();
        parametersAdded.clear();

        parametersRemoved.addAll(parameters.stream()
                .filter(x -> !newParameters.contains(x))
                .collect(Collectors.toSet()));
        parametersAdded.addAll(newParameters.stream()
                .filter(x -> !parameters.contains(x))
                .collect(Collectors.toSet()));

        parameters.clear();
        parameters.addAll(newParameters);

        Collections.sort(parameters);
    }

    public void fillVariables(Set<String> newVariables) {
        variablesRemoved.clear();
        variablesAdded.clear();

        //variablesAdded.addAll(variables.stream().filter(obj1 -> parsedFunction.sortedVariables.stream().noneMatch(obj1::equalsIgnoreCase)).collect(Collectors.toList()));

        variablesRemoved.addAll(variables.stream()
                .filter(x -> !newVariables.contains(x))
                .collect(Collectors.toSet()));
        variablesAdded.addAll(newVariables.stream()
                .filter(x -> !variables.contains(x))
                .collect(Collectors.toSet()));

        variables.clear();
        variables.addAll(newVariables);

        Collections.sort(variables);
    }

    @Override
    public List<String> getVariables() {
        return variables;
    }

    @Override
    public Set<String> getVariablesRemoved() {
        return variablesRemoved;
    }

    @Override
    public Set<String> getVariablesAdded() {
        return variablesAdded;
    }

    @Override
    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public Set<String> getParametersRemoved() {
        return parametersRemoved;
    }

    @Override
    public Set<String> getParametersAdded() {
        return parametersAdded;
    }
}
