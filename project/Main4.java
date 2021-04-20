import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import cs2030.simulator.Server;
import cs2030.simulator.Customer;
import cs2030.simulator.EventRunner;

class Main4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        int numOfCustomers = sc.nextInt();
        Server[] servers = new Server[numOfServers];
        for (int i = 0; i < numOfServers; i++) {
            Customer[] customersInstant = new Customer[1];
            servers[i] = new Server(i + 1, customersInstant, maxQueueLength);
        }       

        int custCount = 0;
        List<Customer> customersList = new ArrayList<Customer>();
        for (int i = 0; i < numOfCustomers; i++) {
            custCount++;
            double arrivalTime = sc.nextDouble();
            double serviceTime = sc.nextDouble();
            customersList.add(new Customer(custCount, arrivalTime, new double[]{serviceTime}));
        }

        List<Double> restTimeList = new ArrayList<Double>();
        for (int i = 0; i < numOfCustomers; i++) {
            double restTime = sc.nextDouble();
            restTimeList.add(restTime);
        }

        EventRunner eventRunner = new EventRunner(servers, customersList, restTimeList);
        eventRunner.run();
    }
}