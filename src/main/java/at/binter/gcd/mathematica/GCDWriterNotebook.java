package at.binter.gcd.mathematica;

import at.binter.gcd.mathematica.syntax.*;
import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.MathematicaModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static at.binter.gcd.util.MathematicaUtils.transformToFullForm;

public class GCDWriterNotebook implements GCDMathematica {
    private static final Logger log = LoggerFactory.getLogger(GCDWriterNotebook.class);
    private final GCDModel gcdModel;
    private final GCDMode mode;
    private final File outputFile;
    private final OutputStreamWriter writer;
    private final MathematicaModel model;
    private final List<IExpression> elements = new ArrayList<>();

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
    public void writeToFile() {
        log.info("Generating {}", outputFile.getAbsolutePath());
        try {
            writer.write("Notebook[{\r\n");
            generate();
            writeElements();
            writer.write("}]");
            writer.flush();
            writer.close();
            log.info("file output stream closed");
        } catch (IOException e) {
            log.error("Could not generate file", e);
        }
    }

    @Override
    public void generate() {
        elements.clear();
        generateVariableDefinition();
    }

    @Override
    public void generatePlotStyles() {
    }

    @Override
    public void generateVariableDefinition() {

        RowBox diffVarComment = new RowBox(model.getDiffvarComment());
        RowBox diffVar = new RowBox(model.getSetDiffVar());
        RowBox nDiffVar = new RowBox(model.getSetnDiffVar());
        RowBox algVarComment = new RowBox(model.getAlgvarComment());
        RowBox algVar = new RowBox(model.getSetAlgVar());
        RowBox nAlgVar = new RowBox(model.getSetnAlgVar());
        RowBox varComment = new RowBox(model.getVarComment());
        RowBox var = new RowBox(model.getSetVar());
        RowBox nVar = new RowBox(model.getSetnVar());
        diffVarComment.setDoLinebreaks(true);
        diffVar.setDoLinebreaks(true);
        nDiffVar.setDoLinebreaks(true);
        algVarComment.setDoLinebreaks(true);
        algVar.setDoLinebreaks(true);
        nAlgVar.setDoLinebreaks(true);
        varComment.setDoLinebreaks(true);
        var.setDoLinebreaks(true);
        nVar.setDoLinebreaks(true);

        RowBox inner = new RowBox(
                diffVarComment, diffVar, nDiffVar,
                algVarComment, algVar, nAlgVar,
                varComment, var, nVar
        );

        elements.add(new Cell(new BoxData(inner)));
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

    }

    private String writeList(List<IExpression> list) {
        StringBuilder b = new StringBuilder();
        for (IExpression expr : list) {
            try {
                String e;
                if (expr instanceof MComment) {
                    e = expr.getMathematicaExpression();
                } else {
                    e = transformToFullForm(expr.getMathematicaExpression(), true);
                }
                b.append(e);
                if (StringUtils.isBlank(e)) {
                    continue;
                }
                if (log.isTraceEnabled()) {
                    log.trace(e);
                }
                writer.write(e);
                writer.write("\r\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return b.toString();
    }

    private void writeElements() {
        Iterator<IExpression> it = elements.iterator();
        while (it.hasNext()) {
            IExpression expr = it.next();
            try {
                writer.write(expr.getMathematicaExpression());
                if (it.hasNext()) {
                    writer.write(", \"\\[IndentingNewLine]\"\r\n");
                }
            } catch (IOException e) {
                log.error("could not write {}", expr.getMathematicaExpression(), e);
            }
        }
    }
}
