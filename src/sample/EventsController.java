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
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventsController extends RootController {

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
    private Button eventCancel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label fromLabel;
    @FXML
    private Label toLabel;

    private Calendar calendar;

    private DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

    void init(Parent root) throws Exception {
        this.stage = new Stage();
        this.initStage(root, "newWindow.css", "");

        this.calendar = new Calendar();
    }

    @Override
    void initMainWindow() {
        eventTitle = (TextField) stage.getScene().lookup("#eventTitle");
        eventMessage = (TextArea) stage.getScene().lookup("#eventMessage");
        eventFrom = (TextField) stage.getScene().lookup("#eventFrom");
        eventTo = (TextField) stage.getScene().lookup("#eventTo");
        eventAccept = (Button) stage.getScene().lookup("#eventAccept");
        eventCancel = (Button) stage.getScene().lookup("#eventCancel");

        titleLabel = ((Label)stage.getScene().lookup("#titleLabel"));
        fromLabel = ((Label)stage.getScene().lookup("#fromLabel"));
        toLabel = ((Label)stage.getScene().lookup("#toLabel"));

        eventTitle.textProperty().addListener((obs,old,niu)->{
            setValidationTitleLabel(niu);
        });

        eventFrom.textProperty().addListener((obs,old,niu)->{
            setValidationDataLabel(fromLabel, isValidateDate(niu), "OD");
        });

        eventTo.textProperty().addListener((obs,old,niu)->{
            setValidationDataLabel(toLabel, isValidateDate(niu), "DO");
        });
    }

    void show() {
        stage.show();
    }

    @FXML
    void close() {
        stage.close();
    }

    void loadEventInfo(Optional<Event> event, Node node, LocalDate date) {
        refreshStyles();
        if(event.isPresent()) {
            eventTitle.setText(event.get().getTitle());
            eventMessage.setText(event.get().getMessage());
            eventFrom.setText(format.format(event.get().getFrom()));
            eventTo.setText(format.format(event.get().getTo()));

            eventAccept.setText("Zapisz");

            eventAccept.setOnMouseClicked(clicked -> {
                if(isValidate(eventTitle.getText(), eventFrom.getText(), eventTo.getText())) {
                    uploadEvent(event.get(), node);
                    close();
                }
            });

            eventCancel.setText("Usuń");

            eventCancel.setOnMouseClicked(clicked -> {
                deleteEvent(event.get(), date, node.getParent().getParent().getParent().getParent().getParent().getParent());
                close();
            });

        } else {
            eventTitle.setText("");
            eventMessage.setText("");
            eventFrom.setText("");
            eventTo.setText("");

            eventAccept.setOnMouseClicked(clicked -> {
                if(isValidate(eventTitle.getText(), eventFrom.getText(), eventTo.getText())) {
                    addNewEvent(date, node);
                    close();
                }
            });

            eventCancel.setText("Anuluj");

            eventCancel.setOnMouseClicked(clicked -> {
                close();
            });
        }
    }

    void refreshEvents(Node node, LocalDate date) {
        List<Event> events = calendar.getEvents(date).stream()
                .filter(e -> e.getDate().isEqual(date))
                .sorted(Comparator.comparing(Event::getFrom))
                .collect(Collectors.toList());

        getCalendarGridCellEventsBox(node).getChildren().clear();

        events.forEach(e -> getCalendarGridCellEventsBox(node).getChildren().add(this.createNewEventPane(e, date)));
    }

    private void refreshStyles() {
        titleLabel.setText("Tytuł");
        titleLabel.getStyleClass().clear();
        fromLabel.setText("OD");
        fromLabel.getStyleClass().clear();
        toLabel.setText("DO");
        toLabel.getStyleClass().clear();
    }

    private boolean isValidate(String title, String start, String end){
        setValidationTitleLabel(title);

        boolean dateValidation = isValidateDate(start) && isValidateDate(end) && LocalTime.parse(start).isBefore(LocalTime.parse(end));
        setValidationDataLabel(fromLabel, dateValidation, "OD");
        setValidationDataLabel(toLabel, dateValidation, "DO");

        return !title.isEmpty() && dateValidation;
    }

    private void setValidationTitleLabel(String title){
        if(title.isEmpty()){
            titleLabel.setText("Tytuł (Nie może być pusty)");
            titleLabel.getStyleClass().add("error");
        } else {
            titleLabel.setText("Tytuł");
            titleLabel.getStyleClass().clear();
        }
    }

    private void setValidationDataLabel(Label dataLabel, boolean validate, String type){
        if(!validate) {
            dataLabel.setText(type + " (hh:mm)");
            dataLabel.getStyleClass().add("error");
        } else {
            dataLabel.setText(type);
            dataLabel.getStyleClass().clear();
        }
    }

    private boolean isValidateDate(String date) {
        if(date.isEmpty())
            return false;
        try {
            LocalTime.parse(date);
            return true;
        } catch (DateTimeParseException e){
            return false;
        }
    }

    private void uploadEvent(Event event, Node node) {
        event.setTitle(eventTitle.getText());
        event.setMessage(eventMessage.getText());
        event.setFrom(LocalTime.parse(eventFrom.getText()));
        event.setTo(LocalTime.parse(eventTo.getText()));
        ((Label) ((StackPane) node).getChildren().get(0)).setText(format.format(event.getFrom()) + "-" + format.format(event.getTo()) + " " + event.getTitle());
        close();
    }

    private void deleteEvent(Event event, LocalDate date, Node node) {
        calendar.deleteEvent(event);
        refreshEvents(node, date);
    }

    private void addNewEvent(LocalDate date, Node node) {
        Event newEvent = new Event(date, LocalTime.parse(eventFrom.getText()),LocalTime.parse(eventTo.getText()), eventTitle.getText(), eventMessage.getText());
        calendar.addEvent(newEvent);
        refreshEvents(node, date);
    }

    private StackPane createNewEventPane(Event event, LocalDate date) {
        StackPane eventPane = new StackPane();
        eventPane.getStyleClass().add("eventPanel");

        Label label = new Label(format.format(event.getFrom()) + "-" + format.format(event.getTo()) + " " + event.getTitle());
        label.getStyleClass().add("eventLabel");

        eventPane.getChildren().add(label);
        addDoubleClickListener(eventPane, event, date);
        eventPane.toFront();

        return eventPane;
    }

    private void addDoubleClickListener(Node node, Event event, LocalDate date) {
        node.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                try {
                    loadEventInfo(Optional.of(event), node, date);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
