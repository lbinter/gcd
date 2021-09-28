package at.binter.gcd.util;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathematicaUtils {
    public static final String linebreak = ",\"\\[IndentingNewLine]\"\r\n";
    private static final Logger log = LoggerFactory.getLogger(MathematicaUtils.class);
    private static KernelLink ml;

    private static int pageWidth = 125;

    static {
        String jLinkDir = "C:/Program Files/Wolfram Research/Mathematica/12.1/SystemFiles/Links/JLink";
        System.setProperty("com.wolfram.jlink.libdir", jLinkDir);
    }

    public static KernelLink getMl() {
        return ml;
    }

    public static int getPageWidth() {
        return pageWidth;
    }

    public static void setPageWidth(int pageWidth) {
        MathematicaUtils.pageWidth = pageWidth;
    }

    public static String transformToFullForm(String mathExpr, boolean addNewLine) {
        try {
            ml = MathLinkFactory.createKernelLink("-linkmode launch -linkname 'C:/Program Files/Wolfram Research/Mathematica/12.1/MathKernel.exe'");
            ml.discardAnswer();
            String formatted;
            String expr = "FullForm[ToBoxes[Defer[" + mathExpr + "]]]";
            if (log.isTraceEnabled()) {
                log.trace("Input:  {}", expr);
            }
            formatted = ml.evaluateToOutputForm(expr, 0);
            if (log.isTraceEnabled()) {
                log.trace("Output: {}", formatted);
            }
            ml.newPacket();
            ml.close();
            if (addNewLine) {
                formatted = formatted + ", \"\\[IndentingNewLine]\"";
            }
            return formatted;
        } catch (MathLinkException e) {
            throw new RuntimeException("Could not create MathLink", e);
        }
    }
}