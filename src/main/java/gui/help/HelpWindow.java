package gui.help;

import gui.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by svkreml on 13.03.2017.
 */
public class HelpWindow {
    public HelpWindow(MainApp mainApp, String topic) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("Help.fxml"));
            AnchorPane NewProject = loader.load();
            HelpC controller = loader.getController();
            Stage helpStage = new Stage();
            controller.setStage(helpStage);
            helpStage.setTitle("Помощь");
            helpStage.initModality(Modality.WINDOW_MODAL);
            helpStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(NewProject);
            helpStage.setScene(scene);
            controller.setStage(helpStage);
            controller.init(topic);
            helpStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
