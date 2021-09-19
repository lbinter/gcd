package at.binter.gcd.mathematica;

import at.binter.gcd.mathematica.elements.MPlotStyle;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.HasPlotStyle;
import at.binter.gcd.model.MathematicaModel;
import at.binter.gcd.model.elements.Agent;
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
    private final GCDModel gcdModel;
    private final MathematicaModel model;

    public GCDWriterHTML(GCDModel gcdModel) {
        this.gcdModel = gcdModel;
        model = new MathematicaModel(gcdModel);
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
        builder.writeln(generateVariableDefinition());
        builder.writeln(generateAgentDefinition());
        builder.writeln(generateSubstitutes());
        builder.writeln(generateConstraintDefinition());
        builder.writeln(generateChangeMus());
        builder.writeln(generateTransformVariables());
        builder.writeln(generateIndexedVariables());
        builder.writeln(generateSubstituteVariables());
        builder.writeln(generateSubstituteFunctions());
        builder.writeln(generateBehavioralEquation());
        builder.writeln(generateInitialConditions());
        builder.writeln(generateSystemOfEquation());
        return builder.toString();
    }

    @Override
    public String generatePlotStyles() {
        HTMLBuilder builder = new HTMLBuilder();
        builder.writeln("defaultColor = Black;");
        builder.writeln("defaultThickness = AbsoluteThickness[1];");
        gcdModel.getAgents().sorted().forEach(agent -> builder.writeln(new MPlotStyle(gcdModel, agent).toHTML()));
        builder.writeln(new MPlotStyle(gcdModel).toHTML());
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
        list.add(model.getDiffVarComment());
        list.add(model.getSetDiffVar());
        list.add(model.getSetnDiffVar());
        list.add(model.getAlgVarComment());
        list.add(model.getSetAlgVar());
        list.add(model.getSetnAlgVar());
        list.add(model.getVarComment());
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

        list.add(model.getAGComment());
        list.add(model.getSetAG());
        list.add(model.getSetnAG());

        writeToBuilder(builder, list, true);

        return builder.toString();
    }

    @Override
    public String generateSubstitutes() {
        HTMLBuilder builder = new HTMLBuilder();

        List<IExpression> list = new ArrayList<>();

        list.add(model.getSubstituteComment());
        list.add(model.getSetSubstitute());
        list.addAll(model.getSetDelayedDefalgvar());
        list.addAll(model.getSetDelayedDefAlgVarSubstitute());
        list.addAll(model.getParameterListDefAlgVar());
        list.addAll(model.getParameterListDefAlgVarSubstitute());
        list.add(model.getLinebreak());
        list.add(model.getDefuVarComment());
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

        list.add(model.getnZwangBComment());
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

        list.add(model.getMfiComment());
        list.add(model.getSetMFi());
        list.add(model.getMFiMatrix());
        list.add(model.getMachtfaktorenComment());
        list.add(model.getFlattenMFiComment());
        list.add(model.getSetFlattenMFi());
        list.add(model.getSetChangeMu());
        list.add(model.getMFexComment());
        list.add(model.getSetMFex());

        writeToBuilder(builder, list, true);

        return builder.toString();
    }

    @Override
    public String generateTransformVariables() {
        HTMLBuilder builder = new HTMLBuilder();

        List<IExpression> list = new ArrayList<>();

        list.add(model.getLambdaFComment());
        list.add(model.getSetLambdaF());
        list.add(model.getLinebreak());
        list.add(model.getDiffVarXComment());
        list.add(model.getSetDiffVarX());
        list.add(model.getSetAlgVarXX());
        list.add(model.getSetVarXXX());

        writeToBuilder(builder, list, true);

        return builder.toString();
    }

    @Override
    public String generateIndexedVariables() {
        HTMLBuilder builder = new HTMLBuilder();

        List<IExpression> list = new ArrayList<>();

        list.add(model.getSetChangeDiffaX());
        list.add(model.getSetChangeDiffXa());
        list.add(model.getSetChangeAlgbXX());
        list.add(model.getSetChangeAlgXXb());

        writeToBuilder(builder, list, true);

        return builder.toString();
    }

    @Override
    public String generateSubstituteVariables() {
        HTMLBuilder builder = new HTMLBuilder();

        List<IExpression> list = new ArrayList<>();

        list.add(model.getSetDelayedDefAlgVarSubstituteXXX());
        list.add(model.getSetDelayedDefUVarSubstituteXXX());
        list.add(model.getSetDelayedDefZVarSubstituteXXX());

        writeToBuilder(builder, list, true);

        return builder.toString();
    }

    @Override
    public String generateSubstituteFunctions() {
        HTMLBuilder builder = new HTMLBuilder();

        List<IExpression> list = new ArrayList<>();

        list.addAll(model.getParameterListDefUVarSubstituteXXX());
        list.addAll(model.getParameterListDefZVarSubstituteXXX());

        writeToBuilder(builder, list, true);

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
        SortedList<Variable> variables = gcdModel.getVariables().sorted();
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
        SortedList<Agent> agents = gcdModel.getAgents().sorted();
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