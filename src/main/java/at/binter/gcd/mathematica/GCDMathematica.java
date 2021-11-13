package at.binter.gcd.mathematica;

import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.model.elements.Constraint;
import at.binter.gcd.model.elements.Variable;

import java.util.List;

public interface GCDMathematica {
    GCDMode getGCDMode(GCDMode mode);

    String toString();

    boolean writeToFile() throws Exception;

    /**
     * <pre>
     *     (* Erstellt am &lt;Date&gt; *)
     *     ClearAll["Global`*"]
     *     {@link #generatePlotStyles()}
     *     {@link #generateVariableDefinition()}
     *     {@link #generateAgentDefinition()}
     *     {@link #generateSubstitutes()}
     *     {@link #generateConstraintDefinition()}
     *     {@link #generateChangeMus()}
     *     {@link #generateTransformVariables()}
     *     {@link #generateIndexedVariables()}
     *     {@link #generateSubstituteVariables()}
     *     {@link #generateSubstituteFunctions()}
     *     {@link #generateBehavioralEquation()}
     *     {@link #generateInitialConditions()}
     *     {@link #generateSystemOfEquation()}
     * </pre>
     */
    void generate();

    /**
     * Block 1:
     * <pre>
     *     &lt;List of plot styles&gt;
     *     PLOTSTYLE&lt;id&gt; := {
     *          &lt;List of directives&gt;
     *     };
     * </pre>
     */
    void generatePlotStyles();

    /**
     * Block 1:
     * <pre>
     *     (* durch Differentialgleichungen bestimmte Variable *)
     *     diffvar = {{@link GCDModel#getVariables()}}
     *     ndiffvar = Length[diffvar]
     *     (* durch algebraische Definitionsgleichungen bestimmte Variable *)
     *     algvar = {{@link GCDModel#getAlgebraicVariables()}}
     *     nalgvar = Length[algvar]
     *     (* gesamte Variable *)
     *     var = Join[diffvar, algvar]
     *     nvar = Length[var]
     * </pre>
     */
    void generateVariableDefinition();

    /**
     * Block 2:
     * <pre>
     *     (* Agenten *)
     *     AG = {<i>&lt;List of {@link Agent#getName()}&gt;</i>}
     *     nAG = Length[AG]
     * </pre>
     */
    void generateAgentDefinition();

    /**
     * Block 4:
     * <pre>
     *     <i>&lt;index - starts with 1&gt;</i>
     *
     *     (* algebraische Definitionsgleichungen der "algebraischen" Variablen \ algvar: Produktionsfunktion *)
     *     substitute = {
     *         <i>&lt;List of {@link AlgebraicVariable#getFunction()}&gt;</i>
     *     }
     *     <i>&lt;for each {@link AlgebraicVariable}&gt;</i>
     *     defalgvar[ii_, t_, var_] := <i>&lt;{@link AlgebraicVariable#getName()}&gt;</i> /; ii == <i>&lt;index&gt;</i>;
     *
     *     <i>&lt;for each {@link AlgebraicVariable}&gt;</i>
     *     defalgvarsubstitute[ii_, t_, var_] := (defalgvar[<i>&lt;index&gt;</i>, t, var] /. substitute) /; ii == <i>&lt;index&gt;</i>;
     *
     *     <i>&lt;for each {@link AlgebraicVariable}&gt;</i>
     *     defalgvar[<i>&lt;index&gt;</i>, t, var]
     *
     *     <i>&lt;for each {@link AlgebraicVariable}&gt;</i>
     *     defalgvarsubstitute[<i>&lt;index&gt;</i>, t, var]
     *
     *     (* Definitionsgleichungen der Nutzenfunktionen *)
     *     <i>&lt;for each {@link Agent}&gt;</i>
     *     defuvar[j_, t_, var_] := <i>&lt;{@link Agent#getFunction()} ()}&gt;</i> /; j == <i>&lt;{@link Agent#getName()}&gt;</i>;
     *
     *     <i>&lt;for each {@link Agent}&gt;</i>
     *     defuvarsubstitute[j_, t_, var_] := defuvar[<i>&lt;{@link Agent#getName()}&gt;</i>, t, var] /. substitute) /; j == <i>&lt;{@link Agent#getName()}&gt;</i>;
     *
     *     <i>&lt;for each {@link Agent}&gt;</i>
     *     defuvar[<i>&lt;{@link Agent#getName()}&gt;</i>, t, var]
     *
     *     <i>&lt;for each {@link Agent}&gt;</i>
     *     defuvarsubstitute[<i>&lt;{@link Agent#getName()}&gt;</i>, t, var]
     * </pre>
     */
    void generateSubstitutes();

    /**
     * Block 3:
     * <pre>
     *     (* Definitionsgleichungen der Zwangsbedingungen *)
     *     nZwangB = <i>&lt;{@link GCDModel#getConstraints()}.getSize()&gt;</i>
     *
     *     <i>&lt;for each {@link Constraint}&gt;</i>
     *     (* <i>&lt;{@link Constraint#getDescription()}&gt;</i> *)
     *     defzvar[q_, t_, var_] := <i>&lt;{@link Constraint#getCondition()} ()}&gt;</i>  /; q == <i>&lt;{@link Constraint#getId()} ()}&gt;</i>;
     *
     *     <i>&lt;for each {@link Constraint}&gt;</i>
     *     defzvar[<i>&lt;{@link Constraint#getId()} ()}&gt;</i>, t, var]
     *
     *     <i>&lt;for each {@link Constraint}&gt;</i>
     *     defzvarsubstitute[q_, t_, var_] := (defzvar[<i>&lt;{@link Constraint#getId()} ()}&gt;</i>, t, var] /. substitute) /; q == <i>&lt;{@link Constraint#getId()} ()}&gt;</i>;
     *
     *     <i>&lt;for each {@link Constraint}&gt;</i>
     *     defzvarsubstitute[<i>&lt;{@link Constraint#getId()} ()}&gt;</i>, t, var]
     * </pre>
     */
    void generateConstraintDefinition();

    /**
     * Block 5
     * <pre>
     *     <i>&lt;index - starts with 1&gt;</i>
     *
     *     (* indizierte Machtfaktoren *)
     *     MFi = Table[&mu;[j, i], {j, AG}, {i, ndiffvar}];
     *     MFi // MatrixForm
     *
     *     (* Machtfaktoren müssen in Manipulate als nichtindizierte Variable angegeben werden, indizierte Variable können in Manipulate nicht manipuliert werden!!! *)
     *     flattenMFi = Flatten[MFi];
     *
     *     change&mu; = {
     *         <i>&lt;for each {@link Agent}, {@link Variable}&gt;</i>
     *         &mu;[<i>&lt;{@link Agent#getName()}&gt;</i>,<i>&lt;index&gt;</i>] -> &mu;<i>&lt;{@link Agent#getName()}&gt;</i><i>&lt;{@link Variable#getName()}&gt;</i>
     *     }
     *
     *     (* Machtfaktoren als nichtindizierte Variable *)
     *     MFex = flattenMFi /. change&mu;
     * </pre>
     */
    void generateChangeMus();

    /**
     * Block 6:
     * <pre>
     *     (* Lagrangefaktoren *)
     *     &lambda;F = Array[Subscript[&lambda;, #] &, {nZwangB}]
     *     (*
     *       Wandle die aktuellen Variablen diffvar {c,l,k,mh,mf,s,p,w}
     *       um in ein Array diffvarx von indizierten Variablen x der
     *       Länge ndiffvar  {Subscript[x, 1],...,Subscript[x, 8]}
     *       Wandle die aktuellen Variablen algvar {y}  um in ein Array von
     *       indizierten Variablen xx der Länge nalgvar {Subscript[xx, 1]}
     *     *)
     *     diffvarx = Array[Subscript[x, #] &, {ndiffvar}];
     *     algvarxx = Array[Subscript[xx, #] &, {nalgvar}];
     *     varxxx = Join[diffvarx, algvarxx]
     * </pre>
     */
    void generateTransformVariables();

    /**
     * Block 7:
     * <pre>
     *     <i>&lt;index - starts with 1&gt;</i>
     *
     *     changediffax = {
     *         <i>&lt;for each {@link Variable}&gt;</i>
     *         <i>&lt;{@link Variable#getName()}&gt;</i> -> Subscript[x, <i>&lt;index&gt;</i>]
     *     }
     *
     *     changediffxa = {
     *         <i>&lt;for each {@link Variable}&gt;</i>
     *         Subscript[x, <i>&lt;index&gt;</i>] -> <i>&lt;{@link Variable#getName()}&gt;</i>
     *     }
     *
     *     changealgbxx = {
     *         <i>&lt;for each {@link AlgebraicVariable}&gt;</i>
     *         <i>&lt;{@link AlgebraicVariable#getName()}&gt;</i> -> Subscript[xx, <i>&lt;index&gt;</i>]
     *     }
     *
     *     changealgxxb  = {
     *         <i>&lt;for each {@link AlgebraicVariable}&gt;</i>
     *         Subscript[xx, <i>&lt;index&gt;</i>] -> <i>&lt;{@link AlgebraicVariable#getName()}&gt;</i>
     *     }
     * </pre>
     */
    void generateIndexedVariables();

    /**
     * Block 8:
     * <pre>
     *     defalgvarsubstitutexxx[jj_, t_, varxxx_] := defalgvarsubstitute[jj, t, var] /. changediffax /. changealgbxx;
     *     defuvarsubstitutexxx[j_, t_, varxxx_] := defuvarsubstitute[j, t, var] /. changediffax /. changealgbxx;
     *     defzvarsubstitutexxx[q_, t_, varxxx_] := defzvarsubstitute[q, t, var] /. changediffax /. changealgbxx;
     * </pre>
     */
    void generateSubstituteVariables();

    /**
     * Block 9:
     * <pre>
     *     <i>&lt;for each {@link Agent}&gt;</i>
     *     defuvarsubstitutexxx[<i>&lt;{@link Agent#getName()}&gt;</i>, t, varxxx]
     *
     *     <i>&lt;for each {@link Constraint}&gt;</i>
     *     defzvarsubstitutexxx[<i>&lt;{@link Constraint#getId()} ()}&gt;</i>, t, varxxx]
     * </pre>
     */
    void generateSubstituteFunctions();

    /**
     * Block 10:
     * <pre>
     *     (* Verhaltensgleichungen für allgemeines GCD Modell mit indizierten Variablen x *)
     *     dgldiffxxx = Table[Subscript[x, i]'[t] ==
     *      Sum[&mu;[j, i] If[Evaluate[\!\(
     *      \*SubscriptBox[\(&part;\), \(\(
     *      \*SubscriptBox[\(x\), \(i\)]'\)[t]\)]\(defuvarsubstitutexxx[j, t, varxxx]\)\)] != 0, Evaluate[\!\(
     *      \*SubscriptBox[\(&part;\), \(\(\*SubscriptBox[\(x\), \(i\)]'\)[t]\)]\(defuvarsubstitutexxx[j, t, varxxx]\)\)], Evaluate[\!\(
     *      \(\*SubscriptBox[\(x\), \(i\)]\)[t]\)]\(defuvarsubstitutexxx[j, t, varxxx]\)\)]], {j, AG}] + Sum[Subscript[&lambda;, q][t] If[Evaluate[\!\(
     *      \*SubscriptBox[\(&part;\), \(\(\*SubscriptBox[\(x\), \(i\)]'\)[t]\)]\(defzvarsubstitutexxx[q, t, varxxx]\)\)] != 0, Evaluate[\!\(
     *      \*SubscriptBox[\(&part;\), \(\(\*SubscriptBox[\(x\), \(i\)]'\)[t]\)]\(defzvarsubstitutexxx[q, t, varxxx]\)\)], Evaluate[\!\(
     *      \*SubscriptBox[\(&part;\), \(\(\*SubscriptBox[\(x\), \(i\)]\)[t]\)]\(defzvarsubstitutexxx[q, t, varxxx]\)\)]], {q, nZwangB}], {i, ndiffvar}];
     *
     *      dglalgxxx =
     *        Table[Subscript[xx, ii][t] ==
     *          defalgvarsubstitutexxx[ii, t, varxxx], {ii, nalgvar}];
     *
     *      (* Zwangsbedingungen für allgemeines GCD Modell mit indizierten Variablen x *)
     *      dglzxxx = Table[0 == defzvarsubstitutexxx[q, t, varxxx], {q, nZwangB}];
     *      dglxxx = Join[dglalgxxx, dgldiffxxx, dglzxxx];
     *
     *      (* Rücktransformation des Modells mit indizierten Variablen in Modell mit benannten Variablen*)
     *      dgl = dglxxx /. changediffxa /. changealgxxb;
     * </pre>
     *
     * <pre>
     *     (* Verhaltensgleichungen für allgemeines GCD Modell mit indizierten Variablen x *)
     *     dgldiffxxx = Table[Subscript[x, i]'[t] ==
     * </pre>
     */
    void generateBehavioralEquation();

    /**
     * Block 11:
     * <pre>
     *     (* Definition der Anfangsbedingungen *)
     *     init = {
     *         <i>&lt;{@link Variable#getName()}&gt;</i>[0] == <i>&lt;{@link Variable#getInitialCondition()}&gt;</i>
     *     }
     * </pre>
     */
    void generateInitialConditions();

    /**
     * Block 12:
     * <pre>
     *     (* Zum Berechnen und Plotten der Nutzenfunktionen *)
     *     ugl = {
     *         <i>&lt;for each {@link Agent}&gt;</i>
     *         u<i>&lt;lowerCase({@link Agent#getName()})&gt;</i>[t] == defuvar[<i>&lt;{@link Agent#getName()}&gt;</i>, t, var]
     *     }
     *
     *     (* Führe alle Gleichungen für NDSolve zusammen *)
     *     gl = Join[ugl, dgl, init];
     *
     *     (* Differentialgleichungsvariable incl. Lagrangefaktoren und Nutzenfunktionen *)
     *     glvar = Join[{
     *         <i>&lt;for each {@link Agent}&gt;</i>
     *         u<i>&lt;lowerCase({@link Agent#getName()})&gt;</i>
     *     }, var, &lambda;F]
     *
     *     (* Transformiere die indizierten Machtfaktoren in den Gleichungen gl in nichtindizierte Machtfaktoren *)
     *     GL = gl /. change&mu;
     *
     *     (* Das berechnete Ergebnis von GL muss manuell in das Programm zur
     *      *     Lösung der Modellgleichungen übertragen werden (es reicht nicht indem
     *      *     man nur "GL" in NDSolve schreibt, das kann Manipulate nicht
     *      *     verarbeiten). Dann das Lösungsprogramm gesondert gestartet werden *)
     * </pre>
     */
    void generateSystemOfEquation();

    /**
     * <pre>
     *     Manipulate[Column[{Module[{sol = Quiet@NDSolve[{
     *         &lt;Output of GL&gt;
     *     }, glvar, {t, 0, 50}, Method -> {"IndexReduction" -> {True, "ConstraintMethod" -> "Projection"}}]},
     *     {&lt;List of Plots&gt;}]}],
     *     &lt;List of variable initialization&gt;,
     *     &lt;List of changeMu initialization&gt;,
     *     &lt;List of parameter initialization&gt;,
     *     {{tmax, 30}, 0, 100}, {{plotmax, 2.5}, 0, 20}]
     * </pre>
     */
    void generateManipulate();

    /**
     * <pre>
     *     Plot[Evaluate[{&lt;List of plot variables&gt;} /. sol],
     *          {t, 0, tmax},
     *          PlotRange -> {-plotmax, plotmax},
     *          PlotStyle -> &lt;plot style name&gt;,
     *          PlotLegends -> LineLegend[{&lt;legend name list&gt;},
     *          LegendLabel -> "&lt;plot name&gt;"]
     *     ]
     * </pre>
     */
    void generatePlot(String plotName, String plotStyleName, List<String> plotVariables, List<String> legendNames);
}