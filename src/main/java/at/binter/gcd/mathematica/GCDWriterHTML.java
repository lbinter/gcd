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
    private final GCDMode mode;
    private HTMLBuilder builder = new HTMLBuilder();
    private final List<IExpression> elements = new ArrayList<>();

    public GCDWriterHTML(GCDModel gcdModel, GCDMode mode) {
        this.gcdModel = gcdModel;
        this.mode = mode;
        model = new MathematicaModel(gcdModel);
    }

    @Override
    public GCDMode getGCDMode(GCDMode mode) {
        return mode;
    }

    @Override
    public String toString() {
        generate();
        writeList(elements);
        return builder.toString();
    }

    @Override
    public boolean writeToFile() {
        throw new UnsupportedOperationException("GCDWriterHTML cannot write to file");
    }

    @Override
    public void generate() {
        elements.clear();
        builder = new HTMLBuilder();
        builder.comment("Erstellt am: " + new Date());
        builder.linebreak();
        if (gcdModel.isClearGlobal()) {
            builder.writeln("ClearAll[\"Global`*\"]");
            builder.linebreak();
        }
        generatePlotStyles();
        generateVariableDefinition();
        generateAgentDefinition();
        generateSubstitutes();
        generateConstraintDefinition();
        generateChangeMus();
        generateTransformVariables();
        generateIndexedVariables();
        generateSubstituteVariables();
        generateSubstituteFunctions();
        generateBehavioralEquation();
        generateInitialConditions();
        generateSystemOfEquation();
        generateManipulate();
    }

    @Override
    public void generatePlotStyles() {
        builder.writeln("defaultColor = Black;");
        builder.writeln("defaultThickness = AbsoluteThickness[1];");
        gcdModel.getAgents().sorted().forEach(agent -> builder.writeln(new MPlotStyle(gcdModel, agent).toHTML()));
        builder.writeln(new MPlotStyle(gcdModel).toHTML());
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
    public void generateVariableDefinition() {
        elements.add(model.getDiffvarComment());
        elements.add(model.getSetDiffVar());
        elements.add(model.getSetnDiffVar());
        elements.add(model.getAlgvarComment());
        elements.add(model.getSetAlgVar());
        elements.add(model.getSetnAlgVar());
        elements.add(model.getVarComment());
        elements.add(model.getSetVar());
        elements.add(model.getSetnVar());
    }

    @Override
    public void generateAgentDefinition() {
        elements.add(model.getAGComment());
        elements.add(model.getSetAG());
        elements.add(model.getSetnAG());
    }

    @Override
    public void generateSubstitutes() {
        elements.add(model.getSubstituteComment());
        elements.add(model.getSetSubstitute());
        elements.addAll(model.getSetDelayedDefalgvar());
        elements.addAll(model.getSetDelayedDefAlgVarSubstitute());
        elements.addAll(model.getParameterListDefAlgVar());
        elements.addAll(model.getParameterListDefAlgVarSubstitute());
        elements.add(model.getLinebreak());
        elements.add(model.getDefuvarComment());
        elements.addAll(model.getSetDelayedDefuVar());
        elements.addAll(model.getSetDelayedDefuVarSubstitute());
        elements.addAll(model.getParameterListDefuVar());
        elements.addAll(model.getParameterListDefuVarSubstitute());
    }

    @Override
    public void generateConstraintDefinition() {
        elements.add(model.getnZwangBComment());
        elements.add(model.getSetnZwangB());
        elements.addAll(model.getSetDelayedDefzVar());
        elements.addAll(model.getSetDelayedDefzVarSubstitute());
        elements.addAll(model.getParameterListDefzVar());
        elements.addAll(model.getParameterListDefzVarSubstitute());
    }

    @Override
    public void generateChangeMus() {
        elements.add(model.getMfiComment());
        elements.add(model.getSetMFi());
        elements.add(model.getMFiMatrix());
        elements.add(model.getMachtfaktorenComment());
        elements.add(model.getFlattenMFiComment());
        elements.add(model.getSetFlattenMFi());
        elements.add(model.getSetChangeMu());
        elements.add(model.getMFexComment());
        elements.add(model.getSetMFex());
    }

    @Override
    public void generateTransformVariables() {
        elements.add(model.getLambdaFComment());
        elements.add(model.getSetLambdaF());
        elements.add(model.getLinebreak());
        elements.add(model.getDiffvarxComment());
        elements.add(model.getSetDiffVarX());
        elements.add(model.getSetAlgVarXX());
        elements.add(model.getSetVarXXX());
    }

    @Override
    public void generateIndexedVariables() {
        elements.add(model.getSetChangeDiffaX());
        elements.add(model.getSetChangeDiffXa());
        elements.add(model.getSetChangeAlgbXX());
        elements.add(model.getSetChangeAlgXXb());
    }

    @Override
    public void generateSubstituteVariables() {
        elements.add(model.getSetDelayedDefAlgVarSubstituteXXX());
        elements.add(model.getSetDelayedDefUVarSubstituteXXX());
        elements.add(model.getSetDelayedDefZVarSubstituteXXX());
    }

    @Override
    public void generateSubstituteFunctions() {
        elements.addAll(model.getParameterListDefUVarSubstituteXXX());
        elements.addAll(model.getParameterListDefZVarSubstituteXXX());
    }

    @Override
    public void generateBehavioralEquation() {
        elements.add(model.getDgldiffxxxComment());
        elements.add(model.getSetDgldiffxxx());
        elements.add(model.getSetAglalgxxx());
        elements.add(model.getLinebreak());
        elements.add(model.getDglzxxxComment());
        elements.add(model.getSetDglzxxx());
        elements.add(model.getSetDglxxx());
        elements.add(model.getLinebreak());
        elements.add(model.getDglComment());
        elements.add(model.getSetDgl());
    }

    @Override
    public void generateInitialConditions() {
        elements.add(model.getInitComment());
        elements.add(model.getSetInit());
    }

    @Override
    public void generateSystemOfEquation() {
        elements.add(model.getUglComment());
        elements.add(model.getSetUgl());
        elements.add(model.getGlComment());
        elements.add(model.getSetGl());
        elements.add(model.getGlvarComment());
        elements.add(model.getSetGlvar());
        elements.add(model.getGLComment());
        elements.add(model.getSetGL());
        elements.add(model.getAfterGl());
    }

    @Override
    public void generateManipulate() {
        elements.add(model.getManipulate());
    }

    @Override
    public void generatePlot(String plotName, String plotStyleName, List<String> plotVariables, List<String> legendNames) {
        // TODO impl. me
    }

    private String writeList(List<IExpression> list) {
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