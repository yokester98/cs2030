class Event {
    private final Customer customer;
    private final double time;
    private final Server server;

    Event(Customer customer, double time) {
        this.customer = customer;
        this.time = time;
        this.server = null;
    }

    Event(Customer customer, double time, Server server) {
        this.customer = customer;
        this.time = time;
        this.server = server;
    }

    Server getServer() {
        return this.server;
    }

    Customer getCustomer() {
        return this.customer;
    }

    int getCustomerID() {
        return this.customer.getID();
    }

    double getTime() {
        return this.time;
    }

    public String toString() {
        String output = String.format("%.3f %s", this.time, this.customer.toString());

        if (this.server != null) {
            if (this.customer.getState() == State.SERVES) {
                output += " by server " + this.server.getID();
            } else if (this.customer.getState() == State.WAITS) {
                output += " at server " + this.server.getID();
            } else if (this.customer.getState() == State.DONE) {
                output += " serving by server " + this.server.getID();
            }
        }

        return output;
    }
}
