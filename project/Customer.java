class Customer {
    private final int id;
    private final double time;
    private final double serviceTime;
    private final State[] states;
    private final boolean greedy;

    Customer(int id, double time) {
        this.id = id;
        this.time = time;
        this.serviceTime = 1.0;
        this.states = new State[]{State.ARRIVES};
        this.greedy = false;
    }

    Customer(int id, double time, double serviceTime) {
        this.id = id;
        this.time = time;
        this.serviceTime = serviceTime;
        this.states = new State[]{State.ARRIVES};
        this.greedy = false;
    }

    Customer(int id, double time, double serviceTime, boolean greedy) {
        this.id = id;
        this.time = time;
        this.serviceTime = serviceTime;
        this.states = new State[]{State.ARRIVES};
        this.greedy = greedy;
    }

    int getID() {
        return this.id;
    }

    double getTime() {
        return this.time;
    }

    double getServiceTime() {
        return this.serviceTime;
    }

    State getState() {
        return this.states[0];
    }

    boolean getGreedy() {
        return this.greedy;
    }

    void setState(State state) {
        this.states[0] = state;
    }

    public String toString() {
        if (this.greedy) {
            return String.format("%d(greedy) %s", this.id, this.states[0].toString());
        } else {
            return String.format("%d %s", this.id, this.states[0].toString());
        }
    }
}