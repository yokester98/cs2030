class Server {
    private final int id;
    private final Event serving;
    private final Event waiting;


    Server(int id, Event e1, Event e2) {
        this.id = id;
        this.serving = e1;
        this.waiting = e2;
    }

    int getID() {
        return this.id;
    }

    Server setServing(Event event) {
        return new Server(this.id, event, this.waiting);
    }

    Server setWaiting(Event event) {
        return new Server(this.id, this.serving, event);
    }

    boolean isNotServingAndWaiting() {
        return (serving == null && waiting == null);
    }

    boolean isNotWaiting() {
        return (serving != null && waiting == null);
    }

    Server updateServing() {
        Server server = this;
        if (serving != null) {
            server = new Server(server.getID(), null, this.waiting);
        }
        if (waiting != null) {
            server = new Server(server.getID(), this.waiting, null);
        }
        return server;
    }

    public double getNextTime() {
        return this.serving.getTime();
    }
}