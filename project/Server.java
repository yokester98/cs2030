import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

class Server {
    private final int id;
    private final Customer[] servingCustomer;
    private final ArrayBlockingQueue<Customer> queue;
    private final double[] freeTime;
    private final boolean[] resting;
    private final double[] doneTime;

    Server(int id, Customer[] servingCustomer) {
        this.id = id;
        this.servingCustomer = servingCustomer;
        this.queue = new ArrayBlockingQueue<Customer>(1);
        this.freeTime = new double[]{0.0};
        this.resting = new boolean[]{false};
        this.doneTime = new double[]{0.0};
    }

    Server(int id, Customer[] servingCustomer, int queueLength) {
        this.id = id;
        this.servingCustomer = servingCustomer;
        this.queue = new ArrayBlockingQueue<Customer>(queueLength);
        this.freeTime = new double[]{0.0};
        this.resting = new boolean[]{false};
        this.doneTime = new double[]{0.0};
    }

    int getID() {
        return this.id;
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

    boolean isResting() {
        return this.resting[0];
    }

    double getDoneTime() {
        return this.doneTime[0];
    }

    void setRestingStatus(boolean bool) {
        this.resting[0] = bool;
    }

    void setServing(Customer customer) {
        this.servingCustomer[0] = customer;
    }

    void updateFreeTime(double eventTime, double restTime, double serviceTime) {
        this.freeTime[0] = eventTime + restTime + serviceTime;
    }

    void updateDoneTime(double eventTime, double serviceTime) {
        this.doneTime[0] = eventTime + serviceTime;
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

    double getFreeTime() {
        return this.freeTime[0];
    }
}