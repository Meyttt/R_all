package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.ProjFile;

/**
 * Created by svkreml on 26.02.2017.
 */
public class ProjSettingsC {
    @FXML
    public RadioButton radioTestRun;
    @FXML
    public RadioButton radioInputFiles;
    @FXML
    public RadioButton radioConsole;
    Stage stage;
    ProjFile projFile;

    @FXML
    TextField textField;
    @FXML
    CheckBox checkBox;

    public void init() {
        textField.setText(projFile.getLentaPath());
        checkBox.setSelected(projFile.isLenta());
        setRadio(projFile.getRunType());
    }

    public String checkRadio() {
        if (radioConsole.isSelected())
            return "console";
        if (radioInputFiles.isSelected())
            return "inputFiles";
        if (radioTestRun.isSelected())
            return "testRun";
        return null;
    }
    public void setRadio(String type){
        if(type == null) return;
        if(type.equals("console")) radioConsole.setSelected(true);
        else if(type.equals("inputFiles")) radioInputFiles.setSelected(true);
        else if(type.equals("testRun")) radioTestRun.setSelected(true);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void Apply(ActionEvent actionEvent) {
        projFile.setLentaPath(textField.getText());
        projFile.setLenta(checkBox.isSelected());
        projFile.setRunType(checkRadio());
        projFile.save();
    }


    public void Quit(ActionEvent actionEvent) {
        {
            stage.close();
        }
    }

    public void setProjFile(ProjFile projFile) {
        this.projFile = projFile;
    }
}
