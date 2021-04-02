class ArrivalEvent extends Event {

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
            ServeEvent serveEvent = doServeEvent(server);
            Server updatedServer = server.setServing(serveEvent);
            servers[updatedServer.getID() - 1] = updatedServer;
            serveEvent = doServeEvent(updatedServer);
            return serveEvent;
        } else if (server.isNotWaiting()) {
            WaitEvent waitEvent = doWaitEvent(server);
            Server updatedServer = server.setWaiting(waitEvent);
            servers[updatedServer.getID() - 1] = updatedServer;
            waitEvent = doWaitEvent(updatedServer);
            return waitEvent;
        }
        return null;
    }

    ServeEvent doServeEvent(Server server) {
        return new ServeEvent(super.getCustomer(), super.getTime(), server);
    }

    WaitEvent doWaitEvent(Server server) {
        return new WaitEvent(super.getCustomer(), super.getTime(), server);
    }

    LeaveEvent doLeaveEvent() {
        return new LeaveEvent(super.getCustomer(), super.getTime());
    }

    Stats updateStats(Stats stats) {
        return stats;
    }

    public String toString() {
        return String.format("%.3f %d arrives", super.getTime(), super.getCustomerID());
    }
}