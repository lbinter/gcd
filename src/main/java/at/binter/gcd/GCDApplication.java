package at.binter.gcd;


import at.binter.gcd.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * GCD Application
 *
 * @author Lucas Binter
 */
public class GCDApplication extends Application {
    private static final Logger log = LoggerFactory.getLogger(GCDApplication.class);
    public static GCDApplication app;

    public final ResourceBundle resources = ResourceBundle.getBundle("gcd");

    public String gcdCss;

    public FXMLLoader loaderGCD;
    public FXMLLoader loaderAlgVarEditor;
    public FXMLLoader loaderAgentEditor;
    public FXMLLoader loaderConstraintEditor;
    public FXMLLoader loaderVariableEditor;
    public FXMLLoader loaderParameterEditor;
    public FXMLLoader loaderChangeMuEditor;
    public FXMLLoader loaderHelp;

    public Stage primaryStage;
    public Scene primaryScene;

    public Scene algebraicVariableEditorScene;
    public Scene agentEditorScene;
    public Scene constraintEditorScene;
    public Scene variableEditorScene;
    public Scene parameterEditorScene;
    public Scene changeMuEditorScene;
    public Scene helpScene;

    public GCDController gcdController;
    public AlgebraicVariableEditorController algebraicVariableEditorController;
    public AgentEditorController agentEditorController;
    public ConstraintEditorController constraintEditorController;
    public VariableEditorController variableEditorController;
    public ParameterEditorController parameterEditorController;
    public ChangeMuEditorController changeMuEditorController;
    public HelpController helpController;

    public GCDApplication() {
        if (app == null) {
            app = this;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        gcdCss = Objects.requireNonNull(getClass().getResource("gcd.css")).toExternalForm();

        loaderGCD = new FXMLLoader();
        loaderGCD.setLocation(getClass().getResource("gcd.fxml"));
        loaderGCD.setResources(resources);

        primaryScene = new Scene(loaderGCD.load());
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle(resources.getString("main.title"));

        loaderHelp = new FXMLLoader();
        loaderHelp.setLocation(getClass().getResource("help.fxml"));
        loaderHelp.setResources(resources);
        helpScene = new Scene(loaderHelp.load());
        helpScene.getStylesheets().add(gcdCss);
        helpController = loaderHelp.getController();
        helpController.setApplication(this);

        loaderAlgVarEditor = new FXMLLoader();
        loaderAlgVarEditor.setLocation(getClass().getResource("editor/AlgebraicVariableEditor.fxml"));
        loaderAlgVarEditor.setResources(resources);
        algebraicVariableEditorScene = new Scene(loaderAlgVarEditor.load());
        algebraicVariableEditorScene.getStylesheets().add(gcdCss);
        algebraicVariableEditorController = loaderAlgVarEditor.getController();
        algebraicVariableEditorController.setApplication(this);
        algebraicVariableEditorController.setScene(algebraicVariableEditorScene);

        loaderAgentEditor = new FXMLLoader();
        loaderAgentEditor.setLocation(getClass().getResource("editor/AgentEditor.fxml"));
        loaderAgentEditor.setResources(resources);
        agentEditorScene = new Scene(loaderAgentEditor.load());
        agentEditorScene.getStylesheets().add(gcdCss);
        agentEditorController = loaderAgentEditor.getController();
        agentEditorController.setApplication(this);
        agentEditorController.setScene(agentEditorScene);

        loaderConstraintEditor = new FXMLLoader();
        loaderConstraintEditor.setLocation(getClass().getResource("editor/ConstraintEditor.fxml"));
        loaderConstraintEditor.setResources(resources);
        constraintEditorScene = new Scene(loaderConstraintEditor.load());
        constraintEditorScene.getStylesheets().add(gcdCss);
        constraintEditorController = loaderConstraintEditor.getController();
        constraintEditorController.setApplication(this);
        constraintEditorController.setScene(constraintEditorScene);

        loaderVariableEditor = new FXMLLoader();
        loaderVariableEditor.setLocation(getClass().getResource("editor/VariableEditor.fxml"));
        loaderVariableEditor.setResources(resources);
        variableEditorScene = new Scene(loaderVariableEditor.load());
        variableEditorScene.getStylesheets().add(gcdCss);
        variableEditorController = loaderVariableEditor.getController();
        variableEditorController.setApplication(this);
        variableEditorController.setScene(variableEditorScene);

        loaderParameterEditor = new FXMLLoader();
        loaderParameterEditor.setLocation(getClass().getResource("editor/ParameterEditor.fxml"));
        loaderParameterEditor.setResources(resources);
        parameterEditorScene = new Scene(loaderParameterEditor.load());
        parameterEditorScene.getStylesheets().add(gcdCss);
        parameterEditorController = loaderParameterEditor.getController();
        parameterEditorController.setApplication(this);
        parameterEditorController.setScene(parameterEditorScene);

        loaderChangeMuEditor = new FXMLLoader();
        loaderChangeMuEditor.setLocation(getClass().getResource("editor/ChangeMuEditor.fxml"));
        loaderChangeMuEditor.setResources(resources);
        changeMuEditorScene = new Scene(loaderChangeMuEditor.load());
        changeMuEditorScene.getStylesheets().add(gcdCss);
        changeMuEditorController = loaderChangeMuEditor.getController();
        changeMuEditorController.setApplication(this);
        changeMuEditorController.setScene(changeMuEditorScene);

        gcdController = loaderGCD.getController();
        gcdController.setApplication(this);
        gcdController.initializeGCDDepended();

        primaryStage.show();
    }

    public String getString(String i18nKey) {
        try {
            return resources.getString(i18nKey);
        } catch (MissingResourceException e) {
            if (log.isWarnEnabled()) log.warn(e.getMessage());
            return '!' + i18nKey + '!';
        }
    }

    public String getString(String i18nKey, Object... args) {
        try {
            String pattern = resources.getString(i18nKey);
            return MessageFormat.format(pattern, args);
        } catch (MissingResourceException e) {
            if (log.isWarnEnabled()) log.warn(e.getMessage());
            return '!' + i18nKey + '!';
        }
    }
}