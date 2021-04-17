package cs2030.simulator;

public class Customer {
    private final int id;
    private final double time;
    private final double[] serviceTime;
    private final State[] states;
    private final boolean greedy;
    private final double[] doneTime;

    public Customer(int id, double time) {
        this.id = id;
        this.time = time;
        this.serviceTime = new double[]{1.0};
        this.states = new State[]{State.ARRIVES};
        this.greedy = false;
        this.doneTime = new double[]{0.0};
    }

    public Customer(int id, double time, double[] serviceTime) {
        this.id = id;
        this.time = time;
        this.serviceTime = serviceTime;
        this.states = new State[]{State.ARRIVES};
        this.greedy = false;
        this.doneTime = new double[]{0.0};
    }

    public Customer(int id, double time, double[] serviceTime, boolean greedy) {
        this.id = id;
        this.time = time;
        this.serviceTime = serviceTime;
        this.states = new State[]{State.ARRIVES};
        this.greedy = greedy;
        this.doneTime = new double[]{0.0};
    }

    public int getID() {
        return this.id;
    }

    public double getTime() {
        return this.time;
    }

    public double getServiceTime() {
        return this.serviceTime[0];
    }

    public State getState() {
        return this.states[0];
    }

    public boolean getGreedy() {
        return this.greedy;
    }

    public double getDoneTime() {
        return this.doneTime[0];
    }

    public void setState(State state) {
        this.states[0] = state;
    }

    public void setServiceTime(double serviceTime) {
        this.serviceTime[0] = serviceTime;
    }

    public void setDoneTime(double eventTime) {
        this.doneTime[0] = eventTime + this.getServiceTime();
    }

    public String toString() {
        if (this.greedy) {
            return String.format("%d(greedy) %s", this.id, this.states[0].toString());
        } else {
            return String.format("%d %s", this.id, this.states[0].toString());
        }
    }
}