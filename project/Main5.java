import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import cs2030.simulator.EventRunner;

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

        EventRunner.run(numOfServers, maxQueueLength, numOfCustomers, seed, lambda, mu, rho, probRest, probGreed);
    }
}