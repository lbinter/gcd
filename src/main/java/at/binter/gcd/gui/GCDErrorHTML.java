package at.binter.gcd.gui;

import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.GCDWarning;
import at.binter.gcd.model.elements.*;
import at.binter.gcd.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static at.binter.gcd.model.GCDWarning.*;
import static at.binter.gcd.util.Tools.getMaxSize;

public class GCDErrorHTML {
    private static final Logger log = LoggerFactory.getLogger(GCDErrorHTML.class);
    private final GCDModel model;
    private final ResourceBundle resources;
    private StringBuilder b;
    private Map<GCDWarning, List<Object>> warnings = new HashMap<>();
    private int warningCount = 0;

    public GCDErrorHTML(GCDModel model, ResourceBundle resources) {
        this.model = model;
        this.resources = resources;
    }

    public String generate() {
        warnings = model.getWarnings();
        warningCount = 0;
        b = new StringBuilder();
        b.append("<html>");
        b.append("<body>");
        generateDuplicateTable();
        generateAlgVarRecursionTable();
        appendWarningsFor(MAX_VALUE_LESSER_MIN_VALUE);
        appendWarningsFor(START_VALUE_LESSER_MIN_VALUE);
        appendWarningsFor(START_VALUE_GREATER_MAX_VALUE);
        appendWarningsFor(MISSING_START_VALUE);
        appendWarningsFor(MISSING_MIN_VALUE);
        appendWarningsFor(MISSING_MAX_VALUE);
        b.append("</body>");
        b.append("</body>");
        b.append("</html>");
        return b.toString();
    }

    private void generateDuplicateTable() {
        List<Object> variables = warnings.get(DUPLICATE_VARIABLE_PARAMETER);
        if (variables.isEmpty()) {
            return;
        }
        b.append("<div class=\"");
        b.append(DUPLICATE_VARIABLE_PARAMETER.name());
        b.append("\">");
        b.append("<h3>");
        b.append(resources.getString(DUPLICATE_VARIABLE_PARAMETER.getI18n()));
        b.append("</h3>");
        b.append("<ul>");
        for (Object v : variables) {
            if (v instanceof Variable variable) {
                createDuplicateEntry(variable);
            } else {
                b.append("<li>Error - found ");
                b.append(v.getClass());
                b.append(" instead of Variable.class</li>");
            }
        }
        b.append("</ul>");
        b.append("</div>");
    }

    private void generateAlgVarRecursionTable() {
        List<Object> algVars = warnings.get(ALGEBRAIC_VARIABLE_RECURSION);
        if (algVars.isEmpty()) {
            return;
        }
        b.append("<div class=\"");
        b.append(ALGEBRAIC_VARIABLE_RECURSION.name());
        b.append("\">");
        b.append("<h3>");
        b.append(resources.getString(ALGEBRAIC_VARIABLE_RECURSION.getI18n()));
        b.append("</h3>");
        b.append("<ul>");
        for (Object algVar : algVars) {
            if (algVar instanceof AlgebraicVariable algebraicVariable) {
                createRecursionEntry(algebraicVariable);
            } else {
                b.append("<li>Error - found ");
                b.append(algVar.getClass());
                b.append(" instead of AlgebraicVariable.class</li>");
            }
        }
        b.append("</ul>");
        b.append("</div>");
    }

    private void createRecursionEntry(AlgebraicVariable algebraicVariable) {
        b.append("<li>");
        b.append(algebraicVariable.getName());
        b.append(" - ");
        List<String> recursiveNames = new ArrayList<>();
        for (String name : algebraicVariable.getVariables()) {
            AlgebraicVariable algVar = model.getAlgebraicVariable(name);
            if (algVar != null) {
                recursiveNames.add(algVar.getName());
            }
        }
        b.append(StringUtils.join(recursiveNames));
        b.append("</li>");
        warningCount++;
    }

    private void createDuplicateEntry(Variable variable) {
        Parameter parameter = model.getParameter(variable.getName());
        if (parameter == null) {
            log.error("Could not find parameter \"{}\", but was identified as DUPLICATE_VARIABLE_PARAMETER", variable.getName());
            return;
        }

        AlgebraicVariable[] variableUsedInAlgebraicVariables = variable.getAlgebraicVariables().toArray(new AlgebraicVariable[0]);
        Agent[] variableUsedInAgents = variable.getAgents().toArray(new Agent[0]);
        Constraint[] variableUsedInConstraints = variable.getConstraints().toArray(new Constraint[0]);

        AlgebraicVariable[] parameterUsedInAlgebraicVariables = parameter.getAlgebraicVariables().toArray(new AlgebraicVariable[0]);
        Agent[] parameterUsedInAgents = parameter.getAgents().toArray(new Agent[0]);
        Constraint[] parameterUsedInConstraints = parameter.getConstraints().toArray(new Constraint[0]);

        int maxSize = getMaxSize(
                variableUsedInAlgebraicVariables, variableUsedInAgents, variableUsedInConstraints,
                parameterUsedInAlgebraicVariables, parameterUsedInAgents, parameterUsedInConstraints
        );

        Arrays.sort(variableUsedInAlgebraicVariables);
        Arrays.sort(variableUsedInAgents);
        Arrays.sort(variableUsedInConstraints, new Comparator<Constraint>() {
            @Override
            public int compare(Constraint o1, Constraint o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
        Arrays.sort(parameterUsedInAlgebraicVariables);
        Arrays.sort(parameterUsedInAgents);
        Arrays.sort(parameterUsedInConstraints, new Comparator<Constraint>() {
            @Override
            public int compare(Constraint o1, Constraint o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });

        b.append("<li>");
        b.append("<table>");
        b.append("<tr>");
        b.append("<td colspan=\"6\">");
        b.append(variable.getName());
        b.append("</td>");
        b.append("</tr>");

        b.append("<tr>");
        b.append("<td colspan=\"3\">Variable</td>");
        b.append("<td colspan=\"3\">Parameter</td>");
        b.append("</tr>");

        b.append("<tr>");
        b.append("<td>AlgVar</td>");
        b.append("<td>Agent</td>");
        b.append("<td>ZB</td>");
        b.append("<td>AlgVar</td>");
        b.append("<td>Agent</td>");
        b.append("<td>ZB</td>");
        b.append("</tr>");

        for (int i = 0; i < maxSize; i++) {
            b.append("<tr>");
            b.append("<td>");
            appendValue(variableUsedInAlgebraicVariables, i);
            b.append("</td>");
            b.append("<td>");
            appendValue(variableUsedInAgents, i);
            b.append("</td>");
            b.append("<td>");
            appendValue(variableUsedInConstraints, i);
            b.append("</td>");
            b.append("<td>");
            appendValue(parameterUsedInAlgebraicVariables, i);
            b.append("</td>");
            b.append("<td>");
            appendValue(parameterUsedInAgents, i);
            b.append("</td>");
            b.append("<td>");
            appendValue(parameterUsedInConstraints, i);
            b.append("</td>");
            b.append("</tr>");
        }

        b.append("</table>");
        b.append("</li>");
        warningCount++;
    }

    private void appendValue(Object[] array, int index) {
        String text = null;
        if (index < array.length) {
            Object obj = array[index];
            if (obj instanceof AlgebraicVariable) {
                text = ((AlgebraicVariable) obj).getName();
            } else if (obj instanceof Agent) {
                text = ((Agent) obj).getName();
            } else if (obj instanceof Constraint) {
                text = String.valueOf(((Constraint) obj).getId());
            } else {
                throw new IllegalArgumentException("Unexpected class " + obj.getClass() + " found in DUPLICATE_VARIABLE_PARAMETER list");
            }
        }
        if (text != null) {
            b.append(Tools.transformMathematicaGreekToGreekHtmlLetters(text));
        }
    }

    private void appendWarningsFor(GCDWarning warning) {
        List<Object> w = warnings.get(warning);
        if (w.isEmpty()) {
            return;
        }
        List<String> variables = new ArrayList<>();
        List<String> parameters = new ArrayList<>();
        List<String> changeMus = new ArrayList<>();
        for (Object o : w) {
            if (o instanceof Variable) {
                variables.add(((Variable) o).getName());
            } else if (o instanceof Parameter) {
                parameters.add(((Parameter) o).getName());
            } else if (o instanceof ChangeMu) {
                changeMus.add(((ChangeMu) o).getIdentifier());
            } else {
                throw new IllegalArgumentException("HasMinMaxValues is not instance of Variable, Parameter or ChangeMu");
            }
        }
        b.append("<div class=\"");
        b.append(warning.name());
        b.append("\">");
        if (!variables.isEmpty()) {
            appendWarningsFor(warning, "variable", variables);
        }
        if (!parameters.isEmpty()) {
            appendWarningsFor(warning, "parameter", parameters);
        }
        if (!changeMus.isEmpty()) {
            appendWarningsFor(warning, "mu", changeMus);
        }
        b.append("</div>");
    }

    private void appendWarningsFor(GCDWarning warning, String type, List<String> names) {
        boolean isMu = "mu".equals(type);
        b.append("<h3>");
        b.append(resources.getString(warning.getI18n() + "." + type));
        b.append("</h3>");
        b.append("<ul>");
        for (String name : names) {
            String transformed = Tools.transformMathematicaGreekToGreekHtmlLetters(name);
            b.append("<li>");
            b.append(transformed);
            if (!isMu && !transformed.equals(name)) {
                b.append(" - ");
                b.append(name);
            }
            b.append("</li>");
            warningCount++;
        }
        b.append("</ul>");
    }

    public Map<GCDWarning, List<Object>> getWarnings() {
        return warnings;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public boolean hasWarnings() {
        return warningCount != 0;
    }
}