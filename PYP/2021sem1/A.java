class A<T> {
    private final T other;

    A() {
        this.other = null;
    }

    A(T newOther) {
        this.other = newOther;
    }

    A set(T other) {
        return new A(other);
    }

    T get() {
        return this.other;
    }
}