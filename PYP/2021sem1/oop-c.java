class C<T> {
    private final T other;

    C() {
        this.other = null;
    }

    C(T newOther) {
        this.other = newOther;
    }

    @SuppressWarnings("unchecked")
    C set(T other) {
        return new C(other);
    }

    T get() {
        return this.other;
    }
}