class DoneEvent extends Event {
    private final Server server;

    DoneEvent(Customer customer, double time, Server server) {
        super(customer,time);
        this.server = server;
    }

    Event nextEvent(Server[] servers) {
        Server updatedServer = this.server.updateServing();
        servers[updatedServer.getID() - 1] = updatedServer;
        return null;
    }

    Stats updateStats(Stats stats) {
        return stats;
    }

    public String toString() {
        return String.format("%.3f %d done serving by server %d", this.getTime(),
            super.getCustomerID(), this.server.getID());
    }
}