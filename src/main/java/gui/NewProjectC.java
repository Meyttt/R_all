package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import project.LastOpened;
import project.ProjectR;

import java.io.File;
import java.io.IOException;

/**
 * Created by svkreml on 26.02.2017.
 */
public class NewProjectC {
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    File selectedDirectory;
    File projectDirectory;
    @FXML
    private TextField projectName;
    @FXML
    private TextField directoryName;
    @FXML
    private Label errorLabel;


    private Stage newProjectStage;

    public void setNewProjectStage(Stage newProjectStage) {
        this.newProjectStage = newProjectStage;
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        newProjectStage.close();
    }

    @FXML
    public void handleChooseDirectory(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Создание проекта");
        File defaultDirectory = new File("c:/");
        chooser.setInitialDirectory(defaultDirectory);
        selectedDirectory = chooser.showDialog(newProjectStage);
        if(selectedDirectory==null)return;
        directoryName.setText(selectedDirectory.toString());
        errorLabel.setText("");
    }
    @FXML
    public void handleCreateProject() throws IOException {
        projectDirectory=new File(selectedDirectory, projectName.getText());
        if(projectDirectory.isDirectory()) {
            errorLabel.setText("Папка уже существует");
        }
        else if(!projectDirectory.mkdir()) {
            errorLabel.setText("Ошибка создания");
        }
        else {
            mainApp.setProjectR(new ProjectR(projectDirectory));
            System.out.println("projectDirectory = " + projectDirectory);
            LastOpened.save(projectDirectory);
            newProjectStage.close();
        }
    }

}
