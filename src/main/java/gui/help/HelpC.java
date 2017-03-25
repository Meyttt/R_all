package gui.help;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by svkreml on 26.02.2017.
 */
public class HelpC {
    public WebView webView;
    private Stage stage;

    public void init(String topic){
/*        if(topic==null) topic="Справка для Rtran.html";
        File f = new File("src/main/resources/help/Build html documentation/"+topic);*/
        if(topic==null) topic="Справка для Rtran.html";
        File f = new File("src/main/resources/help/doc1/StartHelper.html");
        webView.getEngine().load(f.toURI().toString());
        //webView.getEngine().load(HelpC.class.getResource("file:///C:/Users/svkreml/Documents/HelpNDoc/Output/Build%20html%20documentation/1.html").toExternalForm());
//        webView.getEngine().load(HelpC.class.getResource("src/main/resources/help/Build html documentation/1.html").toExternalForm());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void back(ActionEvent actionEvent) {
        goBack();
    }
    public void goBack()
    {
        final WebHistory history = webView.getEngine().getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();

        Platform.runLater(() ->
        {
            history.go(entryList.size() > 1
                    && currentIndex > 0
                    ? -1
                    : 0);
        });
    }
}
