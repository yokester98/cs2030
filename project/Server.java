class Server {
    private final int id;
    private final double availTime;

    Server(int id, double availTime) {
        this.id = id;
        this.availTime = availTime;
    }

    boolean isAvail(Customer customer) {
        return this.availTime <= this.customer.getArrivalTime();
    }
}
