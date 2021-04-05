class DoneEvent extends Event {
    private final int serverID;

    DoneEvent(Customer customer, double time, int serverID) {
        super(customer,time);
        this.serverID = serverID;
    }

    Event nextEvent(Server[] servers) {
        servers[this.serverID - 1].updateServing();
        return null;
    }

    Stats updateStats(Stats stats) {
        return stats;
    }

    public String toString() {
        return String.format("%.3f %d done serving by server %d", this.getTime(),
            super.getCustomerID(), this.serverID);
    }
}