package at.binter.gcd.model;

import at.binter.gcd.mathematica.GCDMode;
import at.binter.gcd.mathematica.elements.MParameter;
import at.binter.gcd.mathematica.elements.MVariable;
import at.binter.gcd.mathematica.elements.MathematicaPlot;
import at.binter.gcd.mathematica.syntax.*;
import at.binter.gcd.mathematica.syntax.binary.*;
import at.binter.gcd.mathematica.syntax.function.*;
import at.binter.gcd.mathematica.syntax.group.MList;
import at.binter.gcd.mathematica.syntax.group.MParentheses;
import at.binter.gcd.mathematica.syntax.unary.MReplaceAll;
import at.binter.gcd.model.elements.*;
import at.binter.gcd.util.MathematicaUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MathematicaModel {
    private static final Logger log = LoggerFactory.getLogger(MathematicaModel.class);
    private final GCDModel model;
    private final MathematicaUtils utils;

    public MathematicaModel(GCDModel model, MathematicaUtils utils) {
        this.model = model;
        this.utils = utils;
    }

    public MathematicaUtils getUtils() {
        return utils;
    }

    public GCDModel getModel() {
        return model;
    }

    private final String agentNamePrefix = "agent";

    private final MVariable variableT = new MVariable("t");
    private final MVariable variableX = new MVariable("x");
    private final MVariable variableXX = new MVariable("xx");
    private final MVariable variableLambda = new MVariable("\\[Lambda]");
    private final MVariable variablePartialD = new MVariable("\\[PartialD]");
    private final MVariable variableSharp = new MVariable("#");
    private final MVariable variableSol = new MVariable("sol");
    private final MVariable variableTMax = new MVariable("tmax");
    private final MVariable variableI = new MVariable("i");
    private final MVariable variableII = new MVariable("ii");
    private final MVariable variableModelica = new MVariable("modelica");
    private final MExpression e0 = new MExpression(0);
    private final MExpression e2p5 = new MExpression("2.5");
    private final MExpression e20 = new MExpression(20);
    private final MExpression e30 = new MExpression(30);
    private final MExpression e50 = new MExpression(50);
    private final MExpression e100 = new MExpression(100);

    private final MExpression parameterII = new MExpression("parameter", "ii");
    private final MExpression parameterJ = new MExpression("parameter", "j");
    private final MExpression parameterQ = new MExpression("parameter", "q");
    private final MExpression parameterT = new MExpression("parameter", "t");
    private final MExpression parameterPlotMax = new MExpression("parameter", "plotmax");
    private final MExpression parameterTMax = new MExpression("parameter", "tmax");
    private final MExpression parameterSharp = new MExpression("parameter", "#");
    private final MExpression parameterSol = new MExpression("parameter", "sol");

    private final MExpression linebreak = new MExpression("", true);
    private final MComment diffvarComment = new MComment("durch Differentialgleichungen bestimmte Variable");
    private final MVariable diffvar = new MVariable("diffvar");
    private final MVariable ndiffvar = new MVariable("ndiffvar");
    private final MComment algvarComment = new MComment("durch algebraische Definitionsgleichungen bestimmte Variable");
    private final MVariable algvar = new MVariable("algvar");
    private final MVariable nalgvar = new MVariable("nalgvar");
    private final MComment varComment = new MComment("gesamte Variable");
    private final MVariable var = new MVariable("var");
    private final MVariable nvar = new MVariable("nvar");

    private final MComment AGComment = new MComment("Agenten");
    private final MVariable AG = new MVariable("AG");
    private final MVariable nAG = new MVariable("nAG");

    private final MComment substituteComment = new MComment("algebraische Definitionsgleichungen der \"algebraischen\" Variablen algvar");
    private final MVariable substitute = new MVariable("substitute");
    private final MParameter defalgvar = new MParameter("defalgvar", "ii_", "t_", "var_");
    private final MParameter defalgvarsubstitute = new MParameter("defalgvarsubstitute", "ii_", "t_", "var_");
    private final MComment defuvarComment = new MComment("Definitionsgleichungen der Nutzenfunktionen");
    private final MParameter defuvar = new MParameter("defuvar", "j_", "t_", "var_");
    private final MParameter defuvarsubstitute = new MParameter("defuvarsubstitute", "j_", "t_", "var_");
    private final MComment nZwangBComment = new MComment("Definitionsgleichungen der Zwangsbedingungen");
    private final MVariable nZwangB = new MVariable("nZwangB");
    private final MParameter defzvar = new MParameter("defzvar", "q_", "t_", "var_");
    private final MParameter defzvarsubstitute = new MParameter("defzvarsubstitute", "q_", "t_", "var_");
    private final MComment mfiComment = new MComment("indizierte Machtfaktoren");
    private final MVariable mfi = new MVariable("MFi");
    private final MTable mfiTable = new MTable(
            new MParameter("\\[Mu]", "j", "i"),
            new MList(new MExpression("j"), AG),
            new MList(new MExpression("i"), ndiffvar));
    private final MPostfix mfiMatrix = new MPostfix(mfi, new MExpression("MatrixForm"), true);
    private final MComment machtfaktorenComment = new MComment("Machtfaktoren müssen in Manipulate als nichtindizierte Variable angegeben werden, indizierte Variable können in Manipulate nicht  manipuliert werden!!!");
    private final MComment flattenMFiComment = new MComment("um change\\[Mu] leichter hinschreiben zu können, change\\[Mu] kann leider nicht automatisch erstellt werden");
    private final MVariable flattenMFi = new MVariable("flattenMFi");
    private final MVariable changeMu = new MVariable("change\\[Mu]");
    private final MComment MFexComment = new MComment("Machtfaktoren als nicht indizierte Variable");
    private final MVariable mFex = new MVariable("MFex");
    private final MComment lambdaFComment = new MComment("Lagrangefaktoren");
    private final MVariable lambdaF = new MVariable("\\[Lambda]F");
    private final MArray lambdaFArray = new MArray(
            new MExpressionList(
                    new MSubscript(variableLambda, variableSharp),
                    new MVariable("&")
            ), new MList(nZwangB));
    private final MComment diffvarxComment = new MComment("Wandle die aktuellen Variablen diffvar um in ein Array diffvarx von indizierten Variablen x der Länge ndiffvar");
    private final MVariable diffvarx = new MVariable("diffvarx");
    private final MArray diffvarxArray = new MArray(
            new MExpressionList(
                    new MSubscript(new MVariable("x"), variableSharp),
                    new MVariable("&")
            ), new MList(ndiffvar));
    private final MVariable algvarxx = new MVariable("algvarxx");
    private final MArray algvarxxArray = new MArray(
            new MExpressionList(
                    new MSubscript(new MVariable("xx"), variableSharp),
                    new MVariable("&")
            ), new MList(nalgvar));
    private final MVariable varxxx = new MVariable("varxxx");
    private final MJoin varxxxJoin = new MJoin(diffvarx, algvarxx);
    private final MVariable changediffax = new MVariable("changediffax");
    private final MVariable changediffxa = new MVariable("changediffxa");
    private final MVariable changealgbxx = new MVariable("changealgbxx");
    private final MVariable changealgxxb = new MVariable("changealgxxb");
    private final MParameter defalgvarsubstitutexxx = new MParameter("defalgvarsubstitutexxx", "jj_", "t_", "varxxx_");
    private final MParameter defuvarsubstitutexxx = new MParameter("defuvarsubstitutexxx", "j_", "t_", "varxxx_");
    private final MParameter defzvarsubstitutexxx = new MParameter("defzvarsubstitutexxx", "q_", "t_", "varxxx_");
    private final MComment dgldiffxxxComment = new MComment("Verhaltensgleichungen für allgemeines GCD Modell mit indizierten Variablen");
    private final MVariable dgldiffxxx = new MVariable("dgldiffxxx");
    private final MVariable dglalgxxx = new MVariable("dglalgxxx");
    private final MComment dglzxxxComment = new MComment("Zwangsbedingungen für allgemeines GCD Modell mit indizierten Variablen");
    private final MVariable dglzxxx = new MVariable("dglzxxx");
    private final MVariable dglxxx = new MVariable("dglxxx");
    private final MComment dglComment = new MComment("Rücktransformation des Modells mit indizierten Variablen in Modell mit benannten Variablen");
    private final MVariable dgl = new MVariable("dgl");
    private final MComment initComment = new MComment("Definition der Anfangsbedingungen");
    private final MVariable init = new MVariable("init");
    private final MComment uglComment = new MComment("Zum Berechnen und Plotten der Nutzenfunktionen");
    private final MVariable ugl = new MVariable("ugl");
    private final MComment glComment = new MComment("Führe alle Gleichungen für NDSolve zusammen");
    private final MVariable gl = new MVariable("gl");
    private final MComment glvarComment = new MComment("Differentialgleichungsvariable incl. Lagrangefaktoren und Nutzenfunktionen");
    private final MVariable glvar = new MVariable("glvar");
    private final MComment GLComment = new MComment("Transformiere die indizierten Machtfaktoren in den Gleichungen gl in nicht indizierte Machtfaktoren");
    private final MVariable GL = new MVariable("GL");
    private final MComment afterGl = new MComment("Das berechnete Ergebnis von GL muss manuell in das Programm zur",
            "Lösung der Modellgleichungen übertragen werden (es reicht nicht indem",
            "man nur \"GL\" in NDSolve schreibt, das kann Manipulate nicht ",
            "verarbeiten). Dann das Lösungsprogramm gesondert gestartet werden");

    private final MComment controlAlgVarComment = new MComment("Algebraische Variablen");
    private final MComment controlAgentComment = new MComment("Agenten / Nutzenfunktionen");
    private final MComment controlConstraintComment = new MComment("Zwangsbedingungen");
    private final MComment controlVariableComment = new MComment("Anfangsbedingungen und Werte");

    public MExpression getLinebreak() {
        return linebreak;
    }

    public MComment getDiffvarComment() {
        return diffvarComment;
    }

    public MComment getAlgvarComment() {
        return algvarComment;
    }

    public MComment getVarComment() {
        return varComment;
    }

    public MComment getAGComment() {
        return AGComment;
    }

    public MComment getSubstituteComment() {
        return substituteComment;
    }

    public MComment getDefuvarComment() {
        return defuvarComment;
    }

    public MComment getnZwangBComment() {
        return nZwangBComment;
    }

    public MComment getMfiComment() {
        return mfiComment;
    }

    public MComment getMachtfaktorenComment() {
        return machtfaktorenComment;
    }

    public MComment getFlattenMFiComment() {
        return flattenMFiComment;
    }

    public MComment getMFexComment() {
        return MFexComment;
    }

    public MComment getLambdaFComment() {
        return lambdaFComment;
    }

    public MComment getDiffvarxComment() {
        return diffvarxComment;
    }

    public MComment getDgldiffxxxComment() {
        return dgldiffxxxComment;
    }

    public MComment getDglzxxxComment() {
        return dglzxxxComment;
    }

    public MComment getDglComment() {
        return dglComment;
    }

    public MComment getInitComment() {
        return initComment;
    }

    public MComment getUglComment() {
        return uglComment;
    }

    public MComment getGlComment() {
        return glComment;
    }

    public MComment getGlvarComment() {
        return glvarComment;
    }

    public MComment getGLComment() {
        return GLComment;
    }

    public MComment getAfterGl() {
        return afterGl;
    }

    public MSet getDefaultColor() {
        return new MSet(new MVariable("defaultColor"), new MExpression("Automatic"), true);
    }

    public MSet getDefaultThickness() {
        return new MSet(new MVariable("defaultThickness"), new MNamedCall("AbsoluteThickness", new MExpression(1)), true);
    }

    public MSet getSetDiffVar() {
        MList list = new MList();
        for (Variable v : model.getVariablesSorted()) {
            list.add(new MExpression("variable", v.getName()));
        }
        return new MSet(diffvar, list);
    }

    public MSet getSetnDiffVar() {
        return new MSet(ndiffvar, new MLength(diffvar));
    }

    public MSet getSetAlgVar() {
        MList list = new MList();
        for (AlgebraicVariable v : model.getAlgebraicVariablesSorted()) {
            list.add(new MExpression("variable", v.getName()));
        }
        return new MSet(algvar, list);
    }

    public MSet getSetnAlgVar() {
        return new MSet(nalgvar, new MLength(algvar));
    }

    public MSet getSetVar() {
        return new MSet(var, new MJoin(diffvar, algvar));
    }

    public MSet getSetnVar() {
        return new MSet(nvar, new MLength(var));
    }

    public MSet getSetAG() {
        MList list = new MList();
        for (Agent a : model.getAgentsSorted()) {
            list.add(new MExpression("variable", agentNamePrefix + a.getName()));
        }
        return new MSet(AG, list);
    }

    public MSet getSetnAG() {
        return new MSet(nAG, new MLength(AG));
    }

    public MSet getSetSubstitute() {
        MList list = new MList();
        list.setElementsLinebreak(1);
        for (AlgebraicVariable aV : model.getAlgebraicVariablesSorted()) {
            MExpressionList l = new MExpressionList();
            l.add(new MParameter(aV.getName(), aV.getParameter()));
            l.add(new MArrow());
            l.add(new MExpression(aV.getFunction()));
            list.add(l);
        }
        return new MSet(substitute, list, true);
    }

    public RowBox getRowBoxSetSubstitute() {
        MList list = new MList();
        list.setElementsLinebreak(1);
        for (AlgebraicVariable aV : model.getAlgebraicVariablesSorted()) {
            list.add(new MExpression(utils.transformToFullForm(aV.toStringRaw(), false)));
        }
        return new RowBox(false, new MSet(substitute, list, true));
    }

    public List<MSetDelayed> getSetDelayedDefalgvar() {
        List<MSetDelayed> list = new ArrayList<>(model.getAlgebraicVariables().size());
        int ii = 1;
        for (AlgebraicVariable aV : model.getAlgebraicVariablesSorted()) {
            MParameter algVar = new MParameter(aV.getName(), aV.getParameter());
            MSetDelayed delayed = new MSetDelayed(defalgvar,
                    new MCondition(
                            algVar,
                            new MEqual(variableII, new MExpression(ii))
                    ),
                    true);
            list.add(delayed);
            ii++;
        }
        return list;
    }

    public void transformToMathematica() {
        model.transformFunctions(utils);
    }

    public IExpression getPlotOptions() {
        return new MExpression(utils.transformToFullForm("SetOptions[Plot,ImageSize->" + model.getPlotImageSize() + "];", true));
    }

    private MParameter getDefalgVar(int ii) {
        if (ii > model.getAlgebraicVariables().size()) {
            log.error("Called getDefalgvar with ii of {}", ii);
        }
        return new MParameter(defalgvar.getName(), String.valueOf(ii), "t", "var");
    }

    private MParameter getDefAlgVarSubstitute(int ii) {
        if (ii > model.getAlgebraicVariables().size()) {
            log.error("Called getDefAlgVarSubstitute with ii of {}", ii);
        }
        return new MParameter(defalgvarsubstitute.getName(), String.valueOf(ii), "t", "var");
    }

    public List<MSetDelayed> getSetDelayedDefAlgVarSubstitute() {
        List<MSetDelayed> list = new ArrayList<>(model.getAlgebraicVariables().size());
        for (int ii = 1; ii <= model.getAlgebraicVariables().size(); ii++) {
            MParentheses parentheses = new MParentheses(new MReplaceAll(getDefalgVar(ii), substitute));
            MSetDelayed delayed = new MSetDelayed(defalgvarsubstitute,
                    new MCondition(
                            parentheses,
                            new MEqual(parameterII, new MExpression(ii))
                    ),
                    true);
            list.add(delayed);
        }
        return list;
    }

    public List<MParameter> getParameterListDefAlgVar() {
        List<MParameter> list = new ArrayList<>();
        for (int ii = 1; ii <= model.getAlgebraicVariables().size(); ii++) {
            list.add(getDefalgVar(ii));
        }
        return list;
    }

    public List<MParameter> getParameterListDefAlgVarSubstitute() {
        List<MParameter> list = new ArrayList<>();
        for (int ii = 1; ii <= model.getAlgebraicVariables().size(); ii++) {
            list.add(getDefAlgVarSubstitute(ii));
        }
        return list;
    }

    public MParameter getDefuVar(String index) {
        if (StringUtils.isBlank(index)) {
            log.error("Called getDefuVar with index of null");
        } else if (model.getAgent(index.replace(agentNamePrefix, "")) == null) {
            log.error("Called getDefuVar with non exists agent index of {}", index);
        }
        return new MParameter(defuvar.getName(), index, "t", "var");
    }

    public MParameter getDefuVarSubstitute(String index) {
        if (StringUtils.isBlank(index)) {
            log.error("Called getDefuVarSubstitute with index of null");
        } else if (model.getAgent(index.replace(agentNamePrefix, "")) == null) {
            log.error("Called getDefuVarSubstitute with non exists agent index of {}", index);
        }
        return new MParameter(defuvarsubstitute.getName(), index, "t", "var");
    }

    public List<IExpression> getSetDelayedDefuVar() {
        List<IExpression> list = new ArrayList<>(model.getAgentsSorted().size());
        for (Agent a : model.getAgentsSorted()) {
            if (StringUtils.isNotBlank(a.getDescription())) {
                list.add(new MComment(a.getDescription()));
            }
            MSetDelayed delayed = new MSetDelayed(defuvar,
                    new MCondition(
                            new MParentheses(new MExpression(a.getFunction())),
                            new MEqual(
                                    parameterJ,
                                    new MExpression("variable", agentNamePrefix + a.getName()))),
                    true);
            list.add(delayed);
        }
        return list;
    }

    public List<RowBox> getRowBoxDefuVar() {
        List<RowBox> list = new ArrayList<>(model.getAgentsSorted().size());
        for (Agent a : model.getAgentsSorted()) {
            if (StringUtils.isNotBlank(a.getDescription())) {
                list.add(new RowBox(true, new MComment(a.getDescription())));
            }
            MSetDelayed delayed = new MSetDelayed(defuvar,
                    new MCondition(
                            new MParentheses(new MExpression(a.getMathematicaFunction())),
                            new MEqual(
                                    parameterJ,
                                    new MExpression("variable", agentNamePrefix + a.getName()))),
                    true);
            list.add(new RowBox(true, delayed));
        }
        return list;
    }

    public List<MSetDelayed> getSetDelayedDefuVarSubstitute() {
        List<MSetDelayed> list = new ArrayList<>(model.getAgents().size());
        for (Agent a : model.getAgentsSorted()) {
            MParentheses parentheses = new MParentheses(new MReplaceAll(getDefuVar(agentNamePrefix + a.getName()), substitute));
            MSetDelayed delayed = new MSetDelayed(defuvarsubstitute,
                    new MCondition(
                            parentheses,
                            new MEqual(
                                    parameterJ,
                                    new MExpression("variable", agentNamePrefix + a.getName()))),
                    true);
            list.add(delayed);
        }
        return list;
    }

    public List<MParameter> getParameterListDefuVar() {
        List<MParameter> list = new ArrayList<>();
        for (Agent a : model.getAgentsSorted()) {
            list.add(getDefuVar(agentNamePrefix + a.getName()));
        }
        return list;
    }

    public List<MParameter> getParameterListDefuVarSubstitute() {
        List<MParameter> list = new ArrayList<>();
        for (Agent a : model.getAgentsSorted()) {
            list.add(getDefuVarSubstitute(agentNamePrefix + a.getName()));
        }
        return list;
    }

    public MSet getSetnZwangB() {
        return new MSet(nZwangB, new MExpression(model.getConstraints().size()));
    }

    public MParameter getDefzVar(int ii) {
        if (ii > model.getConstraints().size()) {
            log.error("Called getDefzvar with q of {}", ii);
        }
        return new MParameter(defzvar.getName(), String.valueOf(ii), "t", "var");
    }

    public MParameter getDefzVarSubstitute(int ii) {
        if (ii > model.getConstraints().size()) {
            log.error("Called getDefzVarSubstitute with q of {}", ii);
        }
        return new MParameter(defzvarsubstitute.getName(), String.valueOf(ii), "t", "var");
    }

    public List<IExpression> getSetDelayedDefzVar() {
        List<IExpression> list = new ArrayList<>(model.getAgents().size());
        for (Constraint c : model.getConstraints()) {
            if (StringUtils.isNotBlank(c.getDescription())) {
                list.add(new MComment(c.getDescription()));
            }
            MSetDelayed delayed = new MSetDelayed(defzvar,
                    new MCondition(
                            new MParentheses(new MExpression(c.getCondition())),
                            new MEqual(
                                    parameterQ,
                                    new MExpression(c.getId())
                            )),
                    true);
            list.add(delayed);
        }
        return list;
    }

    public List<RowBox> getRowBoxDefzVar() {
        List<RowBox> list = new ArrayList<>(model.getAgents().size());
        for (Constraint c : model.getConstraints()) {
            if (StringUtils.isNotBlank(c.getDescription())) {
                list.add(new RowBox(true, new MComment(c.getDescription())));
            }
            MSetDelayed delayed = new MSetDelayed(defzvar,
                    new MCondition(
                            new MParentheses(new MExpression(c.getMathematicaCondition())),
                            new MEqual(
                                    parameterQ,
                                    new MExpression(c.getId())
                            )),
                    true);
            list.add(new RowBox(true, delayed));
        }
        return list;
    }

    public List<MSetDelayed> getSetDelayedDefzVarSubstitute() {
        List<MSetDelayed> list = new ArrayList<>(model.getAgents().size());
        for (Constraint c : model.getConstraints()) {
            MParentheses parentheses = new MParentheses(new MReplaceAll(getDefzVar(c.getId()), substitute));
            MSetDelayed delayed = new MSetDelayed(defzvarsubstitute,
                    new MCondition(
                            parentheses,
                            new MEqual(
                                    parameterQ,
                                    new MExpression(c.getId())
                            )),
                    true);
            list.add(delayed);
        }
        return list;
    }

    public List<MParameter> getParameterListDefzVar() {
        List<MParameter> list = new ArrayList<>();
        for (Constraint c : model.getConstraints()) {
            list.add(getDefzVar(c.getId()));
        }
        return list;
    }

    public List<MParameter> getParameterListDefzVarSubstitute() {
        List<MParameter> list = new ArrayList<>();
        for (Constraint c : model.getConstraints()) {
            list.add(getDefzVarSubstitute(c.getId()));
        }
        return list;
    }

    public MSet getSetMFi() {
        return new MSet(mfi, mfiTable);
    }

    public MPostfix getMFiMatrix() {
        return mfiMatrix;
    }

    public MSet getSetFlattenMFi() {
        return new MSet(flattenMFi, new MFlatten(mfi), true);
    }

    public MSet getSetChangeMu() {
        MList list = new MList();
        list.setElementsLinebreak(5);
        list.setElementsBlock(model.getVariables().size());
        for (ChangeMu mu : model.getAllChangeMu()) {
            MExpressionList l = new MExpressionList();
            l.add(new MParameter(
                    "\\[Mu]",
                    new MExpression(agentNamePrefix + mu.getAgent().getName()),
                    new MExpression(mu.getIndex())
            ));
            l.add(new MArrow());
            l.add(new MVariable(mu.getIdentifier()));
            list.add(l);
        }
        return new MSet(changeMu, list, true);
    }

    public MSet getSetMFex() {
        return new MSet(mFex, new MReplaceAll(flattenMFi, changeMu));
    }

    public MSet getSetLambdaF() {
        return new MSet(lambdaF, lambdaFArray);
    }

    public MSet getSetDiffVarX() {
        return new MSet(diffvarx, diffvarxArray, true);
    }

    public MSet getSetAlgVarXX() {
        return new MSet(algvarxx, algvarxxArray, true);
    }

    public MSet getSetVarXXX() {
        return new MSet(varxxx, varxxxJoin);
    }

    public MSet getSetChangeDiffaX() {
        MList list = new MList();
        list.setElementsLinebreak(4);
        int count = 1;
        for (Variable v : model.getVariablesSorted()) {
            MExpressionList l = new MExpressionList();
            l.add(new MVariable(v.getName()));
            l.add(new MArrow());
            l.add(new MSubscript(variableX, new MExpression(count)));
            list.add(l);
            count++;
        }
        return new MSet(changediffax, list);
    }

    public MSet getSetChangeDiffXa() {
        MList list = new MList();
        list.setElementsLinebreak(4);
        int count = 1;
        for (Variable v : model.getVariablesSorted()) {
            MExpressionList l = new MExpressionList();
            l.add(new MSubscript(variableX, new MExpression(count)));
            l.add(new MArrow());
            l.add(new MVariable(v.getName()));
            list.add(l);
            count++;
        }
        return new MSet(changediffxa, list);
    }

    public MSet getSetChangeAlgbXX() {
        MList list = new MList();
        list.setElementsLinebreak(4);
        int count = 1;
        for (AlgebraicVariable v : model.getAlgebraicVariablesSorted()) {
            MExpressionList l = new MExpressionList();
            l.add(new MVariable(v.getName()));
            l.add(new MArrow());
            l.add(new MSubscript(variableXX, new MExpression(count)));
            list.add(l);
            count++;
        }
        return new MSet(changealgbxx, list);
    }

    public MSet getSetChangeAlgXXb() {
        MList list = new MList();
        list.setElementsLinebreak(4);
        int count = 1;
        for (AlgebraicVariable v : model.getAlgebraicVariablesSorted()) {
            MExpressionList l = new MExpressionList();
            l.add(new MSubscript(variableXX, new MExpression(count)));
            l.add(new MArrow());
            l.add(new MVariable(v.getName()));
            list.add(l);
            count++;
        }
        return new MSet(changealgxxb, list);
    }

    public MSetDelayed getSetDelayedDefAlgVarSubstituteXXX() {
        return new MSetDelayed(defalgvarsubstitutexxx,
                new MReplaceAll(
                        new MReplaceAll(
                                new MParameter(defalgvarsubstitute.getName(), "jj", "t", "var"),
                                changediffax),
                        changealgbxx),
                true
        );
    }

    public MSetDelayed getSetDelayedDefUVarSubstituteXXX() {
        return new MSetDelayed(defuvarsubstitutexxx,
                new MReplaceAll(
                        new MReplaceAll(
                                new MParameter(defuvarsubstitute.getName(), "j", "t", "var"),
                                changediffax),
                        changealgbxx),
                true
        );
    }

    public MSetDelayed getSetDelayedDefZVarSubstituteXXX() {
        return new MSetDelayed(defzvarsubstitutexxx,
                new MReplaceAll(
                        new MReplaceAll(
                                new MParameter(defzvarsubstitute.getName(), "q", "t", "var"),
                                changediffax),
                        changealgbxx),
                true
        );
    }

    public List<MParameter> getParameterListDefUVarSubstituteXXX() {
        List<MParameter> list = new ArrayList<>();
        for (Agent a : model.getAgentsSorted()) {
            list.add(new MParameter(defuvarsubstitutexxx.getName(), agentNamePrefix + a.getName(), "t", "varxxx"));
        }
        return list;
    }

    public List<MParameter> getParameterListDefZVarSubstituteXXX() {
        List<MParameter> list = new ArrayList<>();
        for (Constraint c : model.getConstraints()) {
            list.add(new MParameter(defzvarsubstitutexxx.getName(), String.valueOf(c.getId()), "t", "varxxx"));
        }
        return list;
    }

    public MSet getSetDgldiffxxx() {
        MSubscript xi = new MSubscript(variableX, variableI);

        MExpressionList xiApos = new MExpressionList(true);
        xiApos.add(xi);
        xiApos.add(new MVariable("'"));

        MParameter xiOfT = new MParameter(xi, variableT);

        MParameter xiAposOfT = new MParameter(xiApos, variableT);

        MSubscript partialDXiOfT = new MSubscript(variablePartialD, xiOfT);

        MSubscript partialDXiAposOfT = new MSubscript(variablePartialD, xiAposOfT);

        MParameter muJI = new MParameter("\\[Mu]", "j", "i");

        MParameter defUSubX = new MParameter(defuvarsubstitutexxx.getName(),
                parameterJ,
                new MVariable("t"),
                new MVariable("varxxx"));

        MSubscript lambdaQ = new MSubscript(variableLambda, parameterQ);
        MParameter lambdaQofT = new MParameter(lambdaQ, variableT);

        MExpressionList list1 = new MExpressionList(true);
        list1.add(partialDXiOfT);
        list1.add(defUSubX);
        MEvaluate eval1 = new MEvaluate(list1);

        MExpressionList list2 = new MExpressionList();
        list2.setDelimiter(" ");
        list2.add(muJI);
        list2.add(eval1);

        MSum sum1 = new MSum(list2, new MList(parameterJ, AG));

        MExpressionList condEvalList = new MExpressionList(true);
        condEvalList.add(partialDXiAposOfT);
        condEvalList.add(new MParameter(defzvarsubstitutexxx.getName(), parameterQ, variableT, varxxx));
        MEvaluate condTrue = new MEvaluate(condEvalList);

        MExpressionList condFalseList = new MExpressionList(true);
        condFalseList.add(partialDXiOfT);
        condFalseList.add(new MParameter(defzvarsubstitutexxx.getName(), parameterQ, variableT, varxxx));
        MEvaluate condFalse = new MEvaluate(condFalseList);

        MNotEqual cond = new MNotEqual(new MEvaluate(condEvalList), e0);
        MIf mIf = new MIf(cond, condTrue, condFalse);
        mIf.setLinebreakBeforeFunction(true);
        mIf.setLinebreakAfterCondition(true);
        mIf.setLinebreakAfterTrue(true);
        mIf.setLinebreakAfter(true);

        MExpressionList list3 = new MExpressionList();
        list3.add(lambdaQofT);
        list3.add(mIf);

        MSum sum2 = new MSum(list3, new MList(parameterQ, nZwangB));

        MAddition addition = new MAddition(sum1, sum2);
        addition.setLinebreakAfterOperator(true);

        MEqual equal = new MEqual(xiAposOfT, addition);
        equal.setLinebreakAfterOperator(true);

        MTable table = new MTable(equal, new MList(variableI, ndiffvar));

        return new MSet(dgldiffxxx, table, true);
    }

    public MSet getSetAglalgxxx() {
        MSubscript xxii = new MSubscript(variableXX, variableII);
        MParameter xxiiOfT = new MParameter(xxii, variableT);
        MParameter def = new MParameter(defalgvarsubstitutexxx.getName(), variableII, variableT, varxxx);
        MList list = new MList(parameterII, nalgvar);
        MEqual equal = new MEqual(xxiiOfT, def);
        MTable table = new MTable(equal, list);
        return new MSet(dglalgxxx, table, true);
    }

    public MSet getSetDglzxxx() {
        MParameter def = new MParameter(defzvarsubstitutexxx.getName(), parameterQ, variableT, varxxx);
        MList list = new MList(parameterQ, nZwangB);
        MEqual equal = new MEqual(e0, def);
        MTable table = new MTable(equal, list);
        return new MSet(dglzxxx, table, true);
    }

    public MSet getSetDglxxx() {
        MJoin join = new MJoin(dglalgxxx, dgldiffxxx, dglzxxx);
        return new MSet(dglxxx, join, true);
    }

    public MSet getSetDgl() {
        MReplaceAll r1 = new MReplaceAll(dglxxx, changediffxa);
        MReplaceAll r2 = new MReplaceAll(r1, changealgxxb);
        return new MSet(dgl, r2, true);
    }

    public MSet getSetInit() {
        MList list = getInitList();
        list.setElementsLinebreak(4);
        return new MSet(init, list, true);
    }

    public MList getInitList() {
        MList list = new MList();
        for (Variable v : model.getVariablesSorted()) {
            MParameter p = new MParameter(v.getName(), e0);
            if (StringUtils.isBlank(v.getInitialCondition())) {
                continue;
            }
            if (!v.getDefaultInitialCondition().equals(v.getInitialCondition())) {
                list.add(new MEqual(p, new MExpression(utils.transformToFullForm(v.getInitialCondition(), false))));
            } else {
                list.add(new MEqual(p, new MVariable(v.getDefaultInitialCondition())));
            }
        }
        return list;
    }

    public MSet getSetUgl() {
        MList list = new MList();
        list.setElementsLinebreak(1);
        for (Agent a : model.getAgentsSorted()) {
            MParameter p = new MParameter("u" + a.getName(), variableT);
            MParameter d = new MParameter(defuvar.getName(), new MVariable(agentNamePrefix + a.getName()), variableT, var);
            list.add(new MEqual(p, d));
        }
        return new MSet(ugl, list);
    }

    public MSet getSetGl() {
        MJoin join = new MJoin(ugl, dgl, init);
        return new MSet(gl, join, true);
    }

    public MSet getSetGlvar() {
        MList list = new MList();
        for (Agent a : model.getAgentsSorted()) {
            list.add(new MVariable("u" + a.getName()));
        }
        MJoin join = new MJoin(list, var, lambdaF);
        return new MSet(glvar, join);
    }

    public MSet getSetGL() {
        MReplaceAll r1 = new MReplaceAll(gl, changeMu);
        return new MSet(GL, r1);
    }

    public MManipulate getManipulate() {
        MList plotList = new MList();
        plotList.setElementsLinebreak(1);
        int id = 1;
        for (GCDPlot plot : model.getPlots()) {
            plotList.add(generatePlot(plot, GCDMode.NDSOLVE, id));
            id++;
        }

        IExpression ndSolveMethod = new MExpression(utils.transformToFullForm(
                "Method -> " + model.getNdSolveMethod()
                , false));

        MList glvarList = new MList();
        for (Agent a : model.getAgentsSorted()) {
            glvarList.add(new MVariable("u" + a.getName()));
        }
        for (IExpression e : ((MList) getSetDiffVar().getExpr2()).getElements()) {
            glvarList.add(e);
        }
        for (IExpression e : ((MList) getSetAlgVar().getExpr2()).getElements()) {
            glvarList.add(e);
        }

        for (int i = 1; i <= model.getConstraints().size(); i++) {
            glvarList.add(new MSubscript(variableLambda, new MVariable("" + i)));
        }

        MQuietNDSolve ndSolve = new MQuietNDSolve();
        MVariable outputGl = new MVariable("%");
        ndSolve.addParameter(outputGl);
        ndSolve.addLinebreak();
        ndSolve.addParameter(glvarList);
        ndSolve.addLinebreak();
        ndSolve.addParameter(new MList(parameterT, e0, e50));
        ndSolve.addParameter(ndSolveMethod);

        MList moduleList = new MList(new MSet(parameterSol, ndSolve));
        MModule module = new MModule(moduleList, plotList);
        module.setLinebreakAfterFunction(true);
        module.setLinebreakAfterParameter(true);
        MList mList = new MList(module);
        mList.setElementsLinebreak(1);
        MColumn column = new MColumn(mList);
        MManipulate manipulate = new MManipulate(column);
        manipulate.setLinebreakAfterFunction(true);
        manipulate.addLinebreak();

        if (addValuesToManipulate(manipulate) != 0) {
            manipulate.addLinebreak();
            manipulate.addLinebreak();
        }
        manipulate.addParameter(getVariableConfig(parameterTMax, e30, e0, e100));
        manipulate.addParameter(getVariableConfig(parameterPlotMax, e2p5, e0, e20));

        return manipulate;
    }

    private int addValuesToManipulate(MManipulate manipulate) {
        int blockSize = 4;
        int varInit = 0;
        for (Variable v : model.getVariablesSorted()) {
            if (v.hasValidInitValues()) {
                if (varInit % blockSize == 0) {
                    manipulate.addLinebreak();
                }
                manipulate.addParameter(getVariableConfig(v.getInitialCondition(), v.getStartValue(), v.getMinValue(), v.getMaxValue()));
                varInit++;
            }
        }
        if (varInit != 0) {
            manipulate.addLinebreak();
        }
        int muInit = 0;
        Agent lastAgent = null;
        for (ChangeMu mu : model.getChangeMus()) {
            if (mu.hasAllValues()) {
                if (lastAgent != mu.getAgent()) {
                    lastAgent = mu.getAgent();
                    if (muInit != 0) {
                        manipulate.addLinebreak();
                        muInit = 0;
                    }
                }
                if (muInit % blockSize == 0) {
                    manipulate.addLinebreak();
                }
                manipulate.addParameter(getVariableConfig(mu.getIdentifier(), mu.getStartValue(), mu.getMinValue(), mu.getMaxValue()));
                muInit++;
            }
        }
        if (muInit != 0) {
            manipulate.addLinebreak();
        }
        int paramInit = 0;
        for (Parameter p : model.getParametersSorted()) {
            if (p.hasAllValues()) {
                if (paramInit % blockSize == 0) {
                    manipulate.addLinebreak();
                }
                manipulate.addParameter(getVariableConfig(p.getName(), p.getStartValue(), p.getMinValue(), p.getMaxValue()));
                paramInit++;
            }
        }
        return paramInit;
    }

    public MSet getModelica() {
        return new MSet(variableModelica, new MCreateSystemModel(new MVariable("%"), variableT));
    }

    public MNamedCall getModelicaDisplay() {
        return new MNamedCall("modelica", new MExpression("\"\\\"ModelicaDisplay\\\"\""));
    }

    public List<MSet> getModelicaSimModel() {
        List<MSet> list = new ArrayList<>();
        int id = 1;
        for (GCDPlot p : model.getPlots()) {
            list.add(getModelicaSimModel(p, id));
            id++;
        }
        return list;
    }

    public MSet getModelicaSimModel(GCDPlot plot, int id) {
        MList list1 = new MList();
        for (GCDPlotItem<Agent> agent : plot.getAgentsSorted()) {
            list1.add(new MExpression("\"\\\"" + "u" + agent.getItem().getName() + "\\\"\""));
        }
        for (GCDPlotItem<AlgebraicVariable> algVar : plot.getAlgebraicVariablesSorted()) {
            list1.add(new MExpression("\"\\\"" + algVar.getItem().getName() + "\\\"\""));
        }
        for (PlotVariable pV : plot.getVariablesSorted()) {
            list1.add(new MExpression("\"\\\"" + pV.variable.getName() + "\\\"\""));
        }

        MList list2 = new MList(); // parameter list
        for (Parameter p : model.getParametersSorted()) {
            list2.add(new MExpression("\"\\\"" + p.getName() + "\\\"\""));
        }
        for (ChangeMu mu : model.getChangeMus()) {
            list2.add(new MExpression("\"\\\"" + mu.getIdentifier() + "\\\"\""));
        }
        for (Variable v : model.getVariablesSorted()) {
            if (v.hasValidInitValues()) {
                list2.add(new MExpression("\"\\\"" + v.getDefaultInitialCondition() + "\\\"\""));
            }
        }

        MExpressionList method = new MExpressionList();
        method.add(new MExpression("Method"));
        method.add(new MArrow());
        method.add(new MExpression("\"\\\"DASSL\\\"\""));

        MSystemModelParametricSimulate sim = new MSystemModelParametricSimulate(variableModelica,
                list1,
                e30,
                list2,
                method
        );
        sim.setLinebreakAfterParameter(true);
        return new MSet(new MVariable("simModelPlot" + id), sim);
    }

    public MManipulate getModelicaManipulate() {
        MList plotList = new MList();
        plotList.setElementsLinebreak(1);
        int id = 1;
        for (GCDPlot plot : model.getPlots()) {
            plotList.add(generatePlot(plot, GCDMode.MODELICA, id));
            id++;
        }

        MManipulate manipulate = new MManipulate(plotList);
        manipulate.setLinebreakAfterFunction(true);
        manipulate.addLinebreak();
        addValuesToManipulate(manipulate);
        manipulate.addLinebreak();
        manipulate.addParameter(getVariableConfig(parameterTMax, e30, e0, e100));
        manipulate.addParameter(getVariableConfig(parameterPlotMax, e2p5, e0, e20));

        return manipulate;
    }

    private MathematicaPlot generatePlot(GCDPlot plot, GCDMode mode, int id) {
        MEvaluate evaluate = null;
        if (mode == GCDMode.NDSOLVE) {
            MList fList = new MList();
            for (GCDPlotItem<Agent> agent : plot.getAgentsSorted()) {
                fList.add(new MExpression(utils.transformToFullForm(agent.getItem().getFunction(), false)));
            }
            for (GCDPlotItem<AlgebraicVariable> algVar : plot.getAlgebraicVariablesSorted()) {
                fList.add(new MParameter(algVar.getItem().getName(), variableT));
            }
            for (PlotVariable pV : plot.getVariablesSorted()) {
                fList.add(new MParameter(pV.variable.getName(), variableT));
            }
            evaluate = new MEvaluate(new MReplaceAll(fList, variableSol));
        } else if (mode == GCDMode.MODELICA) {
            MThrough t = new MThrough("simModelPlot" + id);
            for (Parameter p : model.getParametersSorted()) {
                t.addParameter(new MExpression(p.getName()));
            }
            for (ChangeMu mu : model.getChangeMus()) {
                t.addParameter(new MExpression(mu.getIdentifier()));
            }
            for (Variable v : model.getVariablesSorted()) {
                if (v.hasValidInitValues()) {
                    t.addParameter(new MExpression(v.getDefaultInitialCondition()));
                }
            }
            evaluate = new MEvaluate(t);
        }

        MExpression plotParameter = new MExpression(utils.transformToFullForm(plot.getPlotParameter(), false));

        MExpressionList plotRange = new MExpressionList();
        plotRange.add(new MVariable("PlotRange"));
        plotRange.add(new MArrow());
        plotRange.add(new MExpression(utils.transformToFullForm(plot.getPlotRange(), false)));

        MExpressionList plotStyle = new MExpressionList();
        plotStyle.add(new MVariable("PlotStyle"));
        plotStyle.add(new MArrow());
        plotStyle.add(new MVariable(plot.getPlotStyle()));

        MList legendList = new MList();
        for (GCDPlotItem<Agent> agent : plot.getAgentsSorted()) {
            legendList.add(new MExpression("\"\\\"u" + agent.getItem().getName() + "\\\"\""));
        }
        for (GCDPlotItem<AlgebraicVariable> algVar : plot.getAlgebraicVariablesSorted()) {
            legendList.add(new MExpression("\"\\\"" + algVar.getItem().getName() + "\\\"\""));
        }
        for (PlotVariable pV : plot.getVariablesSorted()) {
            legendList.add(new MExpression("\"\\\"" + pV.variable.getName() + "\\\"\""));
        }

        MExpressionList legendLabel = new MExpressionList();
        legendLabel.add(new MVariable("LegendLabel"));
        legendLabel.add(new MArrow());
        legendLabel.add(new MExpression("\"\\\"" + plot.getLegendLabel() + "\\\"\""));

        RowBox lineLegend = new RowBox();
        lineLegend.add(new MVariable("LineLegend"));
        lineLegend.add(new MVariable("["));
        lineLegend.add(legendList);
        if (StringUtils.isNotBlank(plot.getLegendLabel())) {
            lineLegend.add(new MVariable(","));
            lineLegend.add(legendLabel);
        }
        lineLegend.add(new MVariable("]"));

        MExpressionList plotLegende = new MExpressionList();
        plotLegende.add(new MVariable("PlotLegends"));
        plotLegende.add(new MArrow());
        plotLegende.add(lineLegend);

        MExpressionList plotLabels = new MExpressionList();
        plotLabels.add(new MVariable("PlotLabels"));
        plotLabels.add(new MArrow());
        plotLabels.add(legendList);

        MExpressionList plotName = new MExpressionList();
        plotName.add(new MVariable("PlotLabel"));
        plotName.add(new MArrow());
        plotName.add(new MExpression("\"\\\"" + plot.getName() + "\\\"\""));

        MathematicaPlot mPlot = new MathematicaPlot();
        mPlot.setPlotName(plotName);
        mPlot.setPlotFunction(evaluate);
        mPlot.setPlotParameter(plotParameter);
        mPlot.setPlotRange(plotRange);
        mPlot.setPlotStyle(plotStyle);
        if (plot.isShowPlotLabels()) {
            mPlot.setPlotLabels(plotLabels);
        }
        if (plot.isShowLegendLabels()) {
            mPlot.setPlotLegends(plotLegende);
        }
        return mPlot;
    }

    public MComment getControlAlgVarComment() {
        return controlAlgVarComment;
    }

    public MComment getControlAgentComment() {
        return controlAgentComment;
    }

    public MComment getControlConstraintComment() {
        return controlConstraintComment;
    }

    public MComment getControlVariableComment() {
        return controlVariableComment;
    }

    public List<RowBox> getRowBoxControlAlgebraicVariables() {
        List<RowBox> list = new ArrayList<>();
        for (AlgebraicVariable aV : model.getAlgebraicVariablesSorted()) {
            list.add(new RowBox(true, new MExpression(aV.getMathematicaString(), false)));
        }
        return list;
    }

    public List<RowBox> getRowBoxControlAgents() {
        List<RowBox> list = new ArrayList<>();
        for (Agent agent : model.getAgentsSorted()) {
            list.add(new RowBox(true, new MExpression(agent.getMathematicaString(), false)));
        }
        return list;
    }

    public List<RowBox> getRowBoxControlConstraints() {
        List<RowBox> list = new ArrayList<>();
        for (Constraint constraint : model.getConstraints()) {
            list.add(new RowBox(true, new MExpression(constraint.getMathematicaString(), false)));
        }
        return list;
    }

    public MList getControlDiffVarStartValues() {
        MList list = new MList();

        list.setElementsLinebreak(4);
        for (Variable v : model.getVariablesSorted()) {
            if (v.hasValidInitValues()) {
                list.add(getVariableConfig(v.getInitialCondition(), v.getStartValue(), v.getMinValue(), v.getMaxValue()));
            }
        }
        list.add(MFunction.linebreak);

        int muInit = 0;
        Agent lastAgent = null;
        for (ChangeMu mu : model.getChangeMus()) {
            if (mu.hasAllValues()) {
                if (lastAgent != mu.getAgent()) {
                    lastAgent = mu.getAgent();
                    if (muInit != 0) {
                        list.add(MFunction.linebreak);
                        muInit = 0;
                    }
                }
                list.add(getVariableConfig(mu.getIdentifier(), mu.getStartValue(), mu.getMinValue(), mu.getMaxValue()));
                muInit++;
            }
        }
        list.add(MFunction.linebreak);
        for (Parameter p : model.getParametersSorted()) {
            if (p.hasAllValues()) {
                list.add(getVariableConfig(p.getName(), p.getStartValue(), p.getMinValue(), p.getMaxValue()));
            }
        }
        list.add(MFunction.linebreak);
        list.add(getVariableConfig(parameterTMax, e30, e0, e100));
        list.add(getVariableConfig(parameterPlotMax, e2p5, e0, e20));
        return list;
    }

    public List<RowBox> getRowBoxControlDiffVars() {
        List<RowBox> list = new ArrayList<>();
        list.add(new RowBox(true, getSetInit()));
        list.add(new RowBox(true, getControlDiffVarStartValues()));
        return list;
    }

    public static List<RowBox> convertSetToRowBoxList(List<MSet> set) {
        List<RowBox> list = new ArrayList<>();
        for (MSet s : set) {
            list.add(new RowBox(true, s));
        }
        return list;
    }

    public static List<RowBox> convertDelayedToRowBoxList(List<MSetDelayed> setDelayedList) {
        List<RowBox> list = new ArrayList<>();
        for (MSetDelayed setDelayed : setDelayedList) {
            list.add(new RowBox(true, setDelayed));
        }
        return list;
    }

    public static List<RowBox> convertParameterToRowBoxList(List<MParameter> parameters) {
        List<RowBox> list = new ArrayList<>();
        for (MParameter parameter : parameters) {
            list.add(new RowBox(true, parameter));
        }
        return list;
    }

    private MList getVariableConfig(IExpression name, IExpression startValue, IExpression minValue, IExpression maxValue) {
        return new MList(new MList(name, startValue), minValue, maxValue);
    }

    private MList getVariableConfig(String name, double startValue, double minValue, double maxValue) {
        return new MList(new MList(new MExpression("parameter", name), new MVariable(startValue)), new MVariable(minValue), new MVariable(maxValue));
    }
}