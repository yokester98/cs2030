import java.util.Scanner;
import java.util.ArrayList;
import java.util.PriorityQueue;

class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();
        
        ArrayList<Customer> customers = new ArrayList<Customer>();
        int id = 1;

        while (sc.hasNextDouble()) {
            double arrivalTime = sc.nextDouble();
            customers.add(new Customer(arrivalTime, id, 0));
            id++;
        }

        PriorityQueue<E> PQ = new PriorityQueue<E>(new EventComp());

        // create Server ArrayList
        ArrayList<Server> servers = new ArrayList<Server>();

        // populate Server ArrayList
        for (int i = 0; i < numOfServers; i++) {
            servers.add(new Server(i + 1, (double) 0.0));
        }

        
    }
}
