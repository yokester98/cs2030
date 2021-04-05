class WaitEvent extends Event {
    private final int serverID;

    WaitEvent(Customer customer, double time, int serverID) {
        super(customer,time);
        this.serverID = serverID;
    }

    ServeEvent nextEvent(Server[] servers) {
        if (!servers[this.serverID - 1].isNotWaiting()) {
            ServeEvent serveEvent = new ServeEvent(super.getCustomer(), 
                servers[this.serverID - 1].getNextTime(), this.serverID);
            servers[this.serverID - 1].removeWaiting();
            int count = servers[this.serverID - 1].getQueue().size();
            servers[this.serverID - 1].addWaiting(serveEvent);
            for (int i = 0; i < count; i++) {
                Event polled = servers[this.serverID - 1].removeWaiting();
                servers[this.serverID - 1].addWaiting(polled);
            }
            return serveEvent;
        }
        return null;
    }

    Stats updateStats(Stats stats) {
        return stats;
    }

    public String toString() {
        return String.format("%.3f %d waits at server %d", super.getTime(), 
            super.getCustomerID(), this.serverID);
    }
}