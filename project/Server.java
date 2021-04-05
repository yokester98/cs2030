import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

class Server {
    private final int id;
    private final Customer[] servingCustomer;
    private final ArrayBlockingQueue<Customer> queue;
    private final double[] freeTime;

    Server(int id, Customer[] servingCustomer) {
        this.id = id;
        this.servingCustomer = servingCustomer;
        this.queue = new ArrayBlockingQueue<Customer>(1);
        this.freeTime = new double[]{0.0};
    }

    Server(int id, Customer[] servingCustomer, int queueLength) {
        this.id = id;
        this.servingCustomer = servingCustomer;
        this.queue = new ArrayBlockingQueue<Customer>(queueLength);
        this.freeTime = new double[]{0.0};
    }

    int getID() {
        return this.id;
    }

    double[] getFreeTime() {
        return this.freeTime;
    }

    Customer getServing() {
        return this.servingCustomer[0];
    }

    ArrayBlockingQueue<Customer> getQueue() {
        return this.queue;
    }

    Customer getWaiting() {
        return this.getQueue().peek();
    }

    int getWaitingRemainingCap() {
        return this.getQueue().remainingCapacity();
    }

    void setServing(Customer customer) {
        this.servingCustomer[0] = customer;
    }

    void setServing(Customer customer, double eventTime) {
        this.servingCustomer[0] = customer;
        this.freeTime[0] = eventTime + customer.getServiceTime();
    }

    Customer removeWaiting() {
        return this.getQueue().poll();
    }

    void addWaiting(Customer customer) {
        this.getQueue().add(customer);
    }

    boolean isNotServingAndWaiting() {
        return (this.getServing() == null && this.getWaitingRemainingCap() != 0);
    }

    boolean isNotWaiting() {
        return (this.getServing() != null && this.getWaitingRemainingCap() != 0);
    }

    void updateServing() {
        if (this.getServing() != null) {
            this.setServing(null);
        }
        if (this.getWaitingRemainingCap() != 0) {
            this.setServing(this.removeWaiting());
        }
    }

    double getNextFreeTime() {
        return this.freeTime[0];
    }
}