class WaitEvent extends Event {
    private final Server server;

    WaitEvent(Customer customer, double time, Server server) {
        super(customer,time);
        this.server = server;
    }

    ServeEvent nextEvent(Server[] servers) {
        Server updatedServer = servers[this.server.getID() - 1];
        if (this.server.isNotWaiting() == false) {
            ServeEvent serveEvent = new ServeEvent(super.getCustomer(), 
                this.server.getNextTime(), updatedServer);
            updatedServer = this.server.setWaiting(serveEvent);
            servers[updatedServer.getID() - 1] = updatedServer;
            serveEvent = new ServeEvent(super.getCustomer(), this.server.getNextTime(), updatedServer);
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