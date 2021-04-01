class LeaveEvent extends Event {
    private final Customer customer;
    private final double time;

    LeaveEvent(Customer customer, double time) {
        super(customer,time);
    }

    Event nextEvent() {
        return null;
    }

    void updateStatistics(Statistics statistics) {
        statistics.increaseNumLeft();
    }

    public String toString() {
        return String.format("%.3f %d leaves", super.getTime(), super.getCustomerID());
    }

}