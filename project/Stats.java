class Stats {
    private double waitingTime = 0;
    private int numServed = 0;
    private int numLeft = 0;

    Statistics(){ }

    void increaseWaitingTime(double time) {
        waitingTime += time;
    }

    void increaseNumServed() {
        numServed++;
    }

    void increaseNumLeft() {
        numLeft++;
    }

    public String toString() {
        double avgWaitingTime = waitingTime / numServed;
        return String.format("[%.3f %d %d]", avgWaitingTime, numServed, numLeft);
    }
}