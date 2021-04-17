import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class Main2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        Server[] servers = new Server[numOfServers];
        for (int i = 0; i < numOfServers; i++) {
            Customer[] customersInstant = new Customer[1];
            servers[i] = new Server(i + 1, customersInstant, maxQueueLength);
        }       

        int custCount = 0;
        List<Customer> customersList = new ArrayList<Customer>();
        while (sc.hasNextDouble()) {
            custCount++;
            double arrivalTime = sc.nextDouble();
            double serviceTime = sc.nextDouble();
            customersList.add(new Customer(custCount, arrivalTime, serviceTime));
        }

        EventRunner eventRunner = new EventRunner(servers, customersList);
        eventRunner.run();
    }
}