class ServeEvent extends Event {
    private final Server server;

    ServeEvent(Customer customer, double time, Server server) {
        super(customer,time);
        this.server = server;
    }

    DoneEvent nextEvent(Server[] servers) {
        Server updatedServer = servers[this.server.getID() - 1];
        DoneEvent doneEvent = new DoneEvent(super.getCustomer(), 
            super.getTime() + (double) 1.0, updatedServer);
        updatedServer = this.server.setServing(doneEvent);
        servers[updatedServer.getID() - 1] = updatedServer;
        doneEvent = new DoneEvent(super.getCustomer(), super.getTime() + (double) 1.0, updatedServer);
        return doneEvent;
    }

    Stats updateStats(Stats stats) {
        stats = stats.increaseWaitingTime(this.getTime() - super.getCustomer().getTime());
        return stats.increaseNumServed();
    }

    public String toString() {
        return String.format("%.3f %d serves by server %d", super.getTime(), 
            super.getCustomerID(), this.server.getID());
    }
}