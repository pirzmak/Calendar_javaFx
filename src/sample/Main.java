package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import sample.calendar.Calendar;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        Calendar calendar = new Calendar();


        FXMLLoader newEventLoader = new FXMLLoader(getClass().getResource("newWindow.fxml"));
        Parent root = (Parent) newEventLoader.load();
        EventsController cont = newEventLoader.getController();
        cont.init(root, calendar);

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent mainRoot = (Parent) mainLoader.load();
        Controller mainController = mainLoader.getController();
        mainController.init(mainRoot, primaryStage, cont, calendar);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
