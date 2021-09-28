package at.binter.gcd.mathematica.syntax;

import at.binter.gcd.mathematica.HTMLBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MComment extends MExpression {
    private static final IExpression space = new MExpression("\" \"");
    private static final IExpression delimiter = new MExpression(",");
    private static final IExpression openComment = new MExpression("\"(*\"");
    private static final IExpression closeComment = new MExpression("\"*)\"");
    private static final IExpression linebreak = new MExpression("\"\\[IndentingNewLine]\"");

    private final List<IExpression> lines = new ArrayList<>();

    public MComment(String comment) {
        super("comment", comment);
    }

    public MComment(String cssClass, String comment) {
        super(cssClass, comment);
    }

    public MComment(String... comments) {
        super(comments[0]);
        for (int i = 1; i < comments.length; i++) {
            addLine(comments[i]);
        }
    }

    public List<IExpression> getLines() {
        return lines;
    }

    public void addLine(String line) {
        if (StringUtils.isBlank(line)) {
            return;
        }
        lines.add(new MExpression("comment", line));
    }

    public void addLine(IExpression line) {
        if (line == null || StringUtils.isBlank(line.getExpression())) return;
        lines.add(line);
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        builder.openSpan("comment");
        builder.write("(* ");
        super.toHTML(builder);
        Iterator<IExpression> it = lines.iterator();
        while (it.hasNext()) {
            it.next().toHTML(builder);
            if (it.hasNext()) {
                builder.linebreak();
            }
        }
        builder.write(" *)");
        builder.closeSpan();
    }

    @Override
    public String getMathematicaExpression() {
        List<String[]> commentLines = new ArrayList<>();

        RowBox inner = new RowBox();

        commentLines.add(super.getMathematicaExpression().split(" "));
        for (IExpression line : lines) {
            commentLines.add(line.getMathematicaExpression().split(" "));
        }

        Iterator<String[]> it = commentLines.iterator();
        while (it.hasNext()) {
            String[] split = it.next();
            for (int i = 0; i < split.length; i++) {
                inner.addExpressions(new MExpression("\"" + split[i] + "\""));
                if (i + 1 < split.length) {
                    inner.addExpressions(space);
                }
            }
            if (it.hasNext()) {
                inner.addExpressions(linebreak);
            }
        }

        RowBox outer = new RowBox();
        outer.addExpressions(openComment, inner, closeComment);
        return outer.getMathematicaExpression();
    }
}