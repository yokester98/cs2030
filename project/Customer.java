class Customer {
    private final int id;
    private final double time;

    Customer(int id, double time) {
        this.id = id;
        this.time = time;
    }

    int getID() {
        return this.id;
    }

    double getTime() {
        return this.time;
    }

}