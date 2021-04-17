package cs2030.simulator;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import cs2030.simulator.RandomGenerator;
import cs2030.simulator.EventRunner;
import cs2030.simulator.Server;
import cs2030.simulator.Customer;

class Main5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        int numOfCustomers = sc.nextInt();
        int seed = sc.nextInt();
        double lambda = sc.nextDouble();
        double mu = sc.nextDouble();
        double rho = sc.nextDouble();
        double probRest = sc.nextDouble();
        double probGreed = sc.nextDouble();

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
            double serviceTime = rg.genServiceTime();
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

        EventRunner eventRunner = new EventRunner(servers, customersList, restTimeList);
        eventRunner.run();
    }
}