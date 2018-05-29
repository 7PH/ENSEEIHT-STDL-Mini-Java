package tests;

class Thing { }

class Box<T extends Thing> {
    private Thing t;

    public void set(T t) {
        this.t = t;
    }
}