class WaitEvent extends Event {
    private final Customer customer;
    private final double time;
    private final Server server;

    /** 
     * Creates an WaitEvent.
     * @param customer customer that the event is involving
     * @param time time at which event is created
     * @param server server that the WaitEvent belongs to
     */
    public WaitEvent(Customer customer, double time, Server server) {
        super(customer,time);
        this.server = server;
    }

    ServeEvent nextEvent() {
        if (this.server.isNotWaiting() == false) {
            ServeEvent serveEvent = new ServeEvent(super.getCustomer(), 
                this.server.getNextTime(), this.server);
            this.server.setWaiting(serveEvent);
            return serveEvent;
        }
        return null;
    }

    public void updateStatistics(Statistics statistics) {
        return;
    }

    public String toString() {
        return String.format("%.3f %d waits at server %d", super.getTime(), 
            super.getCustomerID(), this.server.getID());
    }

}