package at.binter.gcd.util;

import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class MathematicaUtils {
    public static final String linebreakString = "\"\\[IndentingNewLine]\"\r\n";
    private static final Logger log = LoggerFactory.getLogger(MathematicaUtils.class);
    public static final String jLinkLibDirProperty = "com.wolfram.jlink.libdir";
    private static boolean jLinkDirSet = false;
    private static String jLinkDirPath;
    private String mathKernelPath;
    private KernelLink ml;

    private int pageWidth = 125;
    private boolean linkOpen = false;

    public MathematicaUtils() {
        setMathKernelPath("C:/Program Files/Wolfram Research/Mathematica/12.1/MathKernel.exe");
    }

    public MathematicaUtils(String mathKernelPath) {
        setMathKernelPath(mathKernelPath);
    }

    private void setMathKernelPath(String mathKernelPath) {
        this.mathKernelPath = mathKernelPath;
        File mathKernel = new File(mathKernelPath);
        if (!mathKernel.exists()) {
            RuntimeException e = new RuntimeException("Math kernel path does not exist " + jLinkDirPath);
            log.error("Could not set math kernel path", e);
            throw e;
        }
        log.info("using math kernel: {}", mathKernelPath);
    }

    public static synchronized void setJLinkDir(String jLinkDirPathNew) {
        if (jLinkDirSet) {
            return;
        }
        jLinkDirPath = jLinkDirPathNew;
        File jLink = new File(jLinkDirPath);
        if (!jLink.exists()) {
            RuntimeException e = new RuntimeException("JLink path does not exist " + jLinkDirPath);
            log.error("Could not set jLinkDir", e);
            throw e;
        }
        System.setProperty(jLinkLibDirProperty, jLinkDirPath);
        log.info("setting system property \"{}\" - \"{}\"", jLinkLibDirProperty, jLinkDirPath);
        jLinkDirSet = true;
    }

    public static File defaultMathematicaInstallationPath() {
        return new File("C:/Program Files/Wolfram Research/Mathematica/");
    }

    public synchronized void openLink() {
        try {
            ml = MathLinkFactory.createKernelLink("-linkmode launch -linkname '" + mathKernelPath + "'");
            ml.discardAnswer();
            linkOpen = true;
            log.info("Opened link to Mathematica Kernel");
        } catch (MathLinkException e) {
            throw new RuntimeException("Could not create MathLink", e);
        }
    }

    public synchronized void closeLink() {
        ml.close();
        linkOpen = false;
        log.info("Closed link to Mathematica Kernel");
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