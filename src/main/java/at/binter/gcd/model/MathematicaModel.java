package at.binter.gcd.model;

import at.binter.gcd.mathematica.elements.MParameter;
import at.binter.gcd.mathematica.elements.MVariable;
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
    private final MathematicaUtils utils = new MathematicaUtils();

    public MathematicaModel(GCDModel model) {
        this.model = model;
    }

    public MathematicaUtils getUtils() {
        return utils;
    }

    public GCDModel getModel() {
        return model;
    }

    private final MVariable variableT = new MVariable("t");
    private final MVariable variableX = new MVariable("x");
    private final MVariable variableXX = new MVariable("xx");
    private final MVariable variableLambda = new MVariable("\\[Lambda]");
    private final MVariable variablePartialD = new MVariable("\\[PartialD]");
    private final MVariable variableSharp = new MVariable("#");
    private final MVariable variableI = new MVariable("i");
    private final MVariable variableII = new MVariable("ii");
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
            list.add(new MExpression("variable", a.getName()));
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
        } else if (model.getAgent(index) == null) {
            log.error("Called getDefuVar with non exists agent index of {}", index);
        }
        return new MParameter(defuvar.getName(), index, "t", "var");
    }

    public MParameter getDefuVarSubstitute(String index) {
        if (StringUtils.isBlank(index)) {
            log.error("Called getDefuVarSubstitute with index of null");
        } else if (model.getAgent(index) == null) {
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
                                    new MExpression("variable", a.getName()))),
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
                            new MParentheses(new MExpression(utils.transformToFullForm(a.getFunction(), false))),
                            new MEqual(
                                    parameterJ,
                                    new MExpression("variable", a.getName()))),
                    true);
            list.add(new RowBox(true, delayed));
        }
        return list;
    }

    public List<MSetDelayed> getSetDelayedDefuVarSubstitute() {
        List<MSetDelayed> list = new ArrayList<>(model.getAgents().size());
        for (Agent a : model.getAgentsSorted()) {
            MParentheses parentheses = new MParentheses(new MReplaceAll(getDefuVar(a.getName()), substitute));
            MSetDelayed delayed = new MSetDelayed(defuvarsubstitute,
                    new MCondition(
                            parentheses,
                            new MEqual(
                                    parameterJ,
                                    new MExpression("variable", a.getName()))),
                    true);
            list.add(delayed);
        }
        return list;
    }

    public List<MParameter> getParameterListDefuVar() {
        List<MParameter> list = new ArrayList<>();
        for (Agent a : model.getAgentsSorted()) {
            list.add(getDefuVar(a.getName()));
        }
        return list;
    }

    public List<MParameter> getParameterListDefuVarSubstitute() {
        List<MParameter> list = new ArrayList<>();
        for (Agent a : model.getAgentsSorted()) {
            list.add(getDefuVarSubstitute(a.getName()));
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
                            new MParentheses(new MExpression(utils.transformToFullForm(c.getCondition(), false))),
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
                    new MExpression(mu.getAgent().getName()),
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
            list.add(new MParameter(defuvarsubstitutexxx.getName(), a.getName(), "t", "varxxx"));
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
        MList list = new MList();
        list.setElementsLinebreak(4);
        for (Variable v : model.getVariablesSorted()) {
            MParameter p = new MParameter(v.getName(), e0);
            if (StringUtils.isNotBlank(v.getInitialCondition())) {
                list.add(new MEqual(p, new MExpression(v.getInitialCondition())));
            } else {
                list.add(new MEqual(p, new MVariable(v.getDefaultInitialCondition())));
            }
        }
        return new MSet(init, list, true);
    }

    public MSet getSetUgl() {
        MList list = new MList();
        list.setElementsLinebreak(1);
        for (Agent a : model.getAgentsSorted()) {
            MParameter p = new MParameter("u" + a.getName(), variableT);
            MParameter d = new MParameter(defuvar.getName(), new MVariable(a.getName()), variableT, var);
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

        MVariable replaceMe = new MVariable("PLOT_LIST");
        replaceMe.setCssClass("replaceMe");
        MList plotList = new MList(replaceMe);
        plotList.setElementsLinebreak(1);
        // TODO add plots

        MExpressionList conMethod = new MExpressionList();
        conMethod.add(new MExpression("\"ConstraintMethod\""));
        conMethod.add(new MArrow());
        conMethod.add(new MExpression("\"Projection\""));
        MExpressionList indexReductionList = new MExpressionList();
        indexReductionList.add(new MExpression("\"IndexReduction\""));
        indexReductionList.add(new MArrow());
        indexReductionList.add(new MList(new MExpression("True"), conMethod));
        MExpressionList methodList = new MExpressionList();
        methodList.add(new MExpression("Method"));
        methodList.add(new MArrow());
        methodList.add(new MList(indexReductionList));

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
        glvarList.add(lambdaF);
        MQuietNDSolve ndSolve = new MQuietNDSolve();
        replaceMe = new MVariable("OUTPUT_GL");
        ndSolve.addParameter(replaceMe);
        ndSolve.addLinebreak();
        ndSolve.addParameter(glvarList);
        ndSolve.addLinebreak();
        ndSolve.addParameter(new MList(parameterT, e0, e50));
        ndSolve.addParameter(methodList);

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
        if (paramInit != 0) {
            manipulate.addLinebreak();
            manipulate.addLinebreak();
        }
        manipulate.addParameter(getVariableConfig(parameterTMax, e30, e0, e100));
        manipulate.addParameter(getVariableConfig(parameterPlotMax, e2p5, e0, e20));

        return manipulate;
    }

    public static List<RowBox> convertToRowBoxList(List<MSetDelayed> setDelayedList) {
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
        return new MList(new MList(new MExpression("parameter", name), new MExpression(startValue)), new MExpression(minValue), new MExpression(maxValue));
    }
}