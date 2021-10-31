package at.binter.gcd.util;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static at.binter.gcd.GCDApplication.app;

public class GuiUtils {
    public static final Color defaultBackground = Color.pink;
    public static final Color defaultForeground = Color.BLACK;

    public static void addStageCloseOnEscapeKey(Stage stage, Scene scene) {
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                stage.close();
            }
        });
    }

    public static String sanitizeString(String str) {
        if (str == null) return null;
        String sanitized = str.trim().replaceAll(" +", " ");
        if (StringUtils.isBlank(sanitized)) return null;
        return sanitized;
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

    public static void showInvalidFileError(File file) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(app.getString("error.invalid.file.title"));
        alert.setHeaderText(app.getString("error.invalid.file.title"));
        alert.setContentText(file.getAbsolutePath() + ": " + app.getString("error.invalid.file.message"));
        alert.showAndWait();
    }

    public static Optional<ButtonType> saveOverwriteQuestion(File file) {
        String title = app.getString("dialog.save.overwrite.question.title");
        String message = app.getString("dialog.save.overwrite.question.message", file.getAbsolutePath());
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText(app.getString("button.yes"));
        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText(app.getString("button.no"));
        return alert.showAndWait();
    }

    public static String getColorAsHtml(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }
}
