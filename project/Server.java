class Server {
    private final int id;
    private final Event serving = null;
    private final Event waiting = null;

    Server(int id) {
        this.id = id;
    }

    int getID() {
        return this.id;
    }

    void setServing(Event event) {
        this.serving = event;
    }

    void setWaiting(Event event) {
        this.waiting = event;
    }

    boolean isNotServingAndWaiting() {
        return (serving == null && waiting == null);
    }

    boolean isNotWaiting() {
        return (served != null && waiting == null);
    }

    void updateServing() {
        if (serving != null) {
            serving = null;
        }
        if (waiting != null) {
            serving = waiting;
            waiting = null;
        }
    }

    public double getNextTime() {
        return this.serving.getTime();
    }
}