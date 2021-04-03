class WaitEvent extends Event {
    private final Server server;

    WaitEvent(Customer customer, double time, Server server) {
        super(customer,time);
        this.server = server;
    }

    ServeEvent nextEvent(Server[] servers) {
        if (!this.server.isNotWaiting()) {
            ServeEvent serveEvent = new ServeEvent(super.getCustomer(), 
                this.server.getNextTime(), this.server);
            this.server.setWaiting(serveEvent);
            return serveEvent;
        }
        return null;
    }

    Stats updateStats(Stats stats) {
        return stats;
    }

    public String toString() {
        return String.format("%.3f %d waits at server %d", super.getTime(), 
            super.getCustomerID(), this.server.getID());
    }
}