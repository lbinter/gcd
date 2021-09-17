package at.binter.gcd.mathematica.elements;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.HTMLWrapper;
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

public class MPlotStyle implements HTMLWrapper {
    private static final Logger log = LoggerFactory.getLogger(MPlotStyle.class);

    public static final PlotStyle defaultPlotStyle = new PlotStyle("Black", 1.0, null);

    private String name;
    private final List<MDirective> directives = new ArrayList<>();

    public MPlotStyle() {
    }

    public MPlotStyle(GCDModel model) {
        name = "PLOTSTYLEalgVar";
        model.getAlgebraicVariables().sorted().forEach(algebraicVariable -> {

        });
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
    public String toHTML() {
        HTMLBuilder builder = new HTMLBuilder();
        toHTML(builder);
        return builder.toString();
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        builder.write(name);
        builder.writeLine(":= {");
        builder.increaseIndent(4);
        Iterator<MDirective> it = directives.iterator();
        while (it.hasNext()) {
            it.next().toHTML(builder, it.hasNext());
        }
        builder.decreaseIndent(4);
        builder.writeLine("};");
    }
}