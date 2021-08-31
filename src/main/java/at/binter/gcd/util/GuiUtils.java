package at.binter.gcd.util;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class GuiUtils {

    public static void addStageCloseOnEscapeKey(Stage stage, Scene scene) {
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                stage.close();
            }
        });
    }

    public static String sanitizeString(String str) {
        if (StringUtils.isBlank(str)) return "";
        return str.trim().replaceAll(" +", " ");
    }

    public static Double readDoubleValueFrom(TextField textField) {
        String text = textField.getText().trim();
        if (StringUtils.isBlank(text)) {
            return null;
        } else {
            try {
                return Double.valueOf(text);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    public static String doubleToString(Double d) {
        if (d == null) {
            return "";
        }
        return d.toString();
    }

    public static final Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
    public static final UnaryOperator<TextFormatter.Change> filter = c -> {
        String text = c.getControlNewText();
        if (validEditingState.matcher(text).matches()) {
            return c;
        } else {
            return null;
        }
    };
    public static final StringConverter<Double> converter = new StringConverter<>() {
        @Override
        public Double fromString(String s) {
            if (s == null || s.isEmpty()) {
                return null;
            }
            if ("-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                return 0.0;
            } else {
                return Double.valueOf(s);
            }
        }

        @Override
        public String toString(Double d) {
            return doubleToString(d);
        }
    };

    public static TextFormatter<Double> createDoubleTextFormatter() {
        return new TextFormatter<>(converter, null, filter);
    }
}
