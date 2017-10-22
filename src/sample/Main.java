package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.calendar.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main extends Application {

    @FXML
    GridPane calendarPanel;
    @FXML
    GridPane leftPanel;
    @FXML
    GridPane rightPanel;

    @FXML
    TextField eventTitle;
    @FXML
    TextArea eventMessage;
    @FXML
    TextField eventFrom;
    @FXML
    TextField eventTo;
    @FXML
    Button eventAccept;
    @FXML
    Button eventDenied;

    Controller controller;

    LocalDate firstDayOfWeek;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass()
                .getResource("fxcalendar.css").toExternalForm());
        primaryStage.setScene(scene);
        initScreen(scene);
        loadView(firstDayOfWeek);
        loadLeftRightPanels(firstDayOfWeek);
        primaryStage.show();
    }

    private void initScreen(Scene scene) {
        calendarPanel = (GridPane) scene.lookup("#calendarGrid");
        leftPanel = (GridPane) scene.lookup("#leftPanel");
        rightPanel = (GridPane) scene.lookup("#rightPanel");
        controller = new Controller();
        firstDayOfWeek = LocalDate.now().minusDays((LocalDate.now().getDayOfWeek().getValue() - 1));
    }

    private void loadView(LocalDate firstDayOfWeek) {
        ObservableList<Node> childrens = calendarPanel.getChildren();
        int weekDay = 0;
        for (Node node : childrens) {
            LocalDate day = firstDayOfWeek.plusDays(weekDay++);
            getCalendarGridCellLabel(node).setText(day.format(DateTimeFormatter.ofPattern("LLLL dd")));
            List<Event> events = controller.loadEvents(firstDayOfWeek).stream().filter(e -> e.getDate().isEqual(day)).collect(Collectors.toList());
            events.forEach(e -> {
                StackPane event = new StackPane();
                event.getStyleClass().add("eventPanel");
                event.getChildren().add(new Label(e.getTitle()));
                addDoubleClickListener(event, Optional.of(e));
                ((VBox) (((ScrollPane) (getCalendarGridCell(node).getChildren().get(1))).getContent())).getChildren().add(event);
            });
        }
    }

    private void addDoubleClickListener(Node node, Optional<Event> event) {
        node.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newWindow.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Scene scene = new Scene(root1);
                    scene.getStylesheets().add(getClass()
                            .getResource("newWindow.css").toExternalForm());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    initNewWindow(scene,event);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initNewWindow(Scene scene, Optional<Event> event) {
        eventTitle = (TextField) scene.lookup("#eventTitle");
        eventMessage = (TextArea) scene.lookup("#eventMessage");
        eventFrom = (TextField) scene.lookup("#eventFrom");
        eventTo = (TextField) scene.lookup("#eventTo");
        eventAccept = (Button) scene.lookup("#eventAccept");
        eventDenied = (Button) scene.lookup("#eventDenied");

        if(event.isPresent()){
            eventTitle.setText(event.get().getTitle());
            eventMessage.setText(event.get().getMessage());
            eventFrom.setText(event.get().getFrom().format(DateTimeFormatter.ISO_TIME));
            eventTo.setText(event.get().getTo().format(DateTimeFormatter.ISO_TIME));
        }
    }

    private void loadLeftRightPanels(LocalDate firstDayOfWeek) {
        loadPanelLabels(firstDayOfWeek, leftPanel);
        loadPanelLabels(firstDayOfWeek, rightPanel);
    }

    private void loadPanelLabels(LocalDate firstDayOfWeek, GridPane panel) {
        ObservableList<Node> childrens = panel.getChildren();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int addDays = 0;
        for (Node node : childrens) {
            LocalDate day = firstDayOfWeek.plusDays(addDays += 7);
            getPanelCellLabel(node, false).setText("Week " + (day.get(weekFields.weekOfWeekBasedYear())));
            getPanelCellLabel(node, true).setText("" + day.getYear());
        }
    }

    private Label getCalendarGridCellLabel(Node node) {
        return ((Label) ((StackPane) getCalendarGridCell(node).getChildren().get(0)).getChildren().get(0));
    }

    private VBox getCalendarGridCell(Node node) {
        return ((VBox) (((StackPane) node).getChildren().get(0)));
    }

    private Label getPanelCellLabel(Node node, boolean second) {
        return ((Label) ((VBox) (((StackPane) node).getChildren().get(0))).getChildren().get(second ? 1 : 0));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
