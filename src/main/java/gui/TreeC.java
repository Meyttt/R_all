package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import project.Explorer;

import java.io.IOException;
import java.nio.file.Path;

public class TreeC {
    MainApp mainApp;
    @FXML
    TreeView treeView;

    public void setRedactorTabs(TabPane redactorTabs) {
        this.redactorTabs = redactorTabs;
    }

    TabPane redactorTabs;
    Explorer explorer;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setExplorer() {
        explorer = new Explorer(treeView, redactorTabs);
    }

    public void buttonRefresh(ActionEvent actionEvent) {
    }

    public void buttonSave(ActionEvent actionEvent) {
    }

    public void openProject(Path path) {
        try {
            explorer.openProject(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Explorer getExplorer() {
        return explorer;
    }
}
