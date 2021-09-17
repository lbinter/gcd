package at.binter.gcd.mathematica;

import at.binter.gcd.model.GCDModel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class GCDWriterNotebook implements GCDMathematica {
    private final GCDModel gcdModel;

    public GCDWriterNotebook(GCDModel gcdModel) {
        this.gcdModel = gcdModel;
    }

    @Override
    public String generate() {
        try {
            FileOutputStream output = new FileOutputStream("C:/Users/lucas/Desktop/test.nb");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String generatePlotStyles() {
        return null;
    }

    @Override
    public String generateVariableDefinition() {
        return null;
    }

    @Override
    public String generateAgentDefinition() {
        return null;
    }

    @Override
    public String generateSubstitutes() {
        return null;
    }

    @Override
    public String generateConstraintDefinition() {
        return null;
    }

    @Override
    public String generateChangeMus() {
        return null;
    }

    @Override
    public String generateTransformVariables() {
        return null;
    }

    @Override
    public String generateIndexedVariables() {
        return null;
    }

    @Override
    public String generateSubstituteVariables() {
        return null;
    }

    @Override
    public String generateSubstituteFunctions() {
        return null;
    }

    @Override
    public String generateBehavioralEquation() {
        return null;
    }

    @Override
    public String generateInitialConditions() {
        return null;
    }

    @Override
    public String generateSystemOfEquation() {
        return null;
    }

    @Override
    public String generateManipulate() {
        return null;
    }

    @Override
    public String generatePlot(String plotName, String plotStyleName, List<String> plotVariables, List<String> legendNames) {
        return null;
    }
}
