import java.util.Comparator;

class EventComp implements Comparator<Event> {
    public int compare(Event e1, Event e2) {
        return e1.getCustID() - e2.getCustID();
    }
}
