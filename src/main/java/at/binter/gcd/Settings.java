package at.binter.gcd;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.LinkedList;

import static at.binter.gcd.GCDApplication.app;
import static at.binter.gcd.util.GuiUtils.sanitizeString;
import static at.binter.gcd.util.GuiUtils.showErrorMessage;

@XmlRootElement(name = "gcd-settings")
public class Settings {
    @XmlElement(name = "jLink")
    public String jLink;
    @XmlElement(name = "mathKernel")
    public String mathKernel;
    @XmlElement
    public String defaultFolder;
    @XmlElementWrapper(name = "recentlyOpenedFiles")
    @XmlElement(name = "file")
    public final LinkedList<String> recentlyOpened = new LinkedList<>();
    @XmlElement
    public String lastOpened;
    @XmlElementWrapper(name = "ndSolveMethods")
    @XmlElement(name = "method")
    public final ObservableList<String> ndSolveMethods = FXCollections.observableArrayList();

    public Settings() {
        if (ndSolveMethods.isEmpty()) {
            loadDefaultNdSolveMethods();
        }
    }

    public void loadDefaultValues() {
        jLink = "C:/Program Files/Wolfram Research/Mathematica/12.1/SystemFiles/Links/JLink";
        mathKernel = "C:/Program Files/Wolfram Research/Mathematica/12.1/MathKernel.exe";
    }

    public void loadDefaultNdSolveMethods() {
        ndSolveMethods.clear();
        addNdSolveMethod("Automatic");
        addNdSolveMethod("{IndexReduction -> {True, ConstraintMethod -> Projection}}");
        addNdSolveMethod("{\"EquationSimplification\" -> \"Residual\"}");
    }

    public void addRecentlyOpened(File f) {
        if (!f.exists()) {
            return;
        }
        if (recentlyOpened.size() >= 7) {
            recentlyOpened.removeFirst();
        }
        String path = f.getAbsolutePath();
        recentlyOpened.remove(path);
        recentlyOpened.addFirst(path);
    }

    public boolean verifyMathematicaPaths(boolean showAlert) {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(getJLinkErrors(jLink));
        StringBuilder mathKernelErrors = getMathKernelErrors(mathKernel);
        if (!mathKernelErrors.isEmpty()) {
            if (!errorMessage.isEmpty()) {
                errorMessage.append("\n");
            }
            errorMessage.append(mathKernelErrors);
        }

        if (!errorMessage.isEmpty() && showAlert) {
            showErrorMessage(app.getString("error.mathematica.config.title"), errorMessage.toString());
        }
        return errorMessage.isEmpty();
    }

    public static boolean isValidJLinkPath(String text) {
        return getJLinkErrors(text).isEmpty();
    }

    public static StringBuilder getJLinkErrors(String text) {
        text = sanitizeString(text);
        StringBuilder errorMessage = new StringBuilder();
        if (isValidPath(text)) {
            File f = new File(text);
            if (!f.isDirectory()) {
                errorMessage.append(app.getString("error.mathematica.jlink.message", f.getName()));
            }
        } else {
            if (StringUtils.isBlank(text)) {
                errorMessage.append(app.getString("error.mathematica.jlink.missing.message"));
            } else {
                errorMessage.append(app.getString("error.mathematica.jlink.exists.message", text));
            }
        }
        return errorMessage;
    }

    public static boolean isValidMathKernelPath(String text) {
        return getMathKernelErrors(text).isEmpty();
    }

    public static StringBuilder getMathKernelErrors(String path) {
        path = sanitizeString(path);
        StringBuilder errorMessage = new StringBuilder();
        if (isValidPath(path)) {
            File f = new File(path);
            if (!"MathKernel.exe".equals(f.getName())) {
                errorMessage.append(app.getString("error.mathematica.math.kernel.file.invalid", f.getName()));
            }
        } else {
            if (StringUtils.isBlank(path)) {
                errorMessage.append(app.getString("error.mathematica.math.kernel.missing.message"));
            } else {
                errorMessage.append(app.getString("error.mathematica.math.kernel.exists.message", path));
            }
        }
        return errorMessage;
    }

    public static boolean isValidPath(String path) {
        if (StringUtils.isBlank(path)) return false;
        return new File(path).exists();
    }

    public ObservableList<String> getNdSolveMethods() {
        return ndSolveMethods;
    }

    public void addNdSolveMethod(String method) {
        method = sanitizeString(method);
        if (method == null || ndSolveMethods.contains(method)) {
            return;
        }
        ndSolveMethods.add(method);
    }

    public void removeNdSolveMethod(String method) {
        method = sanitizeString(method);
        ndSolveMethods.remove(method);
    }
}