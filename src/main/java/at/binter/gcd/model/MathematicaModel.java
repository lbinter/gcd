package at.binter.gcd.model;

import at.binter.gcd.mathematica.elements.MParameter;
import at.binter.gcd.mathematica.elements.MVariable;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MComment;
import at.binter.gcd.mathematica.syntax.MExpression;
import at.binter.gcd.mathematica.syntax.MExpressionList;
import at.binter.gcd.mathematica.syntax.binary.*;
import at.binter.gcd.mathematica.syntax.function.*;
import at.binter.gcd.mathematica.syntax.group.MList;
import at.binter.gcd.mathematica.syntax.group.MParentheses;
import at.binter.gcd.mathematica.syntax.unary.MReplaceAll;
import at.binter.gcd.model.elements.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MathematicaModel {
    private static final Logger log = LoggerFactory.getLogger(MathematicaModel.class);
    private final GCDModel model;

    public MathematicaModel(GCDModel model) {
        this.model = model;
    }

    public GCDModel getModel() {
        return model;
    }

    private boolean clearGlobal = true;

    private final MExpression linebreak = new MExpression("", true);
    private final MComment diffVarComment = new MComment("durch Differentialgleichungen bestimmte Variable");
    private final MVariable diffVar = new MVariable("diffvar");
    private final MVariable nDiffVar = new MVariable("ndiffvar");
    private final MComment algVarComment = new MComment("durch algebraische Definitionsgleichungen bestimmte Variable");
    private final MVariable algVar = new MVariable("algvar");
    private final MVariable nAlgVar = new MVariable("nalgvar");
    private final MComment varComment = new MComment("gesamte Variable");
    private final MVariable var = new MVariable("var");
    private final MVariable nVar = new MVariable("nvar");

    private final MComment AGComment = new MComment("Agenten");
    private final MVariable AG = new MVariable("AG");
    private final MVariable nAG = new MVariable("nAG");

    private final MComment substituteComment = new MComment("algebraische Definitionsgleichungen der \"algebraischen\" Variablen algvar");
    private final MVariable substitute = new MVariable("substitute");
    private final MParameter defAlgVar = new MParameter("defalgvar", "ii_", "t_", "var_");
    private final MParameter defAlgVarSubstitute = new MParameter("defalgvarsubstitute", "ii_", "t_", "var_");
    private final MComment defuVarComment = new MComment("Definitionsgleichungen der Nutzenfunktionen");
    private final MParameter defuVar = new MParameter("defuvar", "j_", "t_", "var_");
    private final MParameter defuVarSubstitute = new MParameter("defuvarsubstitute", "j_", "t_", "var_");
    private final MComment nZwangBComment = new MComment("Definitionsgleichungen der Zwangsbedingungen");
    private final MVariable nZwangB = new MVariable("nZwangB");
    private final MParameter defzVar = new MParameter("defzvar", "q_", "t_", "var_");
    private final MParameter defzVarSubstitute = new MParameter("defzvarsubstitute", "q_", "t_", "var_");
    private final MComment mfiComment = new MComment("indizierte Machtfaktoren");
    private final MVariable mfi = new MVariable("MFi");
    private final MTable mfiTable = new MTable(
            new MParameter("\\[Mu]", "j", "i"),
            new MList(new MExpression("j"), AG),
            new MList(new MExpression("i"), nDiffVar));
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
                    new MSubscript(new MVariable("\\[Lambda]"), new MExpression("parameter", "#")),
                    new MExpression("&")
            ), new MList(nZwangB));
    private final MComment diffVarXComment = new MComment("Wandle die aktuellen Variablen diffvar um in ein Array diffvarx von indizierten Variablen x der Länge ndiffvar");
    private final MVariable diffVarX = new MVariable("diffvarx");
    private final MArray diffVarXArray = new MArray(
            new MExpressionList(
                    new MSubscript(new MVariable("x"), new MExpression("parameter", "#")),
                    new MExpression("&")
            ), new MList(nDiffVar));
    private final MVariable algVarXX = new MVariable("algvarxx");
    private final MArray algVarXXArray = new MArray(
            new MExpressionList(
                    new MSubscript(new MVariable("xx"), new MExpression("parameter", "#")),
                    new MExpression("&")
            ), new MList(nAlgVar));
    private final MVariable varXXX = new MVariable("varxxx");
    private final MJoin varXXXJoin = new MJoin(diffVarX, algVarXX);
    private final MVariable changeDiffaX = new MVariable("changediffax");
    private final MVariable changeDiffXa = new MVariable("changediffxa");
    private final MVariable changeAlgbXX = new MVariable("changealgbxx");
    private final MVariable changeAlgXXb = new MVariable("changealgxxb");
    private final MParameter defAlgVarSubstituteXXX = new MParameter("defalgvarsubstitutexxx", "jj_", "t_", "varxxx_");
    private final MParameter defUVarSubstituteXXX = new MParameter("defuvarsubstitutexxx", "j_", "t_", "varxxx_");
    private final MParameter defZVarSubstituteXXX = new MParameter("defzvarsubstitutexxx", "q_", "t_", "varxxx_");

    public boolean isClearGlobal() {
        return clearGlobal;
    }

    public MExpression getLinebreak() {
        return linebreak;
    }

    public void setClearGlobal(boolean clearGlobal) {
        this.clearGlobal = clearGlobal;
    }

    public MComment getDiffVarComment() {
        return diffVarComment;
    }

    public MComment getAlgVarComment() {
        return algVarComment;
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

    public MComment getDefuVarComment() {
        return defuVarComment;
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

    public MComment getDiffVarXComment() {
        return diffVarXComment;
    }

    public MSet getSetDiffVar() {
        MList list = new MList();
        for (Variable v : model.getVariablesSorted()) {
            list.add(new MExpression("variable", v.getName()));
        }
        return new MSet(diffVar, list);
    }

    public MSet getSetnDiffVar() {
        return new MSet(nDiffVar, new MLength(diffVar));
    }

    public MSet getSetAlgVar() {
        MList list = new MList();
        for (AlgebraicVariable v : model.getAlgebraicVariablesSorted()) {
            list.add(new MExpression("variable", v.getName()));
        }
        return new MSet(algVar, list);
    }

    public MSet getSetnAlgVar() {
        return new MSet(nAlgVar, new MLength(algVar));
    }

    public MSet getSetVar() {
        return new MSet(var, new MJoin(diffVar, algVar));
    }

    public MSet getSetnVar() {
        return new MSet(nVar, new MLength(var));
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
            l.add(new MExpression("&#8594;"));
            l.add(new MExpression(aV.getFunction()));
            list.add(l);
        }
        return new MSet(substitute, list, true);
    }

    public List<MSetDelayed> getSetDelayedDefalgvar() {
        List<MSetDelayed> list = new ArrayList<>(model.getAlgebraicVariables().size());
        int ii = 1;
        for (AlgebraicVariable aV : model.getAlgebraicVariablesSorted()) {
            MParameter algVar = new MParameter(aV.getName(), aV.getParameter());
            MSetDelayed delayed = new MSetDelayed(defAlgVar,
                    new MCondition(
                            algVar,
                            new MEqual(new MExpression("parameter", "ii"), new MExpression(ii))
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
        return new MParameter(defAlgVar.getName(), String.valueOf(ii), "t", "var");
    }

    private MParameter getDefAlgVarSubstitute(int ii) {
        if (ii > model.getAlgebraicVariables().size()) {
            log.error("Called getDefAlgVarSubstitute with ii of {}", ii);
        }
        return new MParameter(defAlgVarSubstitute.getName(), String.valueOf(ii), "t", "var");
    }

    public List<MSetDelayed> getSetDelayedDefAlgVarSubstitute() {
        List<MSetDelayed> list = new ArrayList<>(model.getAlgebraicVariables().size());
        for (int ii = 1; ii <= model.getAlgebraicVariables().size(); ii++) {
            MParentheses parentheses = new MParentheses(new MReplaceAll(getDefalgVar(ii), substitute));
            MSetDelayed delayed = new MSetDelayed(defAlgVarSubstitute,
                    new MCondition(
                            parentheses,
                            new MEqual(new MExpression("parameter", "ii"), new MExpression(ii))
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
        return new MParameter(defuVar.getName(), index, "t", "var");
    }

    public MParameter getDefuVarSubstitute(String index) {
        if (StringUtils.isBlank(index)) {
            log.error("Called getDefuVarSubstitute with index of null");
        } else if (model.getAgent(index) == null) {
            log.error("Called getDefuVarSubstitute with non exists agent index of {}", index);
        }
        return new MParameter(defuVarSubstitute.getName(), index, "t", "var");
    }

    public List<IExpression> getSetDelayedDefuVar() {
        List<IExpression> list = new ArrayList<>(model.getAgentsSorted().size());
        for (Agent a : model.getAgentsSorted()) {
            if (StringUtils.isNotBlank(a.getDescription())) {
                list.add(new MComment(a.getDescription()));
            }
            MSetDelayed delayed = new MSetDelayed(defuVar,
                    new MCondition(
                            new MParentheses(new MExpression(a.getFunction())),
                            new MEqual(
                                    new MExpression("parameter", "j"),
                                    new MExpression("variable", a.getName()
                                    ))),
                    true);
            list.add(delayed);
        }
        return list;
    }

    public List<MSetDelayed> getSetDelayedDefuVarSubstitute() {
        List<MSetDelayed> list = new ArrayList<>(model.getAgents().size());
        for (Agent a : model.getAgentsSorted()) {
            MParentheses parentheses = new MParentheses(new MReplaceAll(getDefuVar(a.getName()), substitute));
            MSetDelayed delayed = new MSetDelayed(defuVarSubstitute,
                    new MCondition(
                            parentheses,
                            new MEqual(
                                    new MExpression("parameter", "j"),
                                    new MExpression("variable", a.getName()
                                    ))),
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
        return new MParameter(defzVar.getName(), String.valueOf(ii), "t", "var");
    }

    public MParameter getDefzVarSubstitute(int ii) {
        if (ii > model.getConstraints().size()) {
            log.error("Called getDefzVarSubstitute with q of {}", ii);
        }
        return new MParameter(defzVarSubstitute.getName(), String.valueOf(ii), "t", "var");
    }

    public List<IExpression> getSetDelayedDefzVar() {
        List<IExpression> list = new ArrayList<>(model.getAgents().size());
        for (Constraint c : model.getConstraints()) {
            if (StringUtils.isNotBlank(c.getDescription())) {
                list.add(new MComment(c.getDescription()));
            }
            MSetDelayed delayed = new MSetDelayed(defzVar,
                    new MCondition(
                            new MParentheses(new MExpression(c.getCondition())),
                            new MEqual(
                                    new MExpression("parameter", "q"),
                                    new MExpression(c.getId())
                            )),
                    true);
            list.add(delayed);
        }
        return list;
    }

    public List<MSetDelayed> getSetDelayedDefzVarSubstitute() {
        List<MSetDelayed> list = new ArrayList<>(model.getAgents().size());
        for (Constraint c : model.getConstraints()) {
            MParentheses parentheses = new MParentheses(new MReplaceAll(getDefzVar(c.getId()), substitute));
            MSetDelayed delayed = new MSetDelayed(defzVarSubstitute,
                    new MCondition(
                            parentheses,
                            new MEqual(
                                    new MExpression("parameter", "q"),
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
        return new MSet(flattenMFi, new MFlatten(flattenMFi), true);
    }

    public MSet getSetChangeMu() {
        MList list = new MList();
        list.setElementsLinebreak(5);
        list.setElementsBlock(model.getVariables().size());
        for (ChangeMu mu : model.getAllChangeMu()) {
            MExpressionList l = new MExpressionList();
            l.add(new MParameter(
                    "\\[Mu]",
                    new MVariable(mu.getAgent().getName()),
                    new MExpression(mu.getIndex())
            ));
            l.add(new MExpression("&#8594;"));
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
        return new MSet(diffVarX, diffVarXArray, true);
    }

    public MSet getSetAlgVarXX() {
        return new MSet(algVarXX, algVarXXArray, true);
    }

    public MSet getSetVarXXX() {
        return new MSet(varXXX, varXXXJoin);
    }

    public MSet getSetChangeDiffaX() {
        MList list = new MList();
        list.setElementsLinebreak(4);
        int count = 1;
        for (Variable v : model.getVariablesSorted()) {
            MExpressionList l = new MExpressionList();
            l.add(new MVariable(v.getName()));
            l.add(new MExpression("&#8594;"));
            l.add(new MSubscript(new MVariable("x"), new MExpression(count)));
            list.add(l);
            count++;
        }
        return new MSet(changeDiffaX, list);
    }

    public MSet getSetChangeDiffXa() {
        MList list = new MList();
        list.setElementsLinebreak(4);
        int count = 1;
        for (Variable v : model.getVariablesSorted()) {
            MExpressionList l = new MExpressionList();
            l.add(new MSubscript(new MVariable("x"), new MExpression(count)));
            l.add(new MExpression("&#8594;"));
            l.add(new MVariable(v.getName()));
            list.add(l);
            count++;
        }
        return new MSet(changeDiffXa, list);
    }

    public MSet getSetChangeAlgbXX() {
        MList list = new MList();
        list.setElementsLinebreak(4);
        int count = 1;
        for (AlgebraicVariable v : model.getAlgebraicVariablesSorted()) {
            MExpressionList l = new MExpressionList();
            l.add(new MVariable(v.getName()));
            l.add(new MExpression("&#8594;"));
            l.add(new MSubscript(new MVariable("xx"), new MExpression(count)));
            list.add(l);
            count++;
        }
        return new MSet(changeAlgbXX, list);
    }

    public MSet getSetChangeAlgXXb() {
        MList list = new MList();
        list.setElementsLinebreak(4);
        int count = 1;
        for (AlgebraicVariable v : model.getAlgebraicVariablesSorted()) {
            MExpressionList l = new MExpressionList();
            l.add(new MSubscript(new MVariable("xx"), new MExpression(count)));
            l.add(new MExpression("&#8594;"));
            l.add(new MVariable(v.getName()));
            list.add(l);
            count++;
        }
        return new MSet(changeAlgXXb, list);
    }

    public MSetDelayed getSetDelayedDefAlgVarSubstituteXXX() {
        return new MSetDelayed(defAlgVarSubstituteXXX,
                new MReplaceAll(
                        new MReplaceAll(
                                new MParameter(defAlgVarSubstitute.getName(), "jj", "t", "var"),
                                changeDiffaX),
                        changeAlgbXX),
                true
        );
    }

    public MSetDelayed getSetDelayedDefUVarSubstituteXXX() {
        return new MSetDelayed(defUVarSubstituteXXX,
                new MReplaceAll(
                        new MReplaceAll(
                                new MParameter(defuVarSubstitute.getName(), "j", "t", "var"),
                                changeDiffaX),
                        changeAlgbXX),
                true
        );
    }

    public MSetDelayed getSetDelayedDefZVarSubstituteXXX() {
        return new MSetDelayed(defZVarSubstituteXXX,
                new MReplaceAll(
                        new MReplaceAll(
                                new MParameter(defzVarSubstitute.getName(), "q", "t", "var"),
                                changeDiffaX),
                        changeAlgbXX),
                true
        );
    }

    public List<MParameter> getParameterListDefUVarSubstituteXXX() {
        List<MParameter> list = new ArrayList<>();
        for (Agent a : model.getAgentsSorted()) {
            list.add(new MParameter(defUVarSubstituteXXX.getName(), a.getName(), "t", "varxxx"));
        }
        return list;
    }

    public List<MParameter> getParameterListDefZVarSubstituteXXX() {
        List<MParameter> list = new ArrayList<>();
        for (Constraint c : model.getConstraints()) {
            list.add(new MParameter(defZVarSubstituteXXX.getName(), String.valueOf(c.getId()), "t", "varxxx"));
        }
        return list;
    }
}
