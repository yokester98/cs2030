import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

class Server {
    private final int id;
    private final Event[] servingEvent;
    private final ArrayBlockingQueue<Event> queue;

    Server(int id, Event[] servingEvent) {
        this.id = id;
        this.servingEvent = servingEvent;
        this.queue = new ArrayBlockingQueue<Event>(1);
    }

    Server(int id, Event[] servingEvent, int queueLength) {
        this.id = id;
        this.servingEvent = servingEvent;
        this.queue = new ArrayBlockingQueue<Event>(queueLength);
    }

    int getID() {
        return this.id;
    }

    Event getServing() {
        return this.servingEvent[0];
    }

    ArrayBlockingQueue<Event> getQueue() {
        return this.queue;
    }

    Event getWaiting() {
        return this.getQueue().peek();
    }

    int getWaitingRemainingCap() {
        return this.getQueue().remainingCapacity();
    }

    void setServing(Event event) {
        this.servingEvent[0] = event;
    }

    Event removeWaiting() {
        return this.getQueue().poll();
    }

    void addWaiting(Event event) {
        this.getQueue().add(event);
    }

    boolean isNotServingAndWaiting() {
        return (this.getServing() == null && this.getWaitingRemainingCap() != 0);
    }

    boolean isNotWaiting() {
        return (this.getServing() != null && this.getWaitingRemainingCap() != 0);
    }

    void updateServing() {
        if (this.getServing() != null) {
            this.setServing(null);
        }
        if (this.getWaitingRemainingCap() != 0) {
            this.setServing(this.removeWaiting());
        }
    }

    public double getNextTime() {
        return this.getServing().getTime();
    }
}