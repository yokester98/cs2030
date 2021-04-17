package cs2030.simulator;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Server {
    private final int id;
    private final Customer[] servingCustomer;
    private final ArrayBlockingQueue<Customer> queue;
    private final double[] freeTime;
    private final boolean[] resting;
    private final double[] doneTime;

    public Server(int id, Customer[] servingCustomer) {
        this.id = id;
        this.servingCustomer = servingCustomer;
        this.queue = new ArrayBlockingQueue<Customer>(1);
        this.freeTime = new double[]{0.0};
        this.resting = new boolean[]{false};
        this.doneTime = new double[]{0.0};
    }

    public Server(int id, Customer[] servingCustomer, int queueLength) {
        this.id = id;
        this.servingCustomer = servingCustomer;
        this.queue = new ArrayBlockingQueue<Customer>(queueLength);
        this.freeTime = new double[]{0.0};
        this.resting = new boolean[]{false};
        this.doneTime = new double[]{0.0};
    }

    public int getID() {
        return this.id;
    }

    public Customer getServing() {
        return this.servingCustomer[0];
    }

    public ArrayBlockingQueue<Customer> getQueue() {
        return this.queue;
    }

    public Customer getWaiting() {
        return this.getQueue().peek();
    }

    public int getWaitingRemainingCap() {
        return this.getQueue().remainingCapacity();
    }

    public boolean isResting() {
        return this.resting[0];
    }

    public double getDoneTime() {
        return this.doneTime[0];
    }

    public void setRestingStatus(boolean bool) {
        this.resting[0] = bool;
    }

    public void setServing(Customer customer) {
        this.servingCustomer[0] = customer;
    }

    public void updateFreeTime(double eventTime) {
        this.freeTime[0] = eventTime;
    }

    public void updateFreeTime(double eventTime, double restTime) {
        this.freeTime[0] = eventTime + restTime;
    }

    public void updateFreeTime(double eventTime, double restTime, double serviceTime) {
        this.freeTime[0] = eventTime + restTime + serviceTime;
    }

    public void updateDoneTime(double eventTime, double serviceTime) {
        this.doneTime[0] = eventTime + serviceTime;
    }

    public Customer removeWaiting() {
        return this.getQueue().poll();
    }

    public void addWaiting(Customer customer) {
        this.getQueue().add(customer);
    }

    public boolean isNotServingAndWaiting() {
        return (this.getServing() == null && this.getWaitingRemainingCap() != 0);
    }

    public boolean isNotWaiting() {
        return (this.getServing() != null && this.getWaitingRemainingCap() != 0);
    }

    public void updateServing() {
        if (this.getServing() != null) {
            this.setServing(null);
        }
        if (this.getWaitingRemainingCap() != 0) {
            this.setServing(this.getWaiting());
        }
    }

    public double getFreeTime() {
        return this.freeTime[0];
    }
}