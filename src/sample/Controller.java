package sample;

import sample.calendar.Calendar;
import sample.calendar.Event;

import java.time.LocalDate;
import java.util.List;

public class Controller {
    private Calendar calendar;

    public Controller() {
        this.calendar = new Calendar();
    }

    public List<Event> loadEvents(LocalDate start) {
        int daysInWeek = 7;
        LocalDate end = start.plusDays(this.calendar.getNumOfWeeks()*daysInWeek);
        return calendar.getEvents(start, end);
    }
}
