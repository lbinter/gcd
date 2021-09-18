package at.binter.gcd.mathematica;

import at.binter.gcd.mathematica.elements.MPlotStyle;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MComment;
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
            builder.writeln("ClearAll[\"Global`*\"]");
            builder.linebreak();
        }
        builder.writeln(generatePlotStyles());
        builder.linebreak();
        builder.writeln(generateVariableDefinition());
        builder.linebreak();
        builder.writeln(generateAgentDefinition());
        builder.linebreak();
        builder.writeln(generateSubstitutes());
        builder.linebreak();
        builder.writeln(generateConstraintDefinition());
        builder.linebreak();
        builder.writeln(generateChangeMus());
        builder.linebreak();
        builder.writeln(generateTransformVariables());
        builder.linebreak();
        builder.writeln(generateIndexedVariables());
        builder.linebreak();
        builder.writeln(generateSubstituteVariables());
        builder.linebreak();
        builder.writeln(generateSubstituteFunctions());
        builder.linebreak();
        builder.writeln(generateBehavioralEquation());
        builder.linebreak();
        builder.writeln(generateInitialConditions());
        builder.linebreak();
        builder.writeln(generateSystemOfEquation());
        return builder.toString();
    }

    @Override
    public String generatePlotStyles() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.writeln("defaultColor = Black;");
        builder.writeln("defaultThickness = AbsoluteThickness[1];");
        model.getAgents().sorted().forEach(agent -> builder.writeln(new MPlotStyle(model, agent).toHTML()));
        builder.writeln(new MPlotStyle(model).toHTML());
        return builder.toString();
    }

    private String generatePlotStyle(String plotName, List<HasPlotStyle> plotStyles, List<String> plotStyleComments) {
        HTMLBuilder builder = new HTMLBuilder();
        builder.write(plotName);
        builder.writeln(":= {");
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
        builder.writeln("};");
        return builder.toString();
    }

    @Override
    public String generateVariableDefinition() {
        HTMLBuilder builder = new HTMLBuilder();

        List<IExpression> list = new ArrayList<>();
        list.add(GCDModel.diffVarComment);
        list.add(model.getSetDiffVar());
        list.add(model.getSetnDiffVar());
        list.add(GCDModel.algVarComment);
        list.add(model.getSetAlgVar());
        list.add(model.getSetnAlgVar());
        list.add(model.getSetVar());
        list.add(model.getSetnVar());

        writeToBuilder(builder, list, true);

        return builder.toString();
    }

    private void writeToBuilder(HTMLBuilder builder, List<IExpression> list, boolean addLineBreak) {
        for (IExpression e : list) {
            e.toHTML(builder);
            if (addLineBreak) {
                builder.linebreak();
            }
        }
    }

    @Override
    public String generateAgentDefinition() {
        HTMLBuilder builder = new HTMLBuilder();

        List<IExpression> list = new ArrayList<>();

        list.add(GCDModel.AGComment);
        list.add(model.getSetAG());
        list.add(model.getSetnAG());

        writeToBuilder(builder, list, true);

        return builder.toString();
    }

    @Override
    public String generateSubstitutes() {
        HTMLBuilder builder = new HTMLBuilder();

        List<IExpression> list = new ArrayList<>();

        list.add(GCDModel.substituteComment);
        list.add(model.getSetSubstitute());
        list.addAll(model.getSetDelayedDefalgvar());
        list.addAll(model.getSetDelayedDefAlgVarSubstitute());
        list.addAll(model.getParameterListDefAlgVar());
        list.addAll(model.getParameterListDefAlgVarSubstitute());
        list.add(GCDModel.defuVarComment);
        list.addAll(model.getSetDelayedDefuVar());
        list.addAll(model.getSetDelayedDefuVarSubstitute());
        list.addAll(model.getParameterListDefuVar());
        list.addAll(model.getParameterListDefuVarSubstitute());

        writeToBuilder(builder, list, true);

        return builder.toString();
    }

    @Override
    public String generateConstraintDefinition() {
        HTMLBuilder builder = new HTMLBuilder();

        List<IExpression> list = new ArrayList<>();

        list.add(GCDModel.nZwangBComment);
        list.add(model.getSetnZwangB());
        list.addAll(model.getSetDelayedDefzVar());
        list.addAll(model.getSetDelayedDefzVarSubstitute());
        list.addAll(model.getParameterListDefzVar());
        list.addAll(model.getParameterListDefzVarSubstitute());

        writeToBuilder(builder, list, true);

        return builder.toString();
    }

    @Override
    public String generateChangeMus() {
        HTMLBuilder builder = new HTMLBuilder();

        List<IExpression> list = new ArrayList<>();

        list.add(GCDModel.mfiComment);
        list.add(model.getSetMFi());
        list.add(model.getMFiMatrix());

        writeToBuilder(builder, list, true);

        MComment machtfaktoren = new MComment(
                "Machtfaktoren müssen in Manipulate als nichtindizierte Variable",
                "angegeben werden, indizierte Variable können in Manipulate nicht",
                "manipuliert werden!!!");
        machtfaktoren.toHTML(builder);

        builder.comment("indizierte Machtfaktoren");
        builder.writeln("MFi = Table[\\[Mu][j, i], {j, AG}, {i, ndiffvar}]");
        builder.writeln("MFi // MatrixForm;");
        builder.comment("Machtfaktoren müssen in Manipulate als nichtindizierte Variable " +
                "angegeben werden, indizierte Variable können in Manipulate nicht " +
                "manipuliert werden!!!");
        builder.writeln("flattenMFi = Flatten[MFi]; ");

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
        builder.writeln("};");
        builder.linebreak();
        builder.comment("Machtfaktoren als nichtindizierte Variable");
        builder.write("MFex = flattenMFi /. change\\[Mu]");
        return builder.toString();
    }

    @Override
    public String generateTransformVariables() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.comment("Lagrangefaktoren");
        builder.writeln("\\[Lambda]F = Array[Subscript[\\[Lambda], #] &, {nZwangB}]");
        builder.comment("Wandle die aktuellen Variablen diffvar um in ein Array diffvarx von indizierten Variablen x der Länge ndiffvar");
        builder.writeln("diffvarx = Array[Subscript[x, #] &, {ndiffvar}];");
        builder.writeln("algvarxx = Array[Subscript[xx, #] &, {nalgvar}];");
        builder.writeln("varxxx = Join[diffvarx, algvarxx]");
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
        builder.writeln("}");

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
        builder.writeln("}");


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
        builder.writeln("}");


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
        builder.writeln("}");

        return builder.toString();
    }

    @Override
    public String generateSubstituteVariables() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.writeln("defalgvarsubstitutexxx[jj_, t_, varxxx_] := defalgvarsubstitute[jj, t, var] /. changediffax /. changealgbxx;");
        builder.writeln("defuvarsubstitutexxx[j_, t_, varxxx_] := defuvarsubstitute[j, t, var] /. changediffax /. changealgbxx;");
        builder.writeln("defzvarsubstitutexxx[q_, t_, varxxx_] := defzvarsubstitute[q, t, var] /. changediffax /. changealgbxx;");
        return builder.toString();
    }

    @Override
    public String generateSubstituteFunctions() {
        SortedList<Agent> agents = model.getAgents().sorted();
        HTMLBuilder builder = new HTMLBuilder();

        agents.forEach(agent -> {
            builder.writeln("defuvarsubstitutexxx[" + agent.getName() + ", t, var]");
        });
        model.getConstraints().forEach(c -> {
            builder.writeln("defzvarsubstitutexxx[" + c.getId() + ", t, var]");
        });

        return builder.toString();
    }

    @Override
    public String generateBehavioralEquation() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.writeln("dgldiffxxx = Table[" + subscript("x", "i") + "'[t] ==");
        builder.increaseIndent(4);
        builder.writeln("Sum[\\[Mu][j, i] If[Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defuvarsubstitutexxx[j, t, varxxx]] &ne; 0, Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defuvarsubstitutexxx[j, t, varxxx]],");
        builder.increaseIndent(4);
        builder.writeln("Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defuvarsubstitutexxx[j, t, varxxx]]], {j, AG}] + ");
        builder.decreaseIndent(3);
        builder.writeln("Sum[" + subscript("\\[Lambda]", "q") + "[t] If[Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defzvarsubstitutexxx[q, t, varxxx]] &ne; 0, Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defzvarsubstitutexxx[q, t, varxxx]],");
        builder.increaseIndent(4);
        builder.writeln("Evaluate[" +
                subscript("&part;", subscript("x", "i") + "'[t]") +
                "defzvarsubstitutexxx[q, t, varxxx]]], {q, nZwangB}],");
        builder.decreaseIndent(6);
        builder.writeln("{i, ndiffvar}];");
        builder.decreaseIndent(3);
        builder.linebreak();

        builder.writeln("dglalgxxx =Table[" + subscript("xx", "ii") + "[t] == defalgvarsubstitutexxx[ii, t, varxxx], {ii, nalgvar}];");
        builder.linebreak();

        builder.comment("Zwangsbedingungen für allgemeines GCD Modell mit indizierten Variablen");
        builder.writeln("dglzxxx = Table[0 == defzvarsubstitutexxx[q, t, varxxx], {q, nZwangB}];");
        builder.writeln("dglxxx = Join[dglalgxxx, dgldiffxxx, dglzxxx];");
        builder.linebreak();

        builder.comment("Rücktransformation des Modells mit indizierten Variablen in Modell mit benannten Variablen");
        builder.writeln("dgl = dglxxx /. changediffxa /. changealgxxb;");

        return builder.toString();
    }

    @Override
    public String generateInitialConditions() {
        SortedList<Variable> variables = model.getVariables().sorted();
        HTMLBuilder builder = new HTMLBuilder();
        builder.comment("Definition der Anfangsbedingungen");
        builder.writeln("init = {");
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
        builder.writeln("}");
        return builder.toString();
    }

    @Override
    public String generateSystemOfEquation() {
        SortedList<Agent> agents = model.getAgents().sorted();
        HTMLBuilder builder = new HTMLBuilder();
        builder.comment("Zum Berechnen und Plotten der Nutzenfunktionen");
        builder.writeln("ugl = {");
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
        builder.writeln("}");
        builder.linebreak();

        builder.comment("Führe alle Gleichungen für NDSolve zusammen");
        builder.writeln("gl = Join[ugl, dgl, init];");
        builder.linebreak();

        builder.comment("Differentialgleichungsvariable incl. Lagrangefaktoren und Nutzenfunktionen");
        builder.writeln("glvar = Join[{" + String.join(",", uAgentNames) + "}, var, \\[Lambda]F]");
        builder.linebreak();

        builder.comment("Transformiere die indizierten Machtfaktoren in den Gleichungen gl in nichtindizierte Machtfaktoren");
        builder.writeln("GL = gl /. change\\[Mu]");
        builder.linebreak();

        builder.writeln("(* Das berechnete Ergebnis von GL muss manuell in das Programm zur ");
        builder.increaseIndent(2);
        builder.writeln("Lösung der Modellgleichungen übertragen werden (es reicht nicht indem ");
        builder.writeln("man nur \"GL\" in NDSolve schreibt, das kann Manipulate nicht ");
        builder.writeln("verarbeiten). Dann das Lösungsprogramm gesondert gestartet werden *)");
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