A a1 = new A();
A a2 = new A();
a1.set(a2);
a2.set(a1);
a1.get().equals(a2);
a2.get().equals(a1);
