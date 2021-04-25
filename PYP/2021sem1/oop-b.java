class B<T> {
    private T other;

    @SuppressWarnings("unchecked")
    void set(T other) {
        this.other = other;
    }

    T get() {
        return this.other;
    }
}