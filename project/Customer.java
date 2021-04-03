class Customer {
    private final int id;
    private final double time;
    private final double serviceTime;

    Customer(int id, double time) {
        this.id = id;
        this.time = time;
        this.serviceTime = 1.0;
    }

    Customer(int id, double time, double serviceTime) {
        this.id = id;
        this.time = time;
        this.serviceTime = serviceTime;
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
}