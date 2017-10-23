package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sample.calendar.Calendar;
import sample.calendar.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class NewEventController extends RootController {

    @FXML
    private TextField eventTitle;
    @FXML
    private TextArea eventMessage;
    @FXML
    private TextField eventFrom;
    @FXML
    private TextField eventTo;
    @FXML
    private Button eventAccept;
    @FXML
    private Button eventDenied;

    private Calendar calendar;

    void init(Parent root, Calendar calendar) throws Exception {
        this.stage = new Stage();
        this.initStage(root, "newWindow.css", "");

        this.calendar = calendar;
    }

    @Override
    void initMainWindow() {
        eventTitle = (TextField) stage.getScene().lookup("#eventTitle");
        eventMessage = (TextArea) stage.getScene().lookup("#eventMessage");
        eventFrom = (TextField) stage.getScene().lookup("#eventFrom");
        eventTo = (TextField) stage.getScene().lookup("#eventTo");
        eventAccept = (Button) stage.getScene().lookup("#eventAccept");
        eventDenied = (Button) stage.getScene().lookup("#eventCancel");
    }

    void show() {
        stage.show();
    }

    @FXML
    void close() {
        stage.close();
    }

    void loadEventInfo(Optional<Event> event, Node node, LocalDate date) {
        if (event.isPresent()) {
            eventTitle.setText(event.get().getTitle());
            eventMessage.setText(event.get().getMessage());
            eventFrom.setText(event.get().getFrom().format(DateTimeFormatter.ISO_TIME));
            eventTo.setText(event.get().getTo().format(DateTimeFormatter.ISO_TIME));

            eventAccept.setOnMouseClicked(clicked -> {
                if(eventTitle.getText().length() > 0) {
                    event.get().setTitle(eventTitle.getText());
                    ((Label)((StackPane)node).getChildren().get(0)).setText(eventTitle.getText());
                    close();
                }
            });
        } else {
            eventTitle.setText("");
            eventMessage.setText("");
            eventFrom.setText("");
            eventTo.setText("");

            eventAccept.setOnMouseClicked(clicked -> {
                if(eventTitle.getText().length() > 0) {
                    Event newEvent = new Event(date, LocalTime.NOON, LocalTime.MIDNIGHT, eventTitle.getText(), eventMessage.getText());
                    calendar.addEvent(newEvent);
                    getCalendarGridCellEventsBox(node).getChildren().add(createNewEventPane(newEvent, date));
                    close();
                }
            });
        }
    }

    StackPane createNewEventPane(Event event, LocalDate date){
        StackPane eventPane = new StackPane();
        eventPane.getStyleClass().add("eventPanel");
        Label label= new Label(event.getTitle());
        label.getStyleClass().add("eventLabel");
        eventPane.getChildren().add(label);
        addDoubleClickListener(eventPane, Optional.of(event), date);
        eventPane.toFront();
        return eventPane;
    }

    private void addDoubleClickListener(Node node, Optional<Event> event, LocalDate date) {
        node.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                try {
                    loadEventInfo(event, node, date);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
