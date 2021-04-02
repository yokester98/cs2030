class LeaveEvent extends Event {

    LeaveEvent(Customer customer, double time) {
        super(customer,time);
    }

    Event nextEvent(Server[] servers) {
        return null;
    }

    Stats updateStats(Stats stats) {
        return stats.increaseNumLeft();
    }

    public String toString() {
        return String.format("%.3f %d leaves", super.getTime(), super.getCustomerID());
    }

}