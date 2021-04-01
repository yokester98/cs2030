class DoneEvent extends Event {
    private final Customer customer;
    private final double time;
    private final Server server;

    DoneEvent(Customer customer, double time, Server server) {
        super(customer,time);
        this.server = server;
    }

    Event nextEvent() {
        this.server.updateServing();
        return null;
    }

    void updateStatistics(Statistics statistics) {
        statistics.increaseWaitingTime(super.getTime() - super.getCustomer().getTime());
        statistics.increaseNumServed();
    }

    public String toString() {
        return String.format("%.3f %d done serving by server %d", super.getTime(),
            super.getCustomerID(), this.server.getID());
    }
}