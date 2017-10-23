package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.calendar.Calendar;
import sample.calendar.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class Controller extends RootController{
    @FXML
    private GridPane calendarPanel;
    @FXML
    private GridPane leftPanel;
    @FXML
    private GridPane rightPanel;

    private Calendar calendar;
    private LocalDate firstDayOfWeek;

    private NewEventController newEventController;

    void init(Parent root, Stage stage, NewEventController newEventController, Calendar calendar) throws Exception {
        this.stage = stage;

        this.calendar = calendar;
        this.firstDayOfWeek = LocalDate.now().minusDays((LocalDate.now().getDayOfWeek().getValue()-1));

        this.newEventController = newEventController;

        initStage(root, "fxcalendar.css", "Hello World");
        initMainWindow();
        loadView();

        stage.show();
    }

    @Override
    void initMainWindow() {
        calendarPanel = (GridPane) stage.getScene().lookup("#calendarGrid");
        leftPanel = (GridPane) stage.getScene().lookup("#leftPanel");
        rightPanel = (GridPane) stage.getScene().lookup("#rightPanel");

        DropShadow shadow = new DropShadow();
        stage.getScene().lookup("#prevButton").addEventHandler(MouseEvent.MOUSE_ENTERED, e -> stage.getScene().lookup("#prevButton").setEffect(shadow));
        stage.getScene().lookup("#prevButton2").addEventHandler(MouseEvent.MOUSE_ENTERED, e -> stage.getScene().lookup("#prevButton2").setEffect(shadow));
        stage.getScene().lookup("#nextButton").addEventHandler(MouseEvent.MOUSE_ENTERED, e -> stage.getScene().lookup("#nextButton").setEffect(shadow));
        stage.getScene().lookup("#nextButton2").addEventHandler(MouseEvent.MOUSE_ENTERED, e -> stage.getScene().lookup("#nextButton2").setEffect(shadow));
        stage.getScene().lookup("#prevButton").addEventHandler(MouseEvent.MOUSE_EXITED, e -> stage.getScene().lookup("#prevButton").setEffect(null));
        stage.getScene().lookup("#prevButton2").addEventHandler(MouseEvent.MOUSE_EXITED, e -> stage.getScene().lookup("#prevButton2").setEffect(null));
        stage.getScene().lookup("#nextButton").addEventHandler(MouseEvent.MOUSE_EXITED, e -> stage.getScene().lookup("#nextButton").setEffect(null));
        stage.getScene().lookup("#nextButton2").addEventHandler(MouseEvent.MOUSE_EXITED, e -> stage.getScene().lookup("#nextButton2").setEffect(null));
    }

    private void loadView() {
        ObservableList<Node> childrens = calendarPanel.getChildren();
        int weekDay = 0;
        for (Node node : childrens) {
            LocalDate day = firstDayOfWeek.plusDays(weekDay);

            DropShadow shadow = new DropShadow();
            node.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> node.setEffect(shadow));
            node.addEventHandler(MouseEvent.MOUSE_EXITED, e -> node.setEffect(null));

            node.setCursor(Cursor.HAND);

            getCalendarGridCellLabel(node).setText(day.format(DateTimeFormatter.ofPattern("LLLL dd")));
            getCalendarGridCellLabel(node).getStyleClass().clear();
            ((ScrollPane) (getCalendarGridCell(node).getChildren().get(1))).getStyleClass().clear();
            getCalendarGridCellLabel(node).getStyleClass().add("calendarGridDayLabel");
            ((ScrollPane) (getCalendarGridCell(node).getChildren().get(1))).getStyleClass().add("scroll-pane");

            if(day.isEqual(LocalDate.now())) {
                getCalendarGridCellLabel(node).getStyleClass().add("today");
                ((ScrollPane) (getCalendarGridCell(node).getChildren().get(1))).getStyleClass().add("today");
            }

            getCalendarGridCellEventsBox(node).getChildren().clear();
            addDoubleClickListener(node, day);

            List<Event> events = this.loadEvents(firstDayOfWeek).stream().filter(e -> e.getDate().isEqual(day)).collect(Collectors.toList());

            events.forEach(e -> getCalendarGridCellEventsBox(node).getChildren().add(this.newEventController.createNewEventPane(e, day)));
            weekDay++;
        }
        loadLeftRightPanels(firstDayOfWeek);
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
            LocalDate day = firstDayOfWeek.plusDays(addDays);
            getPanelCellLabel(node, false).setText("Week " + (day.get(weekFields.weekOfWeekBasedYear())));
            getPanelCellLabel(node, true).setText("" + day.getYear());
            addDays+=7;
        }
    }

    private void addDoubleClickListener(Node node, LocalDate date) {
        node.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2 && !newEventController.stage.isShowing()) {
                try {
                    this.newEventController.loadEventInfo(Optional.empty(), node, date);
                    this.newEventController.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<Event> loadEvents(LocalDate start) {
        int daysInWeek = 7;
        LocalDate end = start.plusDays(this.calendar.getNumOfWeeks()*daysInWeek);
        return calendar.getEvents(start, end);
    }

    public void goToNextWeek(){
        this.firstDayOfWeek = firstDayOfWeek.plusWeeks(1);
        loadView();
    }

    public void goToPrevWeek(){
        this.firstDayOfWeek = firstDayOfWeek.minusWeeks(1);
        loadView();
    }
}
