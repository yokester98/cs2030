abstract class Event {
    private final Customer customer;
    private final double time;

    public Event(Customer customer, double time) {
        this.customer = customer;
        this.time = time;
    }
    
    abstract Event nextEvent(Server[] servers);

    abstract Stats updateStats(Stats stats);

    Customer getCustomer() {
        return this.customer;
    }

    int getCustomerID() {
        return this.customer.getID();
    }

    double getTime() {
        return this.time;
    }
}
