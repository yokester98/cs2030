import java.util.Queue;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

class EventRunner {
    private final Server[] servers;
    private final List<Customer> customersList;
    private final Queue<Event> PQ;
    private final Stats[] stats;
    private final List<Double> restTimeList;

    EventRunner(Server[] servers, List<Customer> customersList) {
        this.servers = servers;
        this.customersList = customersList;
        this.PQ = new PriorityQueue<Event>(new EventComp());
        this.stats = new Stats[]{new Stats(0,0,0)};
        for (Customer customer : this.customersList) {
            this.PQ.add(new Event(customer, customer.getTime()));
        }
        this.restTimeList = new ArrayList<Double>(this.customersList.size());
        for (int i = 0; i < this.customersList.size() + 1; i++) {
            this.restTimeList.add(0.0);
        }
    }

    EventRunner(Server[] servers, List<Customer> customersList, List<Double> restTimeList) {
        this.servers = servers;
        this.customersList = customersList;
        this.PQ = new PriorityQueue<Event>(new EventComp());
        this.stats = new Stats[]{new Stats(0,0,0)};
        for (Customer customer : this.customersList) {
            this.PQ.add(new Event(customer, customer.getTime()));
        }
        this.restTimeList = restTimeList;
    }

    Server getServerWithShortestQ(Server[] servers) {
        Server shortestQ = servers[0];
        for (Server server : servers) {
            if (server.getWaitingRemainingCap() > shortestQ.getWaitingRemainingCap()) {
                shortestQ = server;
            }
        }
        return shortestQ;
    }

    void run() {
        int restCount = 0;
        while (this.PQ.peek() != null) {
            Event event = this.PQ.poll();
            Customer customer = event.getCustomer();
            
            System.out.println(event);

            if (customer.getState() == State.ARRIVES) {
                boolean served = false;

                // Check for idle servers
                for (Server server : servers) {
                    if (customer.getTime() >= server.getFreeTime()) {
                        server.setRestingStatus(false);
                    }
                    if (server.isNotServingAndWaiting() && server.isResting() == false) {
                        customer.setState(State.SERVES);
                        // Create a new SERVES event
                        Event servesEvent = new Event(customer, customer.getTime(), server);
                        if (restTimeList.get(restCount) > 0.0) {
                            server.setRestingStatus(true);
                        } else {
                            server.setRestingStatus(false);
                        }
                        served = true;
                        customer.setDoneTime(servesEvent.getTime());
                        server.updateFreeTime(servesEvent.getTime(), restTimeList.get(restCount), customer.getServiceTime());
                        this.PQ.add(servesEvent);
                        break;
                    }
                }

                // If no idle servers, check for servers with empty queue
                if (!served) {
                    if (customer.getGreedy()) {
                        if (this.getServerWithShortestQ(servers).isNotWaiting()) {
                            customer.setState(State.WAITS);
                            // Create a new WAITS event
                            Event waitsEvent = new Event(customer, customer.getTime(), this.getServerWithShortestQ(servers));
                            served = true;
                            this.PQ.add(waitsEvent);
                        }
                    } else {
                        for (Server server : servers) {
                            if (server.isNotWaiting()) {
                                customer.setState(State.WAITS);
                                // Create a new WAITS event
                                Event waitsEvent = new Event(customer, customer.getTime(), server);
                                served = true;
                                this.PQ.add(waitsEvent);
                                break;
                            }
                        }
                    }
                }

                // If no servers with empty queue, customer leaves
                if (!served) {
                    customer.setState(State.LEAVES);
                    // Create a new LEAVES event
                    Event leavesEvent = new Event(customer, customer.getTime());
                    this.PQ.add(leavesEvent);
                    this.stats[0] = this.stats[0].increaseNumLeft();
                }
            } else if (customer.getState() == State.WAITS) {
                customer.setState(State.SERVES);
                Server server = event.getServer();
                server.addWaiting(customer);
                if (customer.getTime() >= server.getFreeTime()) {
                    server.setRestingStatus(false);
                }
                // Create a new SERVES event
                Event servesEvent = new Event(customer, server.getFreeTime(), server);
                if (restTimeList.get(restCount) > 0.0) {
                    server.setRestingStatus(true);
                } else {
                    server.setRestingStatus(false);
                }
                customer.setDoneTime(servesEvent.getTime());
                server.updateFreeTime(servesEvent.getTime(), restTimeList.get(restCount), customer.getServiceTime());
                System.out.println(server.getFreeTime());
                this.PQ.add(servesEvent);
            } else if (customer.getState() == State.SERVES) {
                customer.setState(State.DONE);
                Server server = event.getServer();
                server.setServing(customer);
                this.stats[0] = this.stats[0].increaseWaitingTime(event.getTime() - customer.getTime());
                if (server.getWaiting() == customer) {
                    server.removeWaiting();
                }
                // Create a new DONE event
                Event doneEvent = new Event(customer, customer.getDoneTime(), server);
                this.PQ.add(doneEvent);
            } else if (customer.getState() == State.DONE) {
                restCount++;
                Server server = event.getServer();
                server.updateServing();
                this.stats[0] = this.stats[0].increaseNumServed();
            }
        }

        System.out.println(this.stats[0]);
    }
}