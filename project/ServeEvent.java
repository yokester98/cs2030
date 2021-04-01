class ServeEvent extends Event {
    private final Customer customer;
    private final double time;
    private final Server server;

    ServeEvent(Customer customer, double time, Server server) {
        super(customer,time);
        this.server = server;
    }

    DoneEvent nextEvent() {
        DoneEvent doneEvent = new DoneEvent(super.getCustomer(), 
            super.getTime() + (double) 1.0, this.server);
        this.server.setServing(doneEvent);
        return doneEvent;
    }

    void updateStatistics(Statistics statistics) { }

    public String toString() {
        return String.format("%.3f %d serves by server %d", super.getTime(), 
            super.getCustomerID(), this.server.getID());
    }
}