package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader newEventLoader = new FXMLLoader(getClass().getResource("newWindow.fxml"));
        Parent root = newEventLoader.load();
        EventsController cont = newEventLoader.getController();
        cont.init(root);

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent mainRoot = mainLoader.load();
        Controller mainController = mainLoader.getController();
        mainController.init(mainRoot, primaryStage, cont);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
