class Server {
    private final int id;
    private final Event[] events;

    Server(int id, Event[] events) {
        this.id = id;
        this.events = events;
    }

    int getID() {
        return this.id;
    }

    Event getServing() {
        return this.events[0];
    }

    Event getWaiting() {
        return this.events[1];
    }

    void setServing(Event event) {
        this.events[0] = event;
    }

    void setWaiting(Event event) {
        this.events[1] = event;
    }

    boolean isNotServingAndWaiting() {
        return (this.getServing() == null && this.getWaiting() == null);
    }

    boolean isNotWaiting() {
        return (this.getServing() != null && this.getWaiting() == null);
    }

    void updateServing() {
        if (this.getServing() != null) {
            this.setServing(null);
        }
        if (this.getWaiting() != null) {
            this.setServing(this.getWaiting());
            this.setWaiting(null);
        }
    }

    public double getNextTime() {
        return this.getServing().getTime();
    }
}