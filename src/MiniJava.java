import java.util.ArrayList;

interface A {
    void doA();
}
interface B<T extends A> {
    T doB(T a);
}

class TheA implements A {
    @Override
    public void doA() {
        String A = "issou";
        System.out.println("A: " + A);
    }
}

class TheB<T extends A> extends TheA implements B<T> {
    @Override
    public T doB(T a) {
        a.doA();
        return a;
    }
}

class TheC<T extends A & B> {
    public void doC (T obj) {
        obj.doA();
        obj.doB(obj);
    }
}

class MiniJava {
    public static void main(String args[]) {
        TheC<TheB> myC = new TheC<>();
        myC.doC(new TheB());
    }
}
