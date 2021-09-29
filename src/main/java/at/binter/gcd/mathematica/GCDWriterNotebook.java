package at.binter.gcd.mathematica;

import at.binter.gcd.mathematica.syntax.*;
import at.binter.gcd.mathematica.syntax.function.MClearAll;
import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.GCDPlot;
import at.binter.gcd.model.MathematicaModel;
import at.binter.gcd.util.MathematicaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static at.binter.gcd.model.MathematicaModel.convertParameterToRowBoxList;
import static at.binter.gcd.model.MathematicaModel.convertToRowBoxList;

public class GCDWriterNotebook implements GCDMathematica {
    private static final Logger log = LoggerFactory.getLogger(GCDWriterNotebook.class);
    private final GCDModel gcdModel;
    private final GCDMode mode;
    private final File outputFile;
    private final OutputStreamWriter writer;
    private final MathematicaModel model;
    private Notebook nb;
    private RowBox currentCell = new RowBox();
    private final IExpression linebreakExpr = new MExpression(MathematicaUtils.linebreakString);

    public GCDWriterNotebook(GCDModel gcdModel, GCDMode mode) {
        this.gcdModel = gcdModel;
        this.mode = mode;
        FileOutputStream output;
        try {
            switch (mode) {
                case NDSOLVE -> outputFile = gcdModel.getMathematicaNDSolveFile();
                case MODELICA -> outputFile = gcdModel.getMathematicaModelicaFile();
                case CONTROL -> outputFile = gcdModel.getMathematicaControlFile();
                default -> throw new IllegalArgumentException("Found invalid GCDMode " + mode);
            }
            output = new FileOutputStream(outputFile);
            writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not create GCDWriterNotebook", e);
        }
        model = new MathematicaModel(gcdModel);
    }

    @Override
    public GCDMode getGCDMode(GCDMode mode) {
        return mode;
    }

    @Override
    public boolean writeToFile() {
        log.info("Generating {}", outputFile.getAbsolutePath());
        try {
            generate();
            closeCurrentCell();
            writeNotebook();
            writer.flush();
            writer.close();
            model.getUtils().closeLink();
            log.info("file output stream closed");
            return true;
        } catch (IOException e) {
            log.error("Could not generate file", e);
            return false;
        }
    }

    @Override
    public void generate() {
        nb = new Notebook();
        nb.add(new Cell(new RowBox(new MClearAll("\"\\\"\\<Global`*\\>\\\"\""))));
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
        for (GCDPlot plot : gcdModel.getPlots()) {
            addToCurrentCell(plot.createMathematicaPlotStyle());
            addToCurrentCell(linebreakExpr);
        }
        closeCurrentCell();
    }

    @Override
    public void generateVariableDefinition() {
        addNewRowBox(model.getDiffvarComment());
        addNewRowBox(model.getSetDiffVar());
        addNewRowBox(model.getSetnDiffVar());
        addNewRowBox(model.getAlgvarComment());
        addNewRowBox(model.getSetAlgVar());
        addNewRowBox(model.getSetnAlgVar());
        addNewRowBox(model.getVarComment());
        addNewRowBox(model.getSetVar());
        addNewRowBox(model.getSetnVar());
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateAgentDefinition() {
        addNewRowBox(model.getAGComment());
        addNewRowBox(model.getSetAG());
        addNewRowBox(model.getSetnAG());
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateSubstitutes() {
        addNewRowBox(model.getSubstituteComment());
        addNewRowBox(model.getRowBoxSetSubstitute());
        addToCurrentCell(convertToRowBoxList(model.getSetDelayedDefalgvar()));
        addToCurrentCell(convertToRowBoxList(model.getSetDelayedDefAlgVarSubstitute()));
        addToCurrentCell(convertParameterToRowBoxList(model.getParameterListDefAlgVar()));
        addToCurrentCell(convertParameterToRowBoxList(model.getParameterListDefAlgVarSubstitute()));
        addToCurrentCell(linebreakExpr);
        addNewRowBox(model.getDefuvarComment());
        addToCurrentCell(model.getRowBoxDefuVar());
        addToCurrentCell(convertToRowBoxList(model.getSetDelayedDefuVarSubstitute()));
        addToCurrentCell(convertParameterToRowBoxList(model.getParameterListDefuVar()));
        addToCurrentCell(convertParameterToRowBoxList(model.getParameterListDefuVarSubstitute()));
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateConstraintDefinition() {
        addNewRowBox(model.getnZwangBComment());
        addNewRowBox(model.getSetnZwangB());
        addToCurrentCell(model.getRowBoxDefzVar());
        addToCurrentCell(convertToRowBoxList(model.getSetDelayedDefzVarSubstitute()));
        addToCurrentCell(convertParameterToRowBoxList(model.getParameterListDefzVar()));
        addToCurrentCell(convertParameterToRowBoxList(model.getParameterListDefzVarSubstitute()));
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateChangeMus() {
        addNewRowBox(model.getMfiComment());
        addNewRowBox(model.getSetMFi());
        addNewRowBox(model.getMFiMatrix());
        addNewRowBox(model.getMachtfaktorenComment());
        addNewRowBox(model.getFlattenMFiComment());
        addNewRowBox(model.getSetFlattenMFi());
        addNewRowBox(model.getSetChangeMu());
        addNewRowBox(model.getMFexComment());
        addNewRowBox(model.getSetMFex());
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateTransformVariables() {
        addNewRowBox(model.getLambdaFComment());
        addNewRowBox(model.getSetLambdaF());
        addToCurrentCell(linebreakExpr);
        addNewRowBox(model.getDiffvarxComment());
        addNewRowBox(model.getSetDiffVarX());
        addNewRowBox(model.getSetAlgVarXX());
        addNewRowBox(model.getSetVarXXX());
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateIndexedVariables() {
        addNewRowBox(model.getSetChangeDiffaX());
        addNewRowBox(model.getSetChangeDiffXa());
        addNewRowBox(model.getSetChangeAlgbXX());
        addNewRowBox(model.getSetChangeAlgXXb());
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateSubstituteVariables() {
        addNewRowBox(model.getSetDelayedDefAlgVarSubstituteXXX());
        addNewRowBox(model.getSetDelayedDefUVarSubstituteXXX());
        addNewRowBox(model.getSetDelayedDefZVarSubstituteXXX());
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateSubstituteFunctions() {
        addToCurrentCell(convertParameterToRowBoxList(model.getParameterListDefUVarSubstituteXXX()));
        addToCurrentCell(convertParameterToRowBoxList(model.getParameterListDefZVarSubstituteXXX()));
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateBehavioralEquation() {
        addNewRowBox(model.getDgldiffxxxComment());
        addNewRowBox(model.getSetDgldiffxxx());
        addNewRowBox(model.getSetAglalgxxx());
        addToCurrentCell(linebreakExpr);
        addNewRowBox(model.getDglzxxxComment());
        addNewRowBox(model.getSetDglzxxx());
        addNewRowBox(model.getSetDglxxx());
        addToCurrentCell(linebreakExpr);
        addNewRowBox(model.getDglComment());
        addNewRowBox(model.getSetDgl());
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateInitialConditions() {
        addNewRowBox(model.getInitComment());
        addNewRowBox(model.getSetInit());
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateSystemOfEquation() {
        addNewRowBox(model.getUglComment());
        addNewRowBox(model.getSetUgl());
        addNewRowBox(model.getGlComment());
        addNewRowBox(model.getSetGl());
        addNewRowBox(model.getGlvarComment());
        addNewRowBox(model.getSetGlvar());
        addNewRowBox(model.getGLComment());
        addNewRowBox(model.getSetGL());
        addNewRowBox(model.getAfterGl());
        addToCurrentCell(linebreakExpr);
    }

    @Override
    public void generateManipulate() {
        closeCurrentCell();
        addNewRowBox(model.getManipulate());
    }

    @Override
    public void generatePlot(String plotName, String plotStyleName, List<String> plotVariables, List<String> legendNames) {

    }

    private void writeNotebook() {
        try {
            writer.write(nb.getMathematicaExpression());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewRowBox(IExpression expression) {
        addToCurrentCell(new RowBox(true, expression));
    }

    private void addToCurrentCell(IExpression... expressions) {
        currentCell.add(expressions);
    }

    private void addToCurrentCell(List<RowBox> list) {
        for (RowBox box : list) {
            currentCell.add(box);
        }
    }

    private void closeCurrentCell() {
        if (currentCell.getExpressions().isEmpty()) {
            return;
        }
        nb.add(new Cell(new BoxData(currentCell)));
        currentCell = new RowBox();
    }
}
