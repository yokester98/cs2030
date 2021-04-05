class ArrivalEvent extends Event {

    ArrivalEvent(Customer customer, double time) {
        super(customer, time);
    }

    // Get the next available server
    int getServerID(Server[] servers) {
        for (int i = 0; i < servers.length; i++) {
            Server currentServer = servers[i];
            if (currentServer.isNotServingAndWaiting()) {
                return currentServer.getID();
            }
        }

        for (int i = 0; i < servers.length; i++) {
            Server currentServer = servers[i];
            if (currentServer.isNotWaiting()) {
                return currentServer.getID();
            }
        }

        return 0;
    }

    Event nextEvent(Server [] servers) {
        int serverID = getServerID(servers);
        if (serverID == 0) {
            return doLeaveEvent();
        } else if (servers[serverID - 1].isNotServingAndWaiting()) {
            ServeEvent serveEvent = doServeEvent(serverID);
            servers[serverID - 1].setServing(serveEvent);
            return serveEvent;
        } else if (servers[serverID - 1].isNotWaiting()) {
            WaitEvent waitEvent = doWaitEvent(serverID);
            servers[serverID - 1].addWaiting(waitEvent);
            return waitEvent;
        }
        return null;
    }

    ServeEvent doServeEvent(int serverID) {
        return new ServeEvent(super.getCustomer(), super.getTime(), serverID);
    }

    WaitEvent doWaitEvent(int serverID) {
        return new WaitEvent(super.getCustomer(), super.getTime(), serverID);
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