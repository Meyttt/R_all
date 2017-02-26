package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.ProjectR;

import java.io.IOException;

/**
 * Created by svkreml on 26.02.2017.
 */
public class MainApp extends Application {
    AnchorPane treeViewPane;

    public TreeC getTreeController() {
        return treeController;
    }

    public void setTreeController(TreeC treeController) {
        this.treeController = treeController;
    }

    TreeC treeController;
    MainWindowC mainWindowC;
    ProjectR projectR;
    private Stage primaryStage;
    private BorderPane mainWindow;

    public static void main(String[] args) {
        launch(args);
    }

    public MainWindowC getMainWindowC() {
        return mainWindowC;
    }

    public void setMainWindowC(MainWindowC mainWindowC) {
        this.mainWindowC = mainWindowC;
    }

    public ProjectR getProjectR() {
        return projectR;
    }

    public void setProjectR(ProjectR projectR) {
        this.projectR = projectR;
       mainWindowC.setB(false);
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("R_tran");
        initMainWindow();
        initTree();
    }

    private void initMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("MainWindow.fxml"));
            mainWindow = loader.load();
            Scene scene = new Scene(mainWindow);
            MainWindowC controller = loader.getController();
            controller.setMainApp(this);
            setMainWindowC(controller);
            scene.getStylesheets().add(MainApp.class.getResource("xml-highlighting.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
            treeViewPane = controller.getTreeViewPane();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTree() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("Tree.fxml"));
            AnchorPane treeView = loader.load();
            // Даём контроллеру доступ к главному приложению.
            treeController = loader.getController();
            //testTextArea= treeController.getT
//            treeViewPane.getChildren().add(treeView);
            // treeView.fit
            treeViewPane.getChildren().add(treeView);
            treeController.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
