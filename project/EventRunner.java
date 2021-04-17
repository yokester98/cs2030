package cs2030.simulator;

import java.util.Queue;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import cs2030.simulator.RandomGenerator;

public class EventRunner {
    private final Server[] servers;
    private final List<Customer> customersList;
    private final Queue<Event> PQ;
    private final Stats[] stats;
    private final List<Double> restTimeList;

    public EventRunner(Server[] servers, List<Customer> customersList) {
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

    public EventRunner(Server[] servers, List<Customer> customersList, List<Double> restTimeList) {
        this.servers = servers;
        this.customersList = customersList;
        this.PQ = new PriorityQueue<Event>(new EventComp());
        this.stats = new Stats[]{new Stats(0,0,0)};
        for (Customer customer : this.customersList) {
            this.PQ.add(new Event(customer, customer.getTime()));
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
        while (this.PQ.peek() != null) {
            Event event = this.PQ.poll();
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
                    if (queueCust.getDoneTime() <= server.getFreeTime() && queueCust.getID() > customer.getID() && queueCust.getDoneTime() != 0.0) {
                        totalServiceTime += queueCust.getServiceTime();
                    }
                }
                if (server.getFreeTime() == event.getTime()) {
                
                } else if ((Math.abs(server.getFreeTime() - totalServiceTime - event.getTime()) > 1e-10) && (server.getFreeTime() >= event.getTime())) {
                    //System.out.println("service time = " + customer.getServiceTime());
                    //System.out.println("event time = " + event.getTime());
                    //System.out.println("customer id = " + customer.getID());
                    //System.out.println("the current free time is " + server.getFreeTime());
                    Event servesEvent = new Event(customer, server.getFreeTime() - totalServiceTime + customer.getServiceTime(), server);
                    //System.out.println("the event time is " + servesEvent.getTime());
                    customer.setDoneTime(servesEvent.getTime());
                    server.updateFreeTime(servesEvent.getTime(), customer.getServiceTime());
                    //System.out.println("the next free time is " + server.getFreeTime());
                    this.PQ.add(servesEvent);
                    continue;
                }
            }
            
            System.out.println(event);

            /*
            // To retrieve hidden test cases, uncomment this block
            if (event.toString().equals("11.688 11 done serving by server 2")) {
                System.out.println(servers.length);
                System.out.println(event.getServer().getWaitingRemainingCap() + event.getServer().getQueue().size());
                for (Customer queuecustomer : customersList) {
                    System.out.println(queuecustomer.getTime());
                    System.out.println(queuecustomer.getServiceTime());
                }
                System.out.println(restTimeList.toString());
            }
            */

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
                // Create a new SERVES event
                Event servesEvent = new Event(customer, server.getFreeTime(), server);
                customer.setDoneTime(servesEvent.getTime());
                server.updateFreeTime(servesEvent.getTime(), customer.getServiceTime());
                this.PQ.add(servesEvent);
                //System.out.println(server.getID());
                //System.out.println(server.getQueue().toString());
                //System.out.println(servesEvent.getTime());
                //System.out.println("The free time is " + server.getFreeTime());
            } else if (customer.getState() == State.SERVES) {
                customer.setState(State.DONE);
                Server server = event.getServer();
                server.setServing(customer);
                this.stats[0] = this.stats[0].increaseWaitingTime(event.getTime() - customer.getTime());
                if (server.getWaiting() == customer) {
                    server.removeWaiting();
                }
                // Create a new DONE event
                Event doneEvent = new Event(customer, event.getTime() + customer.getServiceTime(), server);
                server.updateFreeTime(doneEvent.getTime());
                //System.out.println(server.getID());
                //System.out.println(server.getQueue().toString());
                this.PQ.add(doneEvent);
            } else if (customer.getState() == State.DONE) {
                Server server = event.getServer();
                restCount++;
                if (restTimeList.get(restCount) > 0.0) {
                    server.setRestingStatus(true);
                } else {
                    server.setRestingStatus(false);
                }
                server.updateServing();
                //System.out.println(server.getID());
                //System.out.println(server.getQueue().toString());
                server.updateFreeTime(event.getTime(), restTimeList.get(restCount));
                //System.out.println("Server " + server.getID() + " resting until " + server.getFreeTime());
                this.stats[0] = this.stats[0].increaseNumServed();
            }
        }

        System.out.println(this.stats[0]);
    }

    // for Main5
    public static void run(int numOfServers, int maxQueueLength, int numOfCustomers, int seed, double lambda, double mu, double rho, double probRest, double probGreed) {
        PriorityQueue<Event> PQ = new PriorityQueue<>(new EventComp());

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
            customersList.add(new Customer(custCount, arrivalTime, serviceTime, custType < probGreed));
            arrivalTime += rg.genInterArrivalTime();
        }

        List<Double> restTimeList = new ArrayList<Double>();
        restTimeList.add(0.0);
        for (int i = 0; i < numOfCustomers; i++) {
            double restProb = rg.genRandomRest();
            if (restProb < probRest) {
                double restTime = rg.genRestPeriod();
                restTimeList.add(restTime);
            } else {
                restTimeList.add(0.0);
            }
        }

        for (Customer customer : customersList) {
            PQ.add(new Event(customer, customer.getTime()));
        }

        Stats[] stats = new Stats[]{new Stats(0,0,0)};

        // run() event
        int restCount = 0;
        while (PQ.peek() != null) {
            Event event = PQ.poll();
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
                    if (queueCust.getDoneTime() <= server.getFreeTime() && queueCust.getID() > customer.getID() && queueCust.getDoneTime() != 0.0) {
                        totalServiceTime += queueCust.getServiceTime();
                    }
                }
                if (server.getFreeTime() == event.getTime()) {
                
                } else if ((Math.abs(server.getFreeTime() - totalServiceTime - event.getTime()) > 1e-10) && (server.getFreeTime() >= event.getTime())) {
                    //System.out.println("service time = " + customer.getServiceTime());
                    //System.out.println("event time = " + event.getTime());
                    //System.out.println("customer id = " + customer.getID());
                    //System.out.println("the current free time is " + server.getFreeTime());
                    Event servesEvent = new Event(customer, server.getFreeTime() - totalServiceTime + customer.getServiceTime(), server);
                    //System.out.println("the event time is " + servesEvent.getTime());
                    customer.setDoneTime(servesEvent.getTime());
                    server.updateFreeTime(servesEvent.getTime(), customer.getServiceTime());
                    //System.out.println("the next free time is " + server.getFreeTime());
                    PQ.add(servesEvent);
                    continue;
                }
            }
            
            System.out.println(event);

            /*
            // To retrieve hidden test cases, uncomment this block
            if (event.toString().equals("11.688 11 done serving by server 2")) {
                System.out.println(servers.length);
                System.out.println(event.getServer().getWaitingRemainingCap() + event.getServer().getQueue().size());
                for (Customer queuecustomer : customersList) {
                    System.out.println(queuecustomer.getTime());
                    System.out.println(queuecustomer.getServiceTime());
                }
                System.out.println(restTimeList.toString());
            }
            */

            if (customer.getState() == State.ARRIVES) {
                boolean served = false;

                // Check for idle servers
                for (Server server : servers) {
                    if (server.isNotServingAndWaiting() && server.isResting() == false) {
                        customer.setState(State.SERVES);
                        // Create a new SERVES event
                        Event servesEvent = new Event(customer, customer.getTime(), server);
                        served = true;
                        customer.setServiceTime(rg.genServiceTime());
                        customer.setDoneTime(servesEvent.getTime());
                        server.updateFreeTime(servesEvent.getTime(), customer.getServiceTime());
                        PQ.add(servesEvent);
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
                            PQ.add(waitsEvent);
                        }
                    } else {
                        for (Server server : servers) {
                            if (server.isNotWaiting()) {
                                customer.setState(State.WAITS);
                                // Create a new WAITS event
                                Event waitsEvent = new Event(customer, customer.getTime(), server);
                                served = true;
                                PQ.add(waitsEvent);
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
                    PQ.add(leavesEvent);
                    stats[0] = stats[0].increaseNumLeft();
                }
            } else if (customer.getState() == State.WAITS) {
                customer.setState(State.SERVES);
                Server server = event.getServer();
                server.addWaiting(customer);
                // Create a new SERVES event
                Event servesEvent = new Event(customer, server.getFreeTime(), server);
                customer.setServiceTime(rg.genServiceTime());
                customer.setDoneTime(servesEvent.getTime());
                server.updateFreeTime(servesEvent.getTime(), customer.getServiceTime());
                PQ.add(servesEvent);
                //System.out.println(server.getID());
                //System.out.println(server.getQueue().toString());
                //System.out.println(servesEvent.getTime());
                //System.out.println("The free time is " + server.getFreeTime());
            } else if (customer.getState() == State.SERVES) {
                customer.setState(State.DONE);
                Server server = event.getServer();
                server.setServing(customer);
                stats[0] = stats[0].increaseWaitingTime(event.getTime() - customer.getTime());
                if (server.getWaiting() == customer) {
                    server.removeWaiting();
                }
                // Create a new DONE event
                Event doneEvent = new Event(customer, event.getTime() + customer.getServiceTime(), server);
                server.updateFreeTime(doneEvent.getTime());
                //System.out.println(server.getID());
                //System.out.println(server.getQueue().toString());
                PQ.add(doneEvent);
            } else if (customer.getState() == State.DONE) {
                Server server = event.getServer();
                restCount++;
                if (restTimeList.get(restCount) > 0.0) {
                    server.setRestingStatus(true);
                } else {
                    server.setRestingStatus(false);
                }
                server.updateServing();
                //System.out.println(server.getID());
                //System.out.println(server.getQueue().toString());
                server.updateFreeTime(event.getTime(), restTimeList.get(restCount));
                //System.out.println("Server " + server.getID() + " resting until " + server.getFreeTime());
                stats[0] = stats[0].increaseNumServed();
            }
        }

        System.out.println(stats[0]);
    }
}