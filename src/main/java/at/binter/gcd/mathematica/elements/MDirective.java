package at.binter.gcd.mathematica.elements;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.model.HasPlotStyle;
import org.apache.commons.lang3.StringUtils;

public class MDirective extends MBase implements IExpression {
    private HasPlotStyle plotStyle;
    private String comment;
    private String name;

    public MDirective() {

    }

    public MDirective(HasPlotStyle plotStyle, String comment) {
        this.plotStyle = plotStyle;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HasPlotStyle getPlotStyle() {
        return plotStyle;
    }

    public void setPlotStyle(HasPlotStyle plotStyle) {
        this.plotStyle = plotStyle;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        toHTML(builder, false);
    }

    public HTMLBuilder toHTML(HTMLBuilder builder, boolean comma) {
        builder.write("Directive[");
        if (StringUtils.isBlank(plotStyle.getPlotColor())) {
            builder.write("defaultColor");
        } else {
            builder.write(plotStyle.getPlotColor());
        }
        builder.write(", ");
        if (plotStyle.getPlotThickness() == null) {
            builder.write("defaultThickness");
        } else {
            builder.write("AbsoluteThickness[" + plotStyle.getPlotThickness() + "]");
        }
        if (StringUtils.isNotBlank(plotStyle.getPlotLineStyle())) {
            builder.write(", ");
            builder.write(plotStyle.getPlotLineStyle());
        }

        builder.write("]");
        if (comma) {
            builder.write(",");
        }
        builder.write(" ");
        builder.comment(comment);
        return builder;
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
        String color = "defaultColor";
        if (StringUtils.isNotBlank(plotStyle.getPlotColor())) {
            color = plotStyle.getPlotColor();
        }
        String thickness = "defaultThickness";
        if (plotStyle.getPlotThickness() != null) {
            thickness = "AbsoluteThickness[" + plotStyle.getPlotThickness() + "]";
        }
        if (StringUtils.isNotBlank(plotStyle.getPlotLineStyle())) {
            return new MParameter("Directive", color, thickness, plotStyle.getPlotLineStyle()).getMathematicaExpression();
        }
        return new MParameter("Directive", color, thickness).getMathematicaExpression();
    }
}