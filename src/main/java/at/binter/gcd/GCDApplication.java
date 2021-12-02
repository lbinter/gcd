package at.binter.gcd;


import at.binter.gcd.controller.*;
import at.binter.gcd.model.plotstyle.GCDPlotStyles;
import at.binter.gcd.util.MathematicaUtils;
import at.binter.gcd.xml.XmlReader;
import at.binter.gcd.xml.XmlWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public Path settingsLocation = Paths.get(System.getProperty("user.home"), ".gcd", "settings.xml");
    public Settings settings;
    public final Settings defaultSettings = new Settings();
    public Path plotStyleLocation = Paths.get(System.getProperty("user.home"), ".gcd", "plotStyles.xml");
    public GCDPlotStyles plotStyles;
    public Path defaultPlotStyleLocation;
    public GCDPlotStyles defaultPlotStyles;

    public String gcdCss;
    public String plotStyleTableCss;
    public String statusCss;
    public String mathematicaCss;
    public String errorViewCss;

    public FXMLLoader loaderSettings = new FXMLLoader();
    public FXMLLoader loaderGCD = new FXMLLoader();
    public FXMLLoader loaderPlotStyleEntry = new FXMLLoader();
    public FXMLLoader loaderAlgVarEditor = new FXMLLoader();
    public FXMLLoader loaderAgentEditor = new FXMLLoader();
    public FXMLLoader loaderConstraintEditor = new FXMLLoader();
    public FXMLLoader loaderVariableEditor = new FXMLLoader();
    public FXMLLoader loaderParameterEditor = new FXMLLoader();
    public FXMLLoader loaderChangeMuEditor = new FXMLLoader();
    public FXMLLoader loaderHelp = new FXMLLoader();
    public FXMLLoader loaderMathematica = new FXMLLoader();

    public Stage settingsStage = new Stage();
    public Scene settingsScene;

    public Stage primaryStage;
    public Scene primaryScene;

    public Scene plotStyleEntryScene;

    public Scene algebraicVariableEditorScene;
    public Scene agentEditorScene;
    public Scene constraintEditorScene;
    public Scene variableEditorScene;
    public Scene parameterEditorScene;
    public Scene changeMuEditorScene;
    public Scene helpScene;
    public Scene mathematicaScene;

    public SettingsController settingsController;

    public GCDController gcdController;
    public PlotStyleEntryEditorController plotStyleEntryEditorController;

    public AlgebraicVariableEditorController algebraicVariableEditorController;
    public AgentEditorController agentEditorController;
    public ConstraintEditorController constraintEditorController;
    public VariableEditorController variableEditorController;
    public ParameterEditorController parameterEditorController;
    public ChangeMuEditorController changeMuEditorController;
    public HelpController helpController;
    public MathematicaController mathematicaController;

    public MathematicaUtils utils;

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
        plotStyleTableCss = Objects.requireNonNull(getClass().getResource("plotStyleTable.css")).toExternalForm();
        statusCss = Objects.requireNonNull(getClass().getResource("status.css")).toExternalForm();
        mathematicaCss = Objects.requireNonNull(getClass().getResource("mathematica.css")).toExternalForm();
        errorViewCss = Objects.requireNonNull(getClass().getResource("errorView.css")).toExternalForm();

        defaultSettings.loadDefaultValues();

        readOrCreateSettings();
        defaultPlotStyleLocation = Paths.get(Objects.requireNonNull(getClass().getResource("defaultPlotStyles.xml")).toURI());
        defaultPlotStyles = XmlReader.readGCDPlotStyles(defaultPlotStyleLocation.toFile());
        if (!plotStyleLocation.toFile().exists()) {
            XmlWriter.write(defaultPlotStyles, plotStyleLocation.toFile());
        }
        plotStyles = XmlReader.readGCDPlotStyles(plotStyleLocation.toFile());

        loaderSettings.setLocation(getClass().getResource("settings.fxml"));
        loaderSettings.setResources(resources);
        settingsScene = new Scene(loaderSettings.load());
        settingsScene.getStylesheets().add(gcdCss);
        settingsController = loaderSettings.getController();
        settingsController.setApplication(this);
        settingsStage.setScene(settingsScene);
        settingsStage.setTitle(resources.getString("settings.title"));
        settingsController.loadSettings();
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.initOwner(primaryStage);

        if (!settings.verifyMathematicaPaths(true)) {
            settingsStage.showAndWait();
        }


        MathematicaUtils.setJLinkDir(settings.jLink);
        utils = new MathematicaUtils(settings.mathKernel);

        loaderGCD.setLocation(getClass().getResource("gcd.fxml"));
        loaderGCD.setResources(resources);

        primaryScene = new Scene(loaderGCD.load());
        primaryScene.getStylesheets().add(statusCss);
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle(resources.getString("main.title"));

        loaderPlotStyleEntry.setLocation(getClass().getResource("editor/PlotStyleEntry.fxml"));
        loaderPlotStyleEntry.setResources(resources);
        plotStyleEntryScene = new Scene(loaderPlotStyleEntry.load());
        plotStyleEntryScene.getStylesheets().add(gcdCss);
        plotStyleEntryEditorController = loaderPlotStyleEntry.getController();
        plotStyleEntryEditorController.setApplication(this);
        plotStyleEntryEditorController.setScene(plotStyleEntryScene);

        loaderHelp.setLocation(getClass().getResource("help.fxml"));
        loaderHelp.setResources(resources);
        helpScene = new Scene(loaderHelp.load());
        helpScene.getStylesheets().add(gcdCss);
        helpController = loaderHelp.getController();
        helpController.setApplication(this);

        loaderAlgVarEditor.setLocation(getClass().getResource("editor/AlgebraicVariableEditor.fxml"));
        loaderAlgVarEditor.setResources(resources);
        algebraicVariableEditorScene = new Scene(loaderAlgVarEditor.load());
        algebraicVariableEditorScene.getStylesheets().add(gcdCss);
        algebraicVariableEditorController = loaderAlgVarEditor.getController();
        algebraicVariableEditorController.setApplication(this);
        algebraicVariableEditorController.setScene(algebraicVariableEditorScene);
        algebraicVariableEditorController.initializeGCDDepended();

        loaderAgentEditor.setLocation(getClass().getResource("editor/AgentEditor.fxml"));
        loaderAgentEditor.setResources(resources);
        agentEditorScene = new Scene(loaderAgentEditor.load());
        agentEditorScene.getStylesheets().add(gcdCss);
        agentEditorController = loaderAgentEditor.getController();
        agentEditorController.setApplication(this);
        agentEditorController.setScene(agentEditorScene);
        agentEditorController.initializeGCDDepended();

        loaderConstraintEditor.setLocation(getClass().getResource("editor/ConstraintEditor.fxml"));
        loaderConstraintEditor.setResources(resources);
        constraintEditorScene = new Scene(loaderConstraintEditor.load());
        constraintEditorScene.getStylesheets().add(gcdCss);
        constraintEditorController = loaderConstraintEditor.getController();
        constraintEditorController.setApplication(this);
        constraintEditorController.setScene(constraintEditorScene);

        loaderVariableEditor.setLocation(getClass().getResource("editor/VariableEditor.fxml"));
        loaderVariableEditor.setResources(resources);
        variableEditorScene = new Scene(loaderVariableEditor.load());
        variableEditorScene.getStylesheets().add(gcdCss);
        variableEditorController = loaderVariableEditor.getController();
        variableEditorController.setApplication(this);
        variableEditorController.setScene(variableEditorScene);
        variableEditorController.initializeGCDDepended();

        loaderParameterEditor.setLocation(getClass().getResource("editor/ParameterEditor.fxml"));
        loaderParameterEditor.setResources(resources);
        parameterEditorScene = new Scene(loaderParameterEditor.load());
        parameterEditorScene.getStylesheets().add(gcdCss);
        parameterEditorController = loaderParameterEditor.getController();
        parameterEditorController.setApplication(this);
        parameterEditorController.setScene(parameterEditorScene);

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

        loaderMathematica.setLocation(getClass().getResource("mathematica.fxml"));
        loaderMathematica.setResources(resources);
        mathematicaScene = new Scene(loaderMathematica.load());
        mathematicaScene.getStylesheets().add(gcdCss);
        mathematicaController = loaderMathematica.getController();
        mathematicaController.setApplication(this);
        mathematicaController.initializeGCDDepended();

        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, windowEvent -> gcdController.showCloseDialog(windowEvent));
        primaryStage.show();
    }

    private void readOrCreateSettings() throws IOException {
        File settingsFile = settingsLocation.toFile();
        if (!settingsFile.exists()) {
            if (!settingsFile.getParentFile().exists()) {
                Files.createDirectory(settingsLocation.getParent());
                log.info("creating gcd settings folder {}", settingsLocation.getParent());
            }
            settings = new Settings();
            settings.loadDefaultValues();
            saveSettings();
        } else {
            loadSettings();
        }
    }

    public void saveSettings() {
        XmlWriter.write(settings, settingsLocation.toFile());
    }

    public void loadSettings() {
        settings = XmlReader.readSettings(settingsLocation.toFile());
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