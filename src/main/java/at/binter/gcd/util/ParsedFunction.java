package at.binter.gcd.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ParsedFunction {
    private static final Logger log = LoggerFactory.getLogger(ParsedFunction.class);
    public final String assignmentSymbol;
    public final String name;
    public final String function;
    public final Set<String> variables = new HashSet<>();
    public final Set<String> parameters = new HashSet<>();
    public final List<String> sortedVariables = new ArrayList<>();
    public final List<String> sortedParameters = new ArrayList<>();

    public ParsedFunction(String name, String function, String assignmentSymbol) {
        this.assignmentSymbol = assignmentSymbol;
        this.name = name.trim().replaceAll(" +", " ").replace("\"", "");
        this.function = function.trim().replaceAll(" +", " ").replace("\"", "");
        parseFunction();
        sortedVariables.addAll(variables);
        Collections.sort(sortedVariables);
        sortedParameters.addAll(parameters);
        Collections.sort(sortedParameters);
    }

    private void parseFunction() {
        if (log.isTraceEnabled()) {
            log.trace("parse function from \"{}\"", function);
        }
        try {
            String[] vars = Tools.split(function, "-", "+", " ", "/", "^");
            for (int i = 0; i < vars.length; i++) {
                vars[i] = vars[i].replace("(", "").replace(")", "");
                if (vars[i].contains("[t]")) {
                    String variableName = vars[i].replace("[t]", "").replace("'", "").trim();
                    if (StringUtils.isNotBlank(variableName) && !NumberUtils.isParsable(variableName.replace(".", "").replace(",", ""))) {
                        variables.add(variableName);
                    }
                } else {
                    if (!StringUtils.isNumeric(vars[i])) {
                        String parameterName = vars[i].trim();
                        if (StringUtils.isNotBlank(parameterName) && !NumberUtils.isParsable(parameterName.replace(".", "").replace(",", ""))) {
                            parameters.add(parameterName);
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            // Any invalid variable names should throw an IllegalArgumentException
            variables.clear();
            parameters.clear();
            throw e;
        }
        if (log.isTraceEnabled()) {
            log.trace("Found variables \"{}\" and parameters \"{}\" in function \"{}\"", sortedVariables, sortedParameters, function);
        }
    }

}
