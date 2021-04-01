class EventManager {
    private final E e;
    private final int custID;
    private final double time;

    Event(E e, int custID, double time) {
        this.e = e;
        this.custID = custID;
        this.time = time;
    }
    
    int getCustID() {
        return this.custID;
    }

    public String toString() {
        return String.format("%d %d %s")
    }
}
