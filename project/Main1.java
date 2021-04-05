import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class Main1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();
        Server[] servers = new Server[numOfServers];
        for (int i = 0; i < numOfServers; i++) {
            Customer[] customersInstant = new Customer[1];
            servers[i] = new Server(i + 1, customersInstant);
        }       

        int custCount = 0;
        List<Customer> customersList = new ArrayList<Customer>();
        while (sc.hasNextDouble()) {
            custCount++;
            double arrivalTime = sc.nextDouble();
            customersList.add(new Customer(custCount, arrivalTime));
        }

        EventRunner eventRunner = new EventRunner(servers, customersList);
        eventRunner.run();
    }
}