class ArrivalEvent extends Event {
    private final Customer customer;
    private final double time;

    ArrivalEvent(Customer customer, double time) {
        super(customer, time);
    }

    // Get the next available server
    Server getServer(Server[] servers) {
        for (int i = 0; i < servers.length; i++) {
            Server currentServer = servers[i];
            if (currentServer.isNotServingAndWaiting()) {
                return currentServer;
            }
        }

        for (int i = 0; i < servers.length; i++) {
            Server currentServer = servers[i];
            if (currentServer.isNotWaiting()) {
                return currentServer;
            }
        }

        return null;
    }

    Event nextEvent(Server [] servers) {
        Server server = getServer(servers);
        if (server == null) {
            return doLeaveEvent();
        } else if (server.isNotServingAndWaiting()) {
            ServedEvent servedEevent = doServedEvent(server);
            server.setServedEvent(servedEvent);
            return servedEvent;
        } else if (server.isNotWaiting()) {
            WaitEvent waitEvent = doWaitEvent(server);
            server.setWaitEvent(waitEvent);
            return waitEvent;
        }
    }

    ServedEvent doServedEvent(Server server) {
        return new ServedEvent(super.getCustomer(), super.getTime(), server);
    }

    WaitEvent doWaitEvent(Server Server) {
        return new WaitEvent(super.getCustomer(), super.getTime(), server);
    }

    LeaveEvent doLeaveEvent() {
        return new LeaveEvent(super.getCustomer(), super.getTime());
    }

    void updateStatistics(Statistics statistics) { }

    public String toString() {
        return String.format("%.3f %d arrives", super.getTime(), super.getCustomerID());
    }
}