package sample.calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class Calendar {
    private Vector<Event> events;
    private int numOfWeeks;

    public Calendar() {
        this.events = new Vector<>();
        this.events.add(new Event(LocalDate.now().minusDays(1), LocalTime.NOON, LocalTime.MIDNIGHT,"POPO", ""));
        this.events.add(new Event(LocalDate.now().minusDays(1), LocalTime.NOON, LocalTime.MIDNIGHT,"POPO", ""));
        this.events.add(new Event(LocalDate.now().minusDays(1), LocalTime.NOON, LocalTime.MIDNIGHT,"POPO", ""));
        this.events.add(new Event(LocalDate.now().minusDays(1), LocalTime.NOON, LocalTime.MIDNIGHT,"POPO", ""));
//        this.events.add(new Event(LocalDate.now(), LocalTime.MIDNIGHT, "POPO", ""));
//        this.events.add(new Event(LocalDate.now(), LocalTime.MIDNIGHT, "POPO", ""));
//        this.events.add(new Event(LocalDate.now(), LocalTime.MIDNIGHT, "POPO", ""));
//        this.events.add(new Event(LocalDate.now(), LocalTime.MIDNIGHT, "POPO", ""));
        this.numOfWeeks = 4;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public List<Event> getEvents(LocalDate start, LocalDate end) {
        return events.stream().filter(e -> start.isBefore(e.getDate()) && end.isAfter(e.getDate())).collect(Collectors.toList());
    }

    public int getNumOfWeeks() {
        return numOfWeeks;
    }
}
