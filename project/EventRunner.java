package cs2030.simulator;

import java.util.Queue;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import cs2030.simulator.RandomGenerator;
import cs2030.simulator.Stats;

public class EventRunner {
    private final Server[] servers;
    private final List<Customer> customersList;
    private final Queue<Event> priorityQ;
    private final Stats[] stats;
    private final List<Double> restTimeList;
    private static final double ZEROVAL = 1e-10;

    public EventRunner(Server[] servers, List<Customer> customersList) {
        this.servers = servers;
        this.customersList = customersList;
        this.priorityQ = new PriorityQueue<Event>(new EventComp());
        this.stats = new Stats[]{new Stats(0,0,0)};
        for (Customer customer : this.customersList) {
            this.priorityQ.add(new Event(customer, customer.getTime()));
        }
        this.restTimeList = new ArrayList<Double>();
        for (int i = 0; i < this.customersList.size(); i++) {
            this.restTimeList.add(0.0);
        }
    }

    public EventRunner(Server[] servers, List<Customer> customersList, List<Double> restTimeList) {
        this.servers = servers;
        this.customersList = customersList;
        this.priorityQ = new PriorityQueue<Event>(new EventComp());
        this.stats = new Stats[]{new Stats(0,0,0)};
        for (Customer customer : this.customersList) {
            this.priorityQ.add(new Event(customer, customer.getTime()));
        }
        this.restTimeList = restTimeList;
    }

    public Server getServerWithShortestQ(Server[] servers) {
        Server shortestQ = servers[0];
        for (Server server : servers) {
            if (server.getWaitingRemainingCap() > shortestQ.getWaitingRemainingCap()) {
                shortestQ = server;
            }
        }
        return shortestQ;
    }

    public void run() {
        int restCount = 0;
        while (this.priorityQ.peek() != null) {
            Event event = this.priorityQ.poll();
            Customer customer = event.getCustomer();

            for (Server server : servers) {
                if (event.getTime() >= server.getFreeTime()) {
                    server.setRestingStatus(false);
                }
            }

            if (customer.getState() == State.SERVES) {
                Server server = event.getServer();
                double totalServiceTime = customer.getServiceTime();
                for (Customer queueCust : server.getQueue()) {
                    if (queueCust.getDoneTime() <= server.getFreeTime() && queueCust.getID() 
                        > customer.getID() && queueCust.getDoneTime() != 0.0) {
                        totalServiceTime += queueCust.getServiceTime();
                    }
                }
                if ((Math.abs(server.getFreeTime() - totalServiceTime - event.getTime())
                    > ZEROVAL) && (server.getFreeTime() > event.getTime())) {
                    Event servesEvent = new Event(customer, server.getFreeTime() - totalServiceTime 
                        + customer.getServiceTime(), server);
                    customer.setDoneTime(servesEvent.getTime());
                    server.updateFreeTime(servesEvent.getTime(), customer.getServiceTime());
                    this.priorityQ.add(servesEvent);
                    continue;
                }
            }
            
            System.out.println(event);

            if (customer.getState() == State.ARRIVES) {
                boolean served = false;

                // Check for idle servers
                for (Server server : servers) {
                    if (server.isNotServingAndWaiting() && server.isResting() == false) {
                        customer.setState(State.SERVES);
                        // Create a new SERVES event
                        Event servesEvent = new Event(customer, customer.getTime(), server);
                        served = true;
                        customer.setDoneTime(servesEvent.getTime());
                        server.updateFreeTime(servesEvent.getTime(), customer.getServiceTime());
                        this.priorityQ.add(servesEvent);
                        break;
                    }
                }

                // If no idle servers, check for servers with empty queue
                if (!served) {
                    if (customer.getGreedy()) {
                        if (this.getServerWithShortestQ(servers).isNotWaiting()) {
                            customer.setState(State.WAITS);
                            // Create a new WAITS event
                            Event waitsEvent = new Event(customer, customer.getTime(), 
                                this.getServerWithShortestQ(servers));
                            served = true;
                            this.priorityQ.add(waitsEvent);
                        }
                    } else {
                        for (Server server : servers) {
                            if (server.isNotWaiting()) {
                                customer.setState(State.WAITS);
                                // Create a new WAITS event
                                Event waitsEvent = new Event(customer, customer.getTime(), server);
                                served = true;
                                this.priorityQ.add(waitsEvent);
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
                    this.priorityQ.add(leavesEvent);
                    this.stats[0] = this.stats[0].increaseNumLeft();
                }
            } else if (customer.getState() == State.WAITS) {
                customer.setState(State.SERVES);
                Server server = event.getServer();
                server.addWaiting(customer);
                // Create a new SERVES event
                Event servesEvent = new Event(customer, server.getFreeTime(), server);
                customer.setDoneTime(servesEvent.getTime());
                server.updateFreeTime(servesEvent.getTime(), customer.getServiceTime());
                this.priorityQ.add(servesEvent);
            } else if (customer.getState() == State.SERVES) {
                customer.setState(State.DONE);
                Server server = event.getServer();
                server.setServing(customer);
                this.stats[0] = this.stats[0].increaseWaitingTime(
                        event.getTime() - customer.getTime());
                if (server.getWaiting() == customer) {
                    server.removeWaiting();
                }
                // Create a new DONE event
                Event doneEvent = new Event(customer, event.getTime() + 
                    customer.getServiceTime(), server);
                server.updateFreeTime(doneEvent.getTime());
                this.priorityQ.add(doneEvent);
            } else if (customer.getState() == State.DONE) {
                Server server = event.getServer();
                if (restTimeList.get(restCount) > 0.0) {
                    server.setRestingStatus(true);
                } else {
                    server.setRestingStatus(false);
                }
                server.updateServing();
                server.updateFreeTime(event.getTime(), restTimeList.get(restCount));
                restCount++;
                this.stats[0] = this.stats[0].increaseNumServed();
            }
        }

        System.out.println(this.stats[0]);
    }

    // for Main5

    public static void run(int numOfServers, int maxQueueLength, int numOfCustomers, int seed, 
        double lambda, double mu, double rho, double probRest, double probGreed) {
        PriorityQueue<Event> priorityQ = new PriorityQueue<>(new EventComp());

        RandomGenerator rg = new RandomGenerator(seed, lambda, mu, rho);

        Server[] servers = new Server[numOfServers];
        for (int i = 0; i < numOfServers; i++) {
            Customer[] customersInstant = new Customer[1];
            servers[i] = new Server(i + 1, customersInstant, maxQueueLength);
        }       

        int custCount = 0;
        double arrivalTime = 0.0;
        List<Customer> customersList = new ArrayList<Customer>();
        for (int i = 0; i < numOfCustomers; i++) {
            custCount++;
            double[] serviceTime = new double[]{0.0};   // place holder for service time
            double custType = rg.genCustomerType();
            customersList.add(new Customer(custCount, arrivalTime, 
                serviceTime, custType < probGreed));
            arrivalTime += rg.genInterArrivalTime();
        }

        List<Double> restTimeList = new ArrayList<Double>();

        for (Customer customer : customersList) {
            priorityQ.add(new Event(customer, customer.getTime()));
        }

        Stats[] stats = new Stats[]{new Stats(0.0,0,0)};

        // run() event
        int restCount = 0;
        while (priorityQ.peek() != null) {
            Event event = priorityQ.poll();
            Customer customer = event.getCustomer();

            for (Server server : servers) {
                if (event.getTime() >= server.getFreeTime()) {
                    server.setRestingStatus(false);
                }
            }

            if (customer.getState() == State.SERVES) {
                Server server = event.getServer();
                double totalServiceTime = customer.getServiceTime();
                for (Customer queueCust : server.getQueue()) {
                    if (queueCust.getDoneTime() <= server.getFreeTime() && queueCust.getID()
                        > customer.getID() && queueCust.getDoneTime() != 0.0) {
                        totalServiceTime += queueCust.getServiceTime();
                    }
                }
                if ((Math.abs(server.getFreeTime() - totalServiceTime - event.getTime())
                    > ZEROVAL) && (server.getFreeTime() > event.getTime())) {
                    Event servesEvent = new Event(customer, server.getFreeTime() - totalServiceTime
                        + customer.getServiceTime(), server);
                    customer.setDoneTime(servesEvent.getTime());
                    server.updateFreeTime(servesEvent.getTime(), customer.getServiceTime());
                    priorityQ.add(servesEvent);
                    continue;
                }
            }
            
            System.out.println(event);

            if (customer.getState() == State.ARRIVES) {
                boolean served = false;

                // Check for idle servers
                for (Server server : servers) {
                    if (server.isNotServingAndWaiting() && server.isResting() == false) {
                        customer.setState(State.SERVES);
                        // Create a new SERVES event
                        Event servesEvent = new Event(customer, customer.getTime(), server);
                        served = true;
                        priorityQ.add(servesEvent);
                        break;
                    }
                }

                // If no idle servers, check for servers with empty queue
                if (!served) {
                    if (customer.getGreedy()) {
                        if (EventRunner.getShortestQ(servers).isNotWaiting()) {
                            customer.setState(State.WAITS);
                            // Create a new WAITS event
                            Event waitsEvent = new Event(customer, customer.getTime(),
                                EventRunner.getShortestQ(servers));
                            served = true;
                            priorityQ.add(waitsEvent);
                        }
                    } else {
                        for (Server server : servers) {
                            if (server.isNotWaiting()) {
                                customer.setState(State.WAITS);
                                // Create a new WAITS event
                                Event waitsEvent = new Event(customer, customer.getTime(), server);
                                served = true;
                                priorityQ.add(waitsEvent);
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
                    priorityQ.add(leavesEvent);
                    stats[0] = stats[0].increaseNumLeft();
                }
            } else if (customer.getState() == State.WAITS) {
                customer.setState(State.SERVES);
                Server server = event.getServer();
                server.addWaiting(customer);
                // Create a new SERVES event
                Event servesEvent = new Event(customer, server.getFreeTime(), server);
                priorityQ.add(servesEvent);
            } else if (customer.getState() == State.SERVES) {
                customer.setState(State.DONE);
                Server server = event.getServer();
                customer.setServiceTime(rg.genServiceTime());
                customer.setDoneTime(event.getTime());
                server.updateFreeTime(event.getTime(), customer.getServiceTime());
                server.setServing(customer);
                stats[0] = stats[0].increaseWaitingTime(event.getTime() - customer.getTime());
                if (server.getWaiting() == customer) {
                    server.removeWaiting();
                }
                // Create a new DONE event
                Event doneEvent = new Event(customer, event.getTime() +
                    customer.getServiceTime(), server);
                server.updateFreeTime(doneEvent.getTime());
                priorityQ.add(doneEvent);
            } else if (customer.getState() == State.DONE) {
                Server server = event.getServer();
                double restProb = rg.genRandomRest();
                if (restProb < probRest) {
                    double restTime = rg.genRestPeriod();
                    restTimeList.add(restTime);
                } else {
                    restTimeList.add(0.0);
                }
                if (restTimeList.get(restCount) > 0.0) {
                    server.setRestingStatus(true);
                } else {
                    server.setRestingStatus(false);
                }
                server.updateServing();
                server.updateFreeTime(event.getTime(), restTimeList.get(restCount));
                restCount++;
                stats[0] = stats[0].increaseNumServed();
            }
        }

        System.out.println(stats[0]);
    }

    public static Server getShortestQ(Server[] servers) {
        Server shortestQ = servers[0];
        for (Server server : servers) {
            if (server.getWaitingRemainingCap() > shortestQ.getWaitingRemainingCap()) {
                shortestQ = server;
            }
        }
        return shortestQ;
    }
}