package at.binter.gcd.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static at.binter.gcd.util.GuiUtils.sanitizeString;

public class ParsedFunction {
    private static final Logger log = LoggerFactory.getLogger(ParsedFunction.class);
    public final String function;
    public final Set<String> variables = new HashSet<>();
    public final Set<String> parameters = new HashSet<>();
    public final List<String> sortedVariables = new ArrayList<>();
    public final List<String> sortedParameters = new ArrayList<>();

    public ParsedFunction(String function) {
        if (StringUtils.isBlank(function)) {
            throw new IllegalArgumentException("Function may not be empty or null!");
        }
        this.function = sanitizeString(function).replace("\"", "");
        if (log.isTraceEnabled()) {
            log.trace("Parse function: \"{}\"", function);
        }
        parseFunction();
        sortedVariables.addAll(variables);
        Collections.sort(sortedVariables);
        sortedParameters.addAll(parameters);
        Collections.sort(sortedParameters);
        if (log.isTraceEnabled()) {
            log.trace("Found variables \"{}\" and parameters \"{}\"", String.join(",", sortedVariables), String.join(",", sortedParameters));
        }
    }

    private void parseFunction() {
        try {
            String[] vars = Tools.split(function, "-", "+", " ", "/", "^");
            for (int i = 0; i < vars.length; i++) {
                vars[i] = vars[i].replace("(", " ").replace(")", " ");
                if (vars[i].contains("[t]")) {
                    String variableName = vars[i].replace("[t]", " ").replace("'", " ").trim();
                    if (StringUtils.isNotBlank(variableName) && !NumberUtils.isParsable(variableName.replace(".", "").replace(",", ""))) {
                        variables.add(variableName);
                    }
                } else {
                    if (!StringUtils.isNumeric(vars[i])) {
                        String parameterName = vars[i].trim();
                        if (StringUtils.isNotBlank(parameterName) && !NumberUtils.isParsable(parameterName.replace(".", "").replace(",", ""))) {
                            if (parameterName.endsWith("0")) {
                                if (log.isTraceEnabled()) {
                                    log.trace("Ignoring parameter {}", parameterName);
                                }
                            } else {
                                parameters.add(parameterName);
                            }
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            // Any invalid variable names should throw an IllegalArgumentException
            // TODO check for illegal
            variables.clear();
            parameters.clear();
            throw e;
        }
    }

}
