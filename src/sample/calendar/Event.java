package sample.calendar;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Event implements Serializable{
    private LocalDate date;
    private LocalTime from;
    private LocalTime to;
    private String title;
    private String message;

    public Event(LocalDate date, LocalTime from, LocalTime to, String title, String message) {
        this.date = date;
        this.from = from;
        this.to = to;
        this.title = title;
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime time) {
        this.from = time;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime time) {
        this.to = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
