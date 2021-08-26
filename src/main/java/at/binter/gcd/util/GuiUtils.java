package at.binter.gcd.util;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GuiUtils {

    public static void addStageCloseOnEscapeKey(Stage stage, Scene scene) {
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                stage.close();
            }
        });
    }
}
