package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.LastOpened;
import project.ProjectR;

import java.io.File;
import java.io.IOException;

/**
 * Created by svkreml on 26.02.2017.
 */
public class MainWindowC {
    MainApp mainApp;

    public AnchorPane getTreeViewPane() {
        return treeViewPane;
    }
    @FXML
    private TextFlow errorConsole;
    @FXML
    private AnchorPane treeViewPane;
    @FXML
    Button settingsBtn;
    @FXML
    TextArea testTextArea;

    public TextFlow getErrorConsole() {
        return errorConsole;
    }

    public TextArea getTestTextArea() {
        return testTextArea;
    }

    public TabPane getRedactorTabs() {
        return redactorTabs;
    }

    @FXML
    TabPane redactorTabs;
    public void setB(boolean bool) {
        settingsBtn.setDisable(false);
    }

    public void newProject(ActionEvent actionEvent) {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("NewProject.fxml"));
            GridPane NewProject = loader.load();

            Stage newProjectStage = new Stage();
            newProjectStage.setTitle("Создание нового проекта");
            newProjectStage.initModality(Modality.WINDOW_MODAL);
            newProjectStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(NewProject);
            newProjectStage.setScene(scene);
            NewProjectC controller = loader.getController();
            controller.setNewProjectStage(newProjectStage);
            controller.setMainApp(mainApp);
            newProjectStage.showAndWait();
            mainApp.getPrimaryStage().show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openProject(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Открытие проекта");
        File defaultDirectory = new File("c:/");
        chooser.setInitialDirectory(defaultDirectory);
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("R-tran", "project.rtran"));
        File projectDirectory = chooser.showOpenDialog(mainApp.getPrimaryStage());
        if (projectDirectory == null) return;
        mainApp.setProjectR(new ProjectR(projectDirectory));
        LastOpened.save(projectDirectory);


        TreeC treeController = mainApp.getTreeController();
        try {
            treeController.openProject(projectDirectory.toPath());
        } catch (IOException e) {
            return;
            //e.printStackTrace();
        }



    }

    public void openPrLast(ActionEvent actionEvent) {
        mainApp.setProjectR(new ProjectR(LastOpened.load()));
    }


    public void handleSave(ActionEvent actionEvent) {
    }

    public void closeProject(ActionEvent actionEvent) {
    }

    public void closeProgram(ActionEvent actionEvent) {
    }

    public void help(ActionEvent actionEvent) {
    }

    public void about(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText("R-tran Редактор");
        alert.setContentText("версия 1.0.0\nИВБО-03-13 МИРЭА кафедра ВОСХОД");
        alert.showAndWait();
    }


    public void saveTextArea(ActionEvent actionEvent) {
    }

    public void settings(ActionEvent actionEvent) {
        try {
            // if(pr==null) return;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ProjSettings.fxml"));
            AnchorPane NewProject = loader.load();
            ProjSettingsC controller = loader.getController();
            Stage settingsStage = new Stage();
            controller.setStage(settingsStage);
            settingsStage.setTitle("Настройки проекта");
            settingsStage.initModality(Modality.WINDOW_MODAL);
            settingsStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(NewProject);
            settingsStage.setScene(scene);
            controller.setStage(settingsStage);
            controller.setProjFile(mainApp.getProjectR().getProjFile());
            controller.init();

            settingsStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
