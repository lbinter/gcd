package at.binter.gcd.mathematica;

import at.binter.gcd.mathematica.elements.MPlotStyle;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.HasPlotStyle;
import at.binter.gcd.model.MathematicaModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
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
        if (gcdModel.isClearGlobal()) {
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
        builder.writeln(generateManipulate());
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
        List<IExpression> list = new ArrayList<>();
        list.add(model.getDiffvarComment());
        list.add(model.getSetDiffVar());
        list.add(model.getSetnDiffVar());
        list.add(model.getAlgvarComment());
        list.add(model.getSetAlgVar());
        list.add(model.getSetnAlgVar());
        list.add(model.getVarComment());
        list.add(model.getSetVar());
        list.add(model.getSetnVar());

        return writeList(list);
    }

    @Override
    public String generateAgentDefinition() {
        List<IExpression> list = new ArrayList<>();

        list.add(model.getAGComment());
        list.add(model.getSetAG());
        list.add(model.getSetnAG());

        return writeList(list);
    }

    @Override
    public String generateSubstitutes() {
        List<IExpression> list = new ArrayList<>();

        list.add(model.getSubstituteComment());
        list.add(model.getSetSubstitute());
        list.addAll(model.getSetDelayedDefalgvar());
        list.addAll(model.getSetDelayedDefAlgVarSubstitute());
        list.addAll(model.getParameterListDefAlgVar());
        list.addAll(model.getParameterListDefAlgVarSubstitute());
        list.add(model.getLinebreak());
        list.add(model.getDefuvarComment());
        list.addAll(model.getSetDelayedDefuVar());
        list.addAll(model.getSetDelayedDefuVarSubstitute());
        list.addAll(model.getParameterListDefuVar());
        list.addAll(model.getParameterListDefuVarSubstitute());

        return writeList(list);
    }

    @Override
    public String generateConstraintDefinition() {
        List<IExpression> list = new ArrayList<>();

        list.add(model.getnZwangBComment());
        list.add(model.getSetnZwangB());
        list.addAll(model.getSetDelayedDefzVar());
        list.addAll(model.getSetDelayedDefzVarSubstitute());
        list.addAll(model.getParameterListDefzVar());
        list.addAll(model.getParameterListDefzVarSubstitute());

        return writeList(list);
    }

    @Override
    public String generateChangeMus() {
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

        return writeList(list);
    }

    @Override
    public String generateTransformVariables() {
        List<IExpression> list = new ArrayList<>();

        list.add(model.getLambdaFComment());
        list.add(model.getSetLambdaF());
        list.add(model.getLinebreak());
        list.add(model.getDiffvarxComment());
        list.add(model.getSetDiffVarX());
        list.add(model.getSetAlgVarXX());
        list.add(model.getSetVarXXX());

        return writeList(list);
    }

    @Override
    public String generateIndexedVariables() {
        List<IExpression> list = new ArrayList<>();

        list.add(model.getSetChangeDiffaX());
        list.add(model.getSetChangeDiffXa());
        list.add(model.getSetChangeAlgbXX());
        list.add(model.getSetChangeAlgXXb());

        return writeList(list);
    }

    @Override
    public String generateSubstituteVariables() {
        List<IExpression> list = new ArrayList<>();

        list.add(model.getSetDelayedDefAlgVarSubstituteXXX());
        list.add(model.getSetDelayedDefUVarSubstituteXXX());
        list.add(model.getSetDelayedDefZVarSubstituteXXX());

        return writeList(list);
    }

    @Override
    public String generateSubstituteFunctions() {
        List<IExpression> list = new ArrayList<>();

        list.addAll(model.getParameterListDefUVarSubstituteXXX());
        list.addAll(model.getParameterListDefZVarSubstituteXXX());

        return writeList(list);
    }

    @Override
    public String generateBehavioralEquation() {
        List<IExpression> list = new ArrayList<>();

        list.add(model.getSetDgldiffxxx());
        list.add(model.getSetAglalgxxx());
        list.add(model.getLinebreak());
        list.add(model.getDglzxxxComment());
        list.add(model.getSetDglzxxx());
        list.add(model.getSetDglxxx());
        list.add(model.getLinebreak());
        list.add(model.getDglComment());
        list.add(model.getSetDgl());

        return writeList(list);
    }

    @Override
    public String generateInitialConditions() {
        List<IExpression> list = new ArrayList<>();

        list.add(model.getInitComment());
        list.add(model.getSetInit());

        return writeList(list);
    }

    @Override
    public String generateSystemOfEquation() {
        List<IExpression> list = new ArrayList<>();

        list.add(model.getUglComment());
        list.add(model.getSetUgl());
        list.add(model.getGlComment());
        list.add(model.getSetGl());
        list.add(model.getGlvarComment());
        list.add(model.getSetGlvar());
        list.add(model.getGLComment());
        list.add(model.getSetGL());
        list.add(model.getAfterGl());

        return writeList(list);
    }

    @Override
    public String generateManipulate() {
        List<IExpression> list = new ArrayList<>();

        list.add(model.getManipulate());

        return writeList(list);
    }

    @Override
    public String generatePlot(String plotName, String plotStyleName, List<String> plotVariables, List<String> legendNames) {
        HTMLBuilder builder = new HTMLBuilder();
        return builder.toString();
    }

    private String writeList(List<IExpression> list) {
        HTMLBuilder builder = new HTMLBuilder();
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
}