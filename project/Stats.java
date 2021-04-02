class Stats {
    private final double waitingTime;
    private final int numServed;
    private final int numLeft;

    Stats(double waitingTime, int numServed, int numLeft){
        this.waitingTime = waitingTime;
        this.numServed = numServed;
        this.numLeft = numLeft;
    }

    Stats increaseWaitingTime(double time) {
        return new Stats(this.waitingTime + time, this.numServed, this.numLeft);
    }

    Stats increaseNumServed() {
        return new Stats(this.waitingTime, this.numServed + 1, this.numLeft);
    }

    Stats increaseNumLeft() {
        return new Stats(this.waitingTime, this.numServed, this.numLeft + 1);
    }

    public String toString() {
        double avgWaitingTime = waitingTime / numServed;
        return String.format("[%.3f %d %d]", avgWaitingTime, numServed, numLeft);
    }
}