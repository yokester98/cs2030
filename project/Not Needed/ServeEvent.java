class ServeEvent extends Event {
    private final int serverID;

    ServeEvent(Customer customer, double time, int serverID) {
        super(customer,time);
        this.serverID = serverID;
    }

    DoneEvent nextEvent(Server[] servers) {
        DoneEvent doneEvent = new DoneEvent(super.getCustomer(), 
            super.getTime() + super.getCustomer().getServiceTime(), this.serverID);
        if (servers[this.serverID - 1].getQueue().peek() == this) {
            servers[this.serverID - 1].removeWaiting();
        }
        servers[this.serverID - 1].setServing(doneEvent);
        return doneEvent;
    }

    Stats updateStats(Stats stats) {
        stats = stats.increaseWaitingTime(super.getTime() - super.getCustomer().getTime());
        return stats.increaseNumServed();
    }

    public String toString() {
        return String.format("%.3f %d serves by server %d", super.getTime(), 
            super.getCustomerID(), this.serverID);
    }
}