package at.binter.gcd.mathematica.elements;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MComment;
import at.binter.gcd.mathematica.syntax.MExpression;
import at.binter.gcd.mathematica.syntax.RowBox;
import at.binter.gcd.mathematica.syntax.binary.MSetDelayed;
import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.PlotStyle;
import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.model.elements.Variable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static at.binter.gcd.mathematica.syntax.function.MFunction.linebreak;

public class MPlotStyle extends MBase implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MPlotStyle.class);

    public static final PlotStyle defaultPlotStyle = new PlotStyle("Black", 1.0, null);

    private String name;
    private final List<MDirective> directives = new ArrayList<>();

    public MPlotStyle() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addDirective(MDirective directive) {
        directives.add(directive);
    }

    public MPlotStyle(GCDModel model) {
        Iterator<AlgebraicVariable> it = model.getAlgebraicVariables().sorted().iterator();
        boolean hasMoreAlgVars, hasMoreVars;
        while (it.hasNext()) {
            AlgebraicVariable algVar = it.next();
            directives.add(new MDirective(algVar, algVar.getName()));
            Iterator<String> varIt = algVar.getVariables().stream().sorted().iterator();
            while (varIt.hasNext()) {
                String name = varIt.next();
                Variable variable = model.getVariable(name);
                if (variable == null) {
                    //model.getAlgebraicVariable(name) == null;
                    log.error("Could not find variable \"{}\" for algebraic variable \"{}\" - skipping in plot", name, algVar.getName());
                    continue;
                }
                directives.add(new MDirective(variable, variable.getName()));

            }
        }
    }

    public MPlotStyle(GCDModel model, Agent agent) {
        if (StringUtils.isBlank(agent.getName())) {
            log.error("Agent \"{}\" has no name", agent);
        }
        name = "PLOTSTYLEu" + agent.getName();
        directives.add(new MDirective(agent, "Agent " + agent.getName()));
        model.getAlgebraicVariables().sorted().forEach(algebraicVariable -> {
            if (agent.getVariables().contains(algebraicVariable.getName())) {
                directives.add(new MDirective(algebraicVariable, algebraicVariable.getName()));
            }
        });
        agent.getVariables().stream().sorted().forEach(name -> {
            if (model.hasAlgebraicVariable(name)) {
                return;
            }
            Variable variable = model.getVariable(name);
            if (variable == null) {
                log.error("Could not find variable \"{}\" for agent \"{}\" - skipping in plot", name, agent.getName());
                return;
            }
            directives.add(new MDirective(variable, variable.getName()));
        });
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        builder.write(name);
        builder.writeln(":= {");
        builder.increaseIndent(4);
        Iterator<MDirective> it = directives.iterator();
        while (it.hasNext()) {
            it.next().toHTML(builder, it.hasNext());
        }
        builder.decreaseIndent(4);
        builder.writeln("};");
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        return toHTML();
    }

    @Override
    public String getMathematicaExpression() {
        RowBox inner = new RowBox();
        inner.add(linebreak);

        Iterator<MDirective> it = directives.iterator();
        while (it.hasNext()) {
            MDirective element = it.next();
            inner.add(element);
            if (it.hasNext()) {
                inner.add(new MExpression("\",\""));
            }
            MComment comment = new MComment(element.getComment());
            inner.add(comment);
            inner.add(linebreak);
        }

        RowBox outer = new RowBox();
        outer.add(new MExpression("\"{\""));
        outer.add(inner);
        outer.add(new MExpression("\"}\""));

        MSetDelayed set = new MSetDelayed(new MVariable(name), outer, true);
        set.setDoLinebreakAfter(true);
        return set.getMathematicaExpression();
    }
}