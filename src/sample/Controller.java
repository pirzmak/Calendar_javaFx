package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;

public class Controller extends RootController{
    @FXML
    private GridPane calendarPanel;
    @FXML
    private GridPane leftPanel;
    @FXML
    private GridPane rightPanel;

    private LocalDate firstDayOfWeek;

    private EventsController eventsController;

    void init(Parent root, Stage stage, EventsController eventsController) throws Exception {
        this.stage = stage;
        this.firstDayOfWeek = LocalDate.now().minusDays((LocalDate.now().getDayOfWeek().getValue()-1));
        this.eventsController = eventsController;

        initStage(root, "fxcalendar.css", "Hello World");

        stage.show();
    }

    @Override
    void initMainWindow() {
        calendarPanel = (GridPane) stage.getScene().lookup("#calendarGrid");
        leftPanel = (GridPane) stage.getScene().lookup("#leftPanel");
        rightPanel = (GridPane) stage.getScene().lookup("#rightPanel");

        loadView();
    }

    private void loadView() {
        int weekDay = 0;
        for (Node node : calendarPanel.getChildren()) {
            LocalDate day = firstDayOfWeek.plusDays(weekDay);

            getCalendarGridCellLabel(node).setText(day.format(DateTimeFormatter.ofPattern("LLLL dd")));

            loadStyles(node, day);

            getCalendarGridCellEventsBox(node).getChildren().clear();
            addDoubleClickListener(node, day);

            this.eventsController.refreshEvents(node, day);

            weekDay++;
        }
        loadLeftRightPanels(firstDayOfWeek);
    }

    private void loadStyles(Node node, LocalDate day){
        ScrollPane calendarGridContent = ((ScrollPane) (getCalendarGridCell(node).getChildren().get(1)));

        getCalendarGridCellLabel(node).getStyleClass().clear();
        calendarGridContent.getStyleClass().clear();
        getCalendarGridCellLabel(node).getStyleClass().add("calendarGridDayLabel");
        calendarGridContent.getStyleClass().add("scroll-pane");

        if(day.isEqual(LocalDate.now())) {
            getCalendarGridCellLabel(node).getStyleClass().add("today");
            calendarGridContent.getStyleClass().add("today");
        }
    }

    private void loadLeftRightPanels(LocalDate firstDayOfWeek) {
        loadPanelLabels(firstDayOfWeek, leftPanel);
        loadPanelLabels(firstDayOfWeek, rightPanel);
    }

    private void loadPanelLabels(LocalDate firstDayOfWeek, GridPane panel) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int addDays = 0;
        int DAYS_IN_WEEK = 7;
        for (Node node : panel.getChildren()) {
            LocalDate day = firstDayOfWeek.plusDays(addDays);
            getPanelCellLabel(node, false).setText("Week " + (day.get(weekFields.weekOfWeekBasedYear())));
            getPanelCellLabel(node, true).setText("" + day.getYear());
            addDays+=DAYS_IN_WEEK;
        }
    }

    private void addDoubleClickListener(Node node, LocalDate date) {
        node.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2 && !eventsController.stage.isShowing()) {
                try {
                    this.eventsController.loadEventInfo(Optional.empty(), node, date);
                    this.eventsController.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
