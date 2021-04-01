import java.util.Scanner;
import java.util.PriorityQueue;

class Main1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();
        Server[] servers = new Server[numOfServers];
        for (int i = 0; i < numOfServers; i++) {
            servers[i] = new Server(i + 1)
        }

        PriorityQueue<Event> PQ = new PriorityQueue<>(new EventComp());        

        while (sc.hasNextDouble()) {
            double arrivalTime = sc.nextDouble();
        }
    }
}
