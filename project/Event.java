class Event {
    private final E e;
    private final int custID;
    private final double time;

    Event(E e, int custID) {
        this.e = e;
        this.custID = custID;
    }
    
    int getCustID() {
        return this.custID;
    }

    public String toString() {
    
    }
}
