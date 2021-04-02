import java.util.Scanner;
import java.util.PriorityQueue;

class Main1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();
        Server[] servers = new Server[numOfServers];
        for (int i = 0; i < numOfServers; i++) {
            servers[i] = new Server(i + 1, null, null);
        }

        PriorityQueue<Event> PQ = new PriorityQueue<>(new EventComp());        

        int custCount = 0;
        while (sc.hasNextDouble()) {
            custCount++;
            double arrivalTime = sc.nextDouble();
            Customer newCustomer = new Customer(custCount, arrivalTime);
            ArrivalEvent newEvent = new ArrivalEvent(newCustomer, arrivalTime);
            PQ.add(newEvent);
        }

        Stats stats = new Stats(0, 0, 0);

        while (PQ.peek() != null) {
            Event event = PQ.poll();
            System.out.println(event.toString());
            event = event.nextEvent(servers);
            if (event != null) {
                stats = event.updateStats(stats);
                PQ.add(event);
            }
        }
        System.out.println(stats.toString());
    }
}