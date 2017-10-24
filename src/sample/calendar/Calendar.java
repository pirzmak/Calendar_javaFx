package sample.calendar;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class Calendar {
    private Vector<Event> events;
    private int numOfWeeks;

    public Calendar() {
        this.events = new Vector<>();
        readFile();
        this.numOfWeeks = 4;
    }

    private void readFile() {
        try{
            //use buffering
            InputStream file = new FileInputStream("events.bor");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
            try{
                events = (Vector<Event>)input.readObject();
            }
            finally{
                input.close();
            }
        }
        catch(ClassNotFoundException ex){
            System.out.println("Cannot perform input. Class not found. " + ex);
        }
        catch(IOException ex){
            System.out.println("Cannot perform input. " + ex);
        }
    }

    private void writeToFile() {
        try{
            //use buffering
            OutputStream file = new FileOutputStream("events.bor");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try{
                output.writeObject(events);
            }
            finally{
                output.close();
            }
        }
        catch(IOException ex){
            System.out.println("Cannot perform output. " + ex);
        }
    }

    public void addEvent(Event event) {
        this.events.add(event);
        writeToFile();
    }

    public List<Event> getEvents(LocalDate start) {
        return events.stream().filter(e -> start.isEqual(e.getDate())).collect(Collectors.toList());
    }

    public int getNumOfWeeks() {
        return numOfWeeks;
    }
}
