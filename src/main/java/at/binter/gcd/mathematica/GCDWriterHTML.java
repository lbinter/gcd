package at.binter.gcd.mathematica;

import at.binter.gcd.mathematica.elements.MPlotStyle;
import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.HasPlotStyle;
import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.model.elements.ChangeMu;
import at.binter.gcd.model.elements.Variable;
import javafx.collections.transformation.SortedList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GCDWriterHTML implements GCDMathematica {
    private static final Logger log = LoggerFactory.getLogger(GCDWriterHTML.class);
    private final GCDModel model;

    public GCDWriterHTML(GCDModel gcdModel) {
        this.model = gcdModel;
    }

    @Override
    public String generate() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.comment("Erstellt am: " + new Date().toString());
        builder.linebreak();
        if (model.isClearGlobal()) {
            builder.writeLine("ClearAll[\"Global`*\"]");
            builder.linebreak();
        }
        builder.writeLine(generatePlotStyles());
        builder.linebreak();
        builder.writeLine(generateVariableDefinition());
        builder.linebreak();
        builder.writeLine(generateAgentDefinition());
        builder.linebreak();
        builder.writeLine(generateSubstitutes());
        builder.linebreak();
        builder.writeLine(generateConstraintDefinition());
        builder.linebreak();
        builder.writeLine(generateChangeMus());
        builder.linebreak();
        builder.writeLine(generateTransformVariables());
        builder.linebreak();
        builder.writeLine(generateIndexedVariables());
        builder.linebreak();
        builder.writeLine(generateSubstituteVariables());
        builder.linebreak();
        builder.writeLine(generateSubstituteFunctions());
        builder.linebreak();
        builder.writeLine(generateBehavioralEquation());
        builder.linebreak();
        builder.writeLine(generateInitialConditions());
        builder.linebreak();
        builder.writeLine(generateSystemOfEquation());
        return builder.toString();
    }

    @Override
    public String generatePlotStyles() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.writeLine("defaultColor = Black;");
        builder.writeLine("defaultThickness = AbsoluteThickness[1];");
        model.getAgents().sorted().forEach(agent -> builder.writeLine(new MPlotStyle(model, agent).toHTML()));
        builder.writeLine(new MPlotStyle(model).toHTML());
        return builder.toString();
    }

    private String generatePlotStyle(String plotName, List<HasPlotStyle> plotStyles, List<String> plotStyleComments) {
        HTMLBuilder builder = new HTMLBuilder();
        builder.write(plotName);
        builder.writeLine(":= {");
        builder.increaseIndent(4);
        for (int i = 0; i < plotStyles.size(); i++) {
            HasPlotStyle plotStyle = plotStyles.get(i);
            String color = "defaultColor";
            if (StringUtils.isNotBlank(plotStyle.getPlotColor())) {
                color = plotStyle.getPlotColor();
            }
            String thickness = "defaultThickness";
            if (plotStyle.getPlotThickness() != null) {
                thickness = "AbsoluteThickness[" + plotStyle.getPlotThickness() + "]";
            }

            builder.write("Directive[");
            builder.write(color);
            builder.write(",");
            builder.write(thickness);

            if (StringUtils.isNotBlank(plotStyle.getPlotLineStyle())) {
                builder.write(",");
                builder.write(plotStyle.getPlotLineStyle());
            }

            builder.write("]");

            if (i < plotStyles.size() - 1) {
                builder.write(",");
            }
            builder.write(" ");
            builder.comment(plotStyleComments.get(i));
        }
        builder.decreaseIndent(4);
        builder.writeLine("};");
        return builder.toString();
    }

    @Override
    public String generateVariableDefinition() {
        HTMLBuilder builder = new HTMLBuilder();

        builder.comment("durch Differentialgleichungen bestimmte Variable");
        builder.write("diffvar = {");
        builder.write(model.getVariables().sorted().stream()
                .map(Variable::getName)
                .collect(Collectors.joining(",")));
        builder.writeLine("}");
        builder.writeLine("ndiffvar = Length[diffvar]");

        builder.comment("durch algebraische Definitionsgleichungen bestimmte Variable");
        builder.write("diffvar = {");
        builder.write(model.getAlgebraicVariables().sorted().stream()
                .map(AlgebraicVariable::getName)
                .collect(Collectors.joining(",")));
        builder.writeLine("}");
        builder.writeLine("nalgvar = Length[algvar]");

        builder.comment("gesamte Variablen");
        builder.writeLine("var = Join[diffvar, algvar]");
        builder.writeLine("nvar = Length[var]");
        return builder.toString();
    }

    @Override
    public String generateAgentDefinition() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.comment("Agenten");
        builder.write("AG = {");
        builder.write(model.getAgents().sorted().stream()
                .map(Agent::getName)
                .collect(Collectors.joining(",")));
        builder.writeLine("}");
        builder.writeLine("nAG = Length[AG]");
        return builder.toString();
    }

    @Override
    public String generateSubstitutes() {
        SortedList<AlgebraicVariable> algebraicVariables = model.getAlgebraicVariables().sorted();
        SortedList<Agent> agents = model.getAgents().sorted();
        HTMLBuilder builder = new HTMLBuilder();

        builder.writeLine("substitute = {");
        builder.increaseIndent(4);
        Iterator<AlgebraicVariable> it = algebraicVariables.iterator();
        while (it.hasNext()) {
            AlgebraicVariable algebraicVariable = it.next();
            builder.write(algebraicVariable.toString());
            if (it.hasNext()) {
                builder.write(",");
            }
            builder.linebreak();
        }
        builder.decreaseIndent(4);
        builder.writeLine("};");

        it = algebraicVariables.iterator();
        int ii = 0;
        while (it.hasNext()) {
            AlgebraicVariable algebraicVariable = it.next();
            ii++;
            builder.write("defalgvar[ii_, t_, var_] :=");
            builder.write(algebraicVariable.getName());
            builder.write("[");
            builder.write(algebraicVariable.getParameter());
            builder.writeLine("] /; ii ==" + 1 + ";");
        }
        for (int i = 1; i <= ii; i++) {
            builder.write("defalgvarsubstitute[ii_, t_, var_] := (defalgvar[");
            builder.write(String.valueOf(i));
            builder.writeLine(", t, var] /. substitute) /; ii ==" + i + ";");
        }

        for (int i = 1; i <= ii; i++) {
            builder.writeLine("defalgvar[" + i + ", t, var]");
        }

        for (int i = 1; i <= ii; i++) {
            builder.writeLine("defalgvarsubstitute[" + i + ", t, var]");
        }
        builder.linebreak();

        builder.comment("Definitionsgleichungen der Nutzenfunktionen");
        agents.forEach(agent -> {
            builder.write("defuvar[j_, t_, var_] := ");
            builder.write(agent.getFunction());
            builder.writeLine(" /; j == " + agent.getName() + ";");
        });
        agents.forEach(agent -> {
            builder.write("defuvarsubstitute[j_, t_, var_] := (defuvar[");
            builder.write(agent.getName());
            builder.writeLine(", t, var] /. substitute) /; j ==" + agent.getName() + ";");
        });

        agents.forEach(agent -> {
            builder.writeLine("defuvar[" + agent.getName() + ", t, var]");
        });
        agents.forEach(agent -> {
            builder.writeLine("defuvarsubstitute[" + agent.getName() + ", t, var]");
        });
        return builder.toString();
    }

    @Override
    public String generateConstraintDefinition() {
        HTMLBuilder builder = new HTMLBuilder();

        builder.comment("Definitionsgleichungen der Zwangsbedingungen");
        builder.writeLine("nZwangB = " + model.getConstraints().size());
        model.getConstraints().forEach(constraint -> {
            if (StringUtils.isNotBlank(constraint.getDescription())) {
                builder.comment(constraint.getDescription());
            }
            builder.write("defzvar[q_, t_, var_] := ");
            builder.write(constraint.getCondition());
            builder.writeLine(" /; q == " + constraint.getId() + ";");
        });

        model.getConstraints().forEach(constraint -> {
            builder.writeLine("defzvar[" + constraint.getId() + ", t, var]");
        });

        model.getConstraints().forEach(c -> {
            builder.writeLine("defzvarsubstitute[q_, t_, var_] := (defzvar[" + c.getId() + ", t, var] /. substitute) /; q ==" + c.getId() + ";");
        });

        model.getConstraints().forEach(c -> {
            builder.writeLine("defzvarsubstitute[" + c.getId() + ", t, var]");
        });

        return builder.toString();
    }

    @Override
    public String generateChangeMus() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.comment("indizierte Machtfaktoren");
        builder.writeLine("MFi = Table[\\[Mu][j, i], {j, AG}, {i, ndiffvar}]");
        builder.writeLine("MFi // MatrixForm;");
        builder.comment("Machtfaktoren müssen in Manipulate als nichtindizierte Variable " +
                "angegeben werden, indizierte Variable können in Manipulate nicht " +
                "manipuliert werden!!!");
        builder.writeLine("flattenMFi = Flatten[MFi]; ");

        Iterator<ChangeMu> it = model.getAllChangeMu().iterator();
        builder.write("change\\[Mu] = {");
        builder.increaseIndent(4);
        Agent current = null;
        int muCount = 0;
        while (it.hasNext()) {
            ChangeMu mu = it.next();
            if (current != mu.getAgent()) {
                if (muCount > 1) {
                    builder.linebreak();
                }
                builder.linebreak();
                current = mu.getAgent();
                muCount = 0;
            }
            if (muCount > 4) {
                builder.linebreak();
                muCount = 0;
            }
            builder.write(mu.getIndexName());
            builder.write("->");
            builder.write(mu.getIdentifier());
            muCount++;
            if (it.hasNext()) {
                builder.write(", ");
            }
        }
        builder.decreaseIndent(4);
        builder.linebreak();
        builder.writeLine("};");
        builder.linebreak();
        builder.comment("Machtfaktoren als nichtindizierte Variable");
        builder.write("MFex = flattenMFi /. change\\[Mu]");
        return builder.toString();
    }

    @Override
    public String generateTransformVariables() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.comment("Lagrangefaktoren");
        builder.writeLine("\\[Lambda]F = Array[Subscript[\\[Lambda], #] &, {nZwangB}]");
        builder.comment("Wandle die aktuellen Variablen diffvar um in ein Array diffvarx von indizierten Variablen x der Länge ndiffvar");
        builder.writeLine("diffvarx = Array[Subscript[x, #] &, {ndiffvar}];");
        builder.writeLine("algvarxx = Array[Subscript[xx, #] &, {nalgvar}];");
        builder.writeLine("varxxx = Join[diffvarx, algvarxx]");
        return builder.toString();
    }

    @Override
    public String generateIndexedVariables() {
        SortedList<Variable> variables = model.getVariables().sorted();
        SortedList<AlgebraicVariable> algebraicVariables = model.getAlgebraicVariables().sorted();
        HTMLBuilder builder = new HTMLBuilder();

        builder.write("changediffax = {");
        builder.increaseIndent(4);
        int count = 1;
        Iterator<Variable> it = variables.iterator();
        while (it.hasNext()) {
            Variable v = it.next();
            if ((count - 1) % 4 == 0) {
                builder.linebreak();
            }
            builder.write(v.getName());
            builder.write(" -> ");
            builder.write(subscript("x", String.valueOf(count)));
            if (it.hasNext()) {
                builder.write(", ");
            } else {
                builder.linebreak();
            }
            count++;
        }
        builder.decreaseIndent(4);
        builder.writeLine("}");

        builder.write("changediffxa = {");
        builder.increaseIndent(4);
        count = 1;
        it = variables.iterator();
        while (it.hasNext()) {
            Variable v = it.next();
            if ((count - 1) % 4 == 0) {
                builder.linebreak();
            }
            builder.write(subscript("x", String.valueOf(count)));
            builder.write(" -> ");
            builder.write(v.getName());
            if (it.hasNext()) {
                builder.write(", ");
            } else {
                builder.linebreak();
            }
            count++;
        }
        builder.decreaseIndent(4);
        builder.writeLine("}");


        builder.write("changediffax = {");
        builder.increaseIndent(4);
        count = 1;
        Iterator<AlgebraicVariable> itA = algebraicVariables.iterator();
        while (itA.hasNext()) {
            AlgebraicVariable algVar = itA.next();
            if ((count - 1) % 3 == 0) {
                builder.linebreak();
            }
            builder.write(algVar.getName());
            builder.write(" -> ");
            builder.write(subscript("xx", String.valueOf(count)));
            if (itA.hasNext()) {
                builder.write(", ");
            } else {
                builder.linebreak();
            }
            count++;
        }
        builder.decreaseIndent(4);
        builder.writeLine("}");


        builder.write("changediffax = {");
        builder.increaseIndent(4);
        count = 1;
        itA = algebraicVariables.iterator();
        while (itA.hasNext()) {
            AlgebraicVariable algVar = itA.next();
            if ((count - 1) % 3 == 0) {
                builder.linebreak();
            }
            builder.write(subscript("xx", String.valueOf(count)));
            builder.write(" -> ");
            builder.write(algVar.getName());
            if (itA.hasNext()) {
                builder.write(", ");
            } else {
                builder.linebreak();
            }
            count++;
        }
        builder.decreaseIndent(4);
        builder.writeLine("}");

        return builder.toString();
    }

    @Override
    public String generateSubstituteVariables() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.writeLine("defalgvarsubstitutexxx[jj_, t_, varxxx_] := defalgvarsubstitute[jj, t, var] /. changediffax /. changealgbxx;");
        builder.writeLine("defuvarsubstitutexxx[j_, t_, varxxx_] := defuvarsubstitute[j, t, var] /. changediffax /. changealgbxx;");
        builder.writeLine("defzvarsubstitutexxx[q_, t_, varxxx_] := defzvarsubstitute[q, t, var] /. changediffax /. changealgbxx;");
        return builder.toString();
    }

    @Override
    public String generateSubstituteFunctions() {
        SortedList<Agent> agents = model.getAgents().sorted();
        HTMLBuilder builder = new HTMLBuilder();

        agents.forEach(agent -> {
            builder.writeLine("defuvarsubstitutexxx[" + agent.getName() + ", t, var]");
        });
        model.getConstraints().forEach(c -> {
            builder.writeLine("defzvarsubstitutexxx[" + c.getId() + ", t, var]");
        });

        return builder.toString();
    }

    @Override
    public String generateBehavioralEquation() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.writeLine("dgldiffxxx = Table[" + subscript("x", "i") + "'[t] ==");
        builder.increaseIndent(4);
        builder.writeLine("Sum[\\[Mu][j, i] If[Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defuvarsubstitutexxx[j, t, varxxx]] &ne; 0, Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defuvarsubstitutexxx[j, t, varxxx]],");
        builder.increaseIndent(4);
        builder.writeLine("Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defuvarsubstitutexxx[j, t, varxxx]]], {j, AG}] + ");
        builder.decreaseIndent(3);
        builder.writeLine("Sum[" + subscript("\\[Lambda]", "q") + "[t] If[Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defzvarsubstitutexxx[q, t, varxxx]] &ne; 0, Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defzvarsubstitutexxx[q, t, varxxx]],");
        builder.increaseIndent(4);
        builder.writeLine("Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defzvarsubstitutexxx[q, t, varxxx]]], {q, nZwangB}],");
        builder.decreaseIndent(6);
        builder.writeLine("{i, ndiffvar}];");
        builder.decreaseIndent(3);
        builder.linebreak();

        builder.writeLine("dglalgxxx =Table[" + subscript("xx", "ii") + "[t] == defalgvarsubstitutexxx[ii, t, varxxx], {ii, nalgvar}];");
        builder.linebreak();

        builder.comment("Zwangsbedingungen für allgemeines GCD Modell mit indizierten Variablen");
        builder.writeLine("dglzxxx = Table[0 == defzvarsubstitutexxx[q, t, varxxx], {q, nZwangB}];");
        builder.writeLine("dglxxx = Join[dglalgxxx, dgldiffxxx, dglzxxx];");
        builder.linebreak();

        builder.comment("Rücktransformation des Modells mit indizierten Variablen in Modell mit benannten Variablen");
        builder.writeLine("dgl = dglxxx /. changediffxa /. changealgxxb;");

        return builder.toString();
    }

    @Override
    public String generateInitialConditions() {
        SortedList<Variable> variables = model.getVariables().sorted();
        HTMLBuilder builder = new HTMLBuilder();
        builder.comment("Definition der Anfangsbedingungen");
        builder.writeLine("init = {");
        builder.increaseIndent(4);
        Iterator<Variable> it = variables.iterator();
        int count = 0;
        while (it.hasNext()) {
            Variable v = it.next();
            count++;
            builder.write(v.getName());
            builder.write("[0] == ");
            if (StringUtils.isNotBlank(v.getInitialCondition())) {
                builder.write(v.getInitialCondition());
            } else {
                builder.write(v.getDefaultInitialCondition());
            }
            if (it.hasNext()) {
                builder.write(", ");
                if (count % 4 == 0) {
                    builder.linebreak();
                }
            } else {
                builder.linebreak();
            }
        }
        builder.decreaseIndent(4);
        builder.writeLine("}");
        return builder.toString();
    }

    @Override
    public String generateSystemOfEquation() {
        SortedList<Agent> agents = model.getAgents().sorted();
        HTMLBuilder builder = new HTMLBuilder();
        builder.comment("Zum Berechnen und Plotten der Nutzenfunktionen");
        builder.writeLine("ugl = {");
        builder.increaseIndent(4);
        Iterator<Agent> it = agents.iterator();
        List<String> uAgentNames = new ArrayList<>();
        while (it.hasNext()) {
            Agent a = it.next();
            String uName = "u" + a.getName();
            uAgentNames.add(uName);
            builder.write(uName);
            builder.write("[t] == defuvar[");
            builder.write(a.getName());
            builder.write(", t, var]");
            if (it.hasNext()) {
                builder.write(",");
            }
            builder.linebreak();
        }
        builder.decreaseIndent(4);
        builder.writeLine("}");
        builder.linebreak();

        builder.comment("Führe alle Gleichungen für NDSolve zusammen");
        builder.writeLine("gl = Join[ugl, dgl, init];");
        builder.linebreak();

        builder.comment("Differentialgleichungsvariable incl. Lagrangefaktoren und Nutzenfunktionen");
        builder.writeLine("glvar = Join[{" + String.join(",", uAgentNames) + "}, var, \\[Lambda]F]");
        builder.linebreak();

        builder.comment("Transformiere die indizierten Machtfaktoren in den Gleichungen gl in nichtindizierte Machtfaktoren");
        builder.writeLine("GL = gl /. change\\[Mu]");
        builder.linebreak();

        builder.writeLine("(* Das berechnete Ergebnis von GL muss manuell in das Programm zur ");
        builder.increaseIndent(2);
        builder.writeLine("Lösung der Modellgleichungen übertragen werden (es reicht nicht indem ");
        builder.writeLine("man nur \"GL\" in NDSolve schreibt, das kann Manipulate nicht ");
        builder.writeLine("verarbeiten). Dann das Lösungsprogramm gesondert gestartet werden *)");
        return builder.toString();
    }

    @Override
    public String generateManipulate() {
        HTMLBuilder builder = new HTMLBuilder();
        return builder.toString();
    }

    @Override
    public String generatePlot(String plotName, String plotStyleName, List<String> plotVariables, List<String> legendNames) {
        HTMLBuilder builder = new HTMLBuilder();
        return builder.toString();
    }

    private String subscript(String text, String subtext) {
        return text + "<sub>" + subtext + "</sub>";
    }
}