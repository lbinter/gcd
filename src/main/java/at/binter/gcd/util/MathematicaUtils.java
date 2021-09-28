package at.binter.gcd.util;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathematicaUtils {
    public static final String linebreakString = "\"\\[IndentingNewLine]\"\r\n";
    private static final Logger log = LoggerFactory.getLogger(MathematicaUtils.class);
    private KernelLink ml;

    private int pageWidth = 125;
    private boolean linkOpen = false;

    static {
        String jLinkDir = "C:/Program Files/Wolfram Research/Mathematica/12.1/SystemFiles/Links/JLink";
        System.setProperty("com.wolfram.jlink.libdir", jLinkDir);
    }

    public MathematicaUtils() {
    }

    public void openLink() {
        try {
            ml = MathLinkFactory.createKernelLink("-linkmode launch -linkname 'C:/Program Files/Wolfram Research/Mathematica/12.1/MathKernel.exe'");
            ml.discardAnswer();
            linkOpen = true;
        } catch (MathLinkException e) {
            throw new RuntimeException("Could not create MathLink", e);
        }
    }

    public void closeLink() {
        ml.close();
        linkOpen = false;
    }

    public KernelLink getMl() {
        return ml;
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
    }

    public String transformToFullForm(String mathExpr, boolean addNewLine) {
        if (!linkOpen) {
            openLink();
        }
        String formatted;
        String expr = "FullForm[ToBoxes[Defer[" + mathExpr + "]]]";
        if (log.isTraceEnabled()) {
            log.trace("Input:  {}", expr);
        }
        formatted = ml.evaluateToOutputForm(expr, 0);
        if (log.isTraceEnabled()) {
            log.trace("Output: {}", formatted);
        }
        if (addNewLine) {
            formatted = formatted + ", \"\\[IndentingNewLine]\"\r\n";
        }
        return formatted;
    }
}