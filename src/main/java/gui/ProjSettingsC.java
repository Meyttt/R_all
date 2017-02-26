package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.ProjFile;

/**
 * Created by svkreml on 26.02.2017.
 */
public class ProjSettingsC {
    Stage stage;
    ProjFile projFile;

    @FXML
    TextField textField;
    @FXML
    CheckBox checkBox;

    public void init() {
        textField.setText(projFile.getLentaPath());
        checkBox.setSelected(projFile.isLenta());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void Apply(ActionEvent actionEvent) {
        projFile.setLentaPath(textField.getText());
        projFile.setLenta(checkBox.isSelected());
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
