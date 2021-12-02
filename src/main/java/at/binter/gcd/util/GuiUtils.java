package at.binter.gcd.util;

import javafx.css.PseudoClass;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    public static final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
    public static final PseudoClass plotDefaultClass = PseudoClass.getPseudoClass("plot-default");
    public static final PseudoClass plotDiffClass = PseudoClass.getPseudoClass("plot-diff");

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
        return readDoubleValueFrom(text);
    }

    public static Double readDoubleValueFrom(String text) {
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
    public static final StringConverter<Double> DOUBLE_STRING_CONVERTER = new StringConverter<>() {
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

    public static final StringConverter<String> STRING_CONVERTER = new StringConverter<>() {
        @Override
        public String toString(String string) {
            return sanitizeString(string);
        }

        @Override
        public String fromString(String string) {
            return sanitizeString(string);
        }
    };

    public static TextFormatter<Double> createDoubleTextFormatter() {
        return new TextFormatter<>(DOUBLE_STRING_CONVERTER, null, filter);
    }

    public static TextFormatter<String> createStringTextFormatter() {
        return new TextFormatter<>(STRING_CONVERTER, null, null);
    }

    public static void showWarning(String title, String message, Object... params) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(app.getString(title));
        alert.setHeaderText(app.getString(title));
        if (params != null && params.length > 0) {
            alert.setContentText(app.getString(message, params));
        } else {
            alert.setContentText(app.getString(message));
        }
        alert.showAndWait();
    }

    public static void showInvalidFileError(File file) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(app.getString("error.invalid.file.title"));
        alert.setHeaderText(app.getString("error.invalid.file.title"));
        alert.setContentText(app.getString("error.invalid.file.message", file.getAbsolutePath()));
        alert.showAndWait();
    }

    public static void showMissingFileError(File file) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(app.getString("error.missing.file.title"));
        alert.setHeaderText(app.getString("error.missing.file.title"));
        alert.setContentText(app.getString("error.missing.file.message", file.getAbsolutePath()));
        alert.showAndWait();
    }

    public static void showError(String i18nTitle, String i18nMessage, Object... params) {
        String messageText;
        if (params != null && params.length > 0) {
            messageText = app.getString(i18nMessage, params);
        } else {
            messageText = app.getString(i18nMessage);
        }
        showErrorMessage(app.getString(i18nTitle), messageText);
    }

    public static void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        if (message.contains("\n")) {
            String[] messageArray = message.split("\n");
            VBox box = new VBox();
            for (String text : messageArray) {
                box.getChildren().add(new Text(text));
            }
            alert.getDialogPane().setContent(box);
        } else {
            alert.setContentText(message);
        }
        alert.showAndWait();
    }

    public static Optional<ButtonType> showYesNoDialog(String title, String message, String yes, String no) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText(yes);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText(no);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showYesNoDialog(String title, String message) {
        return showYesNoDialog(title, message, app.getString("button.yes"), app.getString("button.no"));
    }

    public static String getColorAsHtml(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static void addValuesValidationListener(TextField editorValueStart, TextField editorValueMinimum, TextField editorValueMaximum) {
        editorValueStart.textProperty().addListener((observable, oldValue, newValue) -> {
            editorValueStart.pseudoClassStateChanged(errorClass, !isValueStartValid(newValue, editorValueMinimum, editorValueMaximum));
        });
        editorValueMinimum.textProperty().addListener((observable, oldValue, newValue) -> {
            editorValueMinimum.pseudoClassStateChanged(errorClass, !isValueMinValid(newValue, editorValueStart, editorValueMaximum));
        });
        editorValueMaximum.textProperty().addListener((observable, oldValue, newValue) -> {
            editorValueMaximum.pseudoClassStateChanged(errorClass, !isValueMaxValid(newValue, editorValueStart, editorValueMinimum));
        });
    }

    public static boolean isValueStartValid(String newValue, TextField editorValueMinimum, TextField editorValueMaximum) {
        Double start = readDoubleValueFrom(newValue);
        if (start == null) {
            return false;
        }
        Double min = readDoubleValueFrom(editorValueMinimum);
        Double max = readDoubleValueFrom(editorValueMaximum);
        if (min != null && min > start) {
            return false;
        }
        if (max != null && max < start) {
            return false;
        }
        return true;
    }

    public static boolean isValueMinValid(String newValue, TextField editorValueStart, TextField editorValueMaximum) {
        Double min = readDoubleValueFrom(newValue);
        if (min == null) {
            return false;
        }
        Double start = readDoubleValueFrom(editorValueStart);
        Double max = readDoubleValueFrom(editorValueMaximum);
        if (start != null && min > start) {
            return false;
        }
        if (max != null && max < min) {
            return false;
        }
        return true;
    }

    public static boolean isValueMaxValid(String newValue, TextField editorValueStart, TextField editorValueMinimum) {
        Double max = readDoubleValueFrom(newValue);
        if (max == null) {
            return false;
        }
        Double start = readDoubleValueFrom(editorValueStart);
        Double min = readDoubleValueFrom(editorValueMinimum);
        if (start != null && max < start) {
            return false;
        }
        if (min != null && max < min) {
            return false;
        }
        return true;
    }


}
