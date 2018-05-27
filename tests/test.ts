import {TAM} from "./TAM";

const SLOW_TEST_MS: number = 2000;

/* ###############################################
 * ##               GRAMMAR TESTS               ## 
 * ###############################################
 */
describe('# Grammar tests', function () {
    this.slow(SLOW_TEST_MS);

    /* ********************* */
    describe('# Global declarations', function() {
        it('-> class, interfaces w/ generic types', function (done: () => any) {
            const s: string = `
            class abc { }
            class abc<T> { }
            class abc<A, B> { }
            final class abc { }
            abstract class abc { }
            interface abc1 { }
            interface abc2 extends abc1 { }
            interface abc3 extends abc2<T> { }
            interface abc4 extends abc3<F<D<P>>> { }
            interface abc<T1 extends abc1 & abc2, T2> extends abc2<T2> { }
            `;
            if (! TAM.parse(TAM.storeCodeInTmp(s)).grammarCheck)
                throw new Error("Grammar check should have passed");
            done();
        });
    });

    /* ********************* */
    describe('# Inner class declarations', function() {
        it('-> methods definitions', function (done: () => any) {
            const s: string = `
            class Circle {
                public Circle bar;
                public int bar;
                public Circle() { }
                public Circle foo() { }
                public int foo() { }
                public static int foo() { }
                public static Circle foo() { }
                private static final int foo() { }
                private static final Integer foo() { }
                private final int foo() { }
                private final Integer foo() { }
            }`;
            if (! TAM.parse(TAM.storeCodeInTmp(s)).grammarCheck)
                throw new Error("Grammar check should have passed");
            done();
        });
        it('-> methods parameters', function (done: () => any) {
            const s: string = `
            class Circle<T> {
                public Circle foo() { }
                public Circle foo(int a) { }
                public Circle foo(int a, int b) { }
                public Circle foo(Circle a, Point b) { }
                public Circle foo(Circle<T> a, Point b) { }
            }`;
            if (! TAM.parse(TAM.storeCodeInTmp(s)).grammarCheck)
                throw new Error("Grammar check should have passed");
            done();
        });
    });

    /* ********************* */
    describe('# Inner interface declarations', function() {
        it('-> public methods with different return types and generic types', function (done: () => any) {
            const s: string = `
            interface Map<TK, TV> {
                TV get(TK key);
                void add(TK key, TV value);
            }`;
            if (! TAM.parse(TAM.storeCodeInTmp(s)).grammarCheck)
                throw new Error("Grammar check should have passed");
            done();
        });
    });
});

/* ###############################################
 * ##      RESOLVE/CHECKTYPE TESTS - PART I     ##
 * ###############################################
 */
describe('# Resolve/Checktype simple tests', function () {
    this.slow(SLOW_TEST_MS);

    /* ********************* */
    describe('# About class', function() {
        it('-> one class', function(done: () => any) {
            TAM.ensureResult(
                `class abc {} class foo {}`,
            {
                resolve: true,
                checkType: true
            });
            done();
        });
        it('-> two classes', function(done: () => any) {
            TAM.ensureResult(
                `class abc {} class foo {}`,
            {
                resolve: true,
                checkType: true
            });
            done();
        });
        it('-> duplicate class', function(done: () => any) {
            TAM.ensureResult(
                `class abc {} class abc {}`,
            {
                resolve: false,
                checkType: true
            });
            done();
        });
        it('-> class declaration with inner attributes', function(done: () => any) {
            TAM.ensureResult(
                `class Point {
                    private int x;
                    private int y;
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> class declaration with inner methods', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    public int x() {}
                    private int y() {}
                }`,
                {
                    resolve: true,
                    checkType: false
                });
            done();
        });
    });

    /* ********************* */
    describe('# About interface', function() {
        it('-> one interface', function(done: () => any) {
            TAM.ensureResult(
                `interface abc {}`,
            {
                resolve: true,
                checkType: true
            });
            done();
        });
        it('-> two interfaces', function(done: () => any) {
            TAM.ensureResult(
                `interface abc {} interface foo {}`,
            {
                resolve: true,
                checkType: true
            });
            done();
        });
        it('-> duplicate interface', function(done: () => any) {
            TAM.ensureResult(
                `interface abc {} interface abc {}`,
            {
                resolve: false,
                checkType: true
            });
            done();
        });
        it('-> interface declaration with inner methods', function(done: () => any) {
            TAM.ensureResult(`
                interface abc {
                    int fun();
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
    });

    /* ********************* */
    describe('# About implementation', function() {
        it('-> interface implementation', function(done: () => any) {
            TAM.ensureResult(`
                interface abc {}
                class issou implements abc {}
                `,{
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> unexisting interface implementation', function(done: () => any) {
            TAM.ensureResult(
                `class issou implements abc {}`,
                {
                    resolve: false,
                    checkType: true
                });
            done();
        });
        it('-> self reference', function(done: () => any) {
            TAM.ensureResult(
                `class abc implements abc {}`,
                {
                    resolve: true,
                    checkType: false
                });
            done();
        });
        it('-> class implementing a class', function(done: () => any) {
            TAM.ensureResult(`
                class foo {}
                class bar implements foo {}
                `,{
                    resolve: true,
                    checkType: false
                });
            done();
        });
    });

    /* ********************* */
    describe('# About extension', function() {
        it('-> class extension', function(done: () => any) {
            TAM.ensureResult(`
                class abc {}
                class issou extends abc {}
                `,{
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> unexisting class extension', function(done: () => any) {
            TAM.ensureResult(
                `class issou extends abc {}`,
                {
                    resolve: false,
                    checkType: true
                });
            done();
        });
        it('-> self reference', function(done: () => any) {
        TAM.ensureResult(
            `class abc extends abc {}`,
            {
                resolve: false,
                checkType: true
            });
            done();
        });
        it('-> class extending an interface', function(done: () => any) {
            TAM.ensureResult(`
                interface foo {}
                class bar extends foo {}
                `,{
                    resolve: false,
                    checkType: true
                });
            done();
        });
        it('-> interface extending an interface', function(done: () => any) {
            TAM.ensureResult(`
                interface foo {}
                interface bar extends foo {}
                `,{
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> interface extending a class', function(done: () => any) {
            TAM.ensureResult(`
                class foo {}
                interface bar extends foo {}
                `,{
                    resolve: true,
                    checkType: false
                });
            done();
        });
    });

    /* ********************* */
    describe('# About overloading', function() {
        it('-> method overloading in class', function(done: () => any) {
            TAM.ensureResult(`
                class abc {
                    public void fun() {}
                    public void fun(String param) {}
                    public void fun(String param, int param2) {}
                    public void fun(int param, String param2) {}
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> method overloading in interfaces', function(done: () => any) {
            TAM.ensureResult(`
                interface abc {
                    int fun();
                    int fun(String param);
                    int fun(String param, int param2);
                    int fun(int param, String param2);
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> bad overloading', function(done: () => any) {
            TAM.ensureResult(`
                class abc {
                    public void fun() {}
                    public void fun() {}
                }`,
                {
                    resolve: false,
                    checkType: true
                });
            done();
        });
        it('-> bad overloading II', function(done: () => any) {
            TAM.ensureResult(`
                class abc {
                    public void fun(String foo) {}
                    public void fun(String bar) {}
                }`,
                {
                    resolve: false,
                    checkType: true
                });
            done();
        });
        it('-> bad overloading III', function(done: () => any) {
            TAM.ensureResult(`
                interface abc {
                    int fun(String foo);
                    String fun(String bar);
                }`,
                {
                    resolve: false,
                    checkType: true 
                });
            done();
        });
        it('-> bad overloading IV', function(done: () => any) {
            TAM.ensureResult(`
                class abc {
                    public void fun(String foo) {}
                    private void fun(String bar) {}
                }`,
                {
                    resolve: false,
                    checkType: true 
                });
            done();
        });
    });

    /* ********************* */
    describe('# About abstraction', function() {
        it('-> abstract class ', function(done: () => any) {
            TAM.ensureResult(
                `abstract class foo { } `,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> abstract class w/ abstract method', function(done: () => any) {
            TAM.ensureResult(`
                abstract class foo { 
                    public abstract void issou();
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> abstract class w/ extented one', function(done: () => any) {
            TAM.ensureResult(`
                abstract class foo {} 
                class foo2 extends foo {}
                `,{
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> abstract class w/ extented one w/ unimplemented method', function(done: () => any) {
            TAM.ensureResult(`
                abstract class foo { 
                    public abstract void issou();
                } 
                class foo2 extends foo {
                }`,
                {
                    resolve: false,
                    checkType: true
                });
            done();
        });
        it('-> abstract class w/ extented one w/ implemented method', function(done: () => any) {
            TAM.ensureResult(
                `abstract class foo { 
                    public abstract void issou();
                } 
                class foo2 extends foo {
                    public void issou() {}
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
    });
});


/* ###############################################
 * ##     RESOLVE/CHECKTYPE TESTS - PART II     ##
 * ###############################################
 */
describe('# Resolve / CheckType medium tests', function () {
    this.slow(SLOW_TEST_MS);

    /* ********************* */
    describe('# About object', function() {  
        it('-> class w/ attribute assignment', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;

                    public Point() {
                        this.x = 1;
                        this.y = 2;
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> class w/ attribute assignment w/ parameter use', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;
    
                    public Point(int p1, int p2) {
                        this.x = p1;
                        this.y = p2;
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> class w/ bad named constructor', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;
                }
                class ColoredPoint extends Point {
                    private String color;
                    public Segment(int x, int y, String color) {
                        this.x = x;
                        this.y = y;
                        this.color = color;
                    }
                }`,
                {
                    resolve: false
                });
            done();
        });
    });

    /* ********************* */
    describe('# About method', function() {  
        it('-> correct typed method body', function(done: () => any) {
            TAM.ensureResult(`
                class Random {
                    public int getRandom() {
                        return 5;
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> correct void method body', function(done: () => any) {
            TAM.ensureResult(`
                class Random {
                    public void getRandom() {}
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> uncorrect typed method body', function(done: () => any) {
            TAM.ensureResult(`
                class Random {
                    public int getRandom() {
                        return "d";
                    }
                }`,
                {
                    resolve: true,
                    checkType: false
                });
            done();
        });
        it('-> uncorrect void method body', function(done: () => any) {
            TAM.ensureResult(`
                class Random {
                    public void getRandom() {
                        return "d";
                    }
                }`,
                {
                    resolve: true,
                    checkType: false
                });
            done();
        });
        it('-> public static void main', function(done: () => any) {
            TAM.ensureResult(`
                class Random {
                    public static void main(String args[]) {}
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> correct method body w/ attribute use ', function(done: () => any) {
            TAM.ensureResult(`
                class Random {
                    private int x;
                    public int getRandom() {
                        return this.x;
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> uncorrect method body w/ attribute use ', function(done: () => any) {
            TAM.ensureResult(`
                class Random {
                    private String x;
                    public int getRandom() {
                        return this.x;
                    }
                }`,
                {
                    resolve: true,
                    checkType: false
                });
            done();
        });
        it('-> method call in inner class', function(done: () => any) {
            TAM.ensureResult(`
                class Random {
                    private int fooBar;

                    public void foo () {
                        int b = this.bar();
                    }
                    
                    public int bar() {
                        return this.fooBar;
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> method call in a different class', function(done: () => any) {
            TAM.ensureResult(`
                class Random {
                    private int x;
                
                    public Random(int y) {
                        this.x = y;
                    }
                
                    public int getRandom() {
                        return 5;
                    }
                }
                class Main {
                    public static void main (String args[]) {
                        Random random = new Random(6);
                        int value = random.getRandom();
                        System.out.println(value);
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> method call in a different class w/ use of result', function(done: () => any) {
            TAM.ensureResult(`
                class Random {
                    private int x;
                    public Random(int y) {
                        this.x = y;
                    }
                    public int getRandom() {
                        return 5;
                    }
                }
                class Main {
                    public static void main (String args[]) {
                        Random r = new Random(6);
                        int x = r.getRandom();
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> method call in a different class w/ bad use of result', function(done: () => any) {
            TAM.ensureResult(`
                class Random {
                    private int x;
                    public Random(int y) {
                        this.x = y;
                    }
                    public int getRandom() {
                        return 5;
                    }
                }
                class Main {
                    public static void main (String args[]) {
                        Random r = new Random(6);
                        String x = r.getRandom();
                    }
                }`,
                {
                    resolve: true,
                    checkType: false
                });
            done();
        });
    });

    /* ********************* */
    describe('# About using instantiation', function() {    
        it('-> class w/ custom attribute types', function(done: () => any) {
            TAM.ensureResult(`
                class Point {}
                class Segment {
                    private Point p1;
                    private Point p2;
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> class w/ attribute assignment w/ parameter use', function(done: () => any) {
            TAM.ensureResult(`
                class Point {}
                class Segment {
                    private Point p1;
                    private Point p2;
    
                    public Segment(Point p1, Point p2) {
                        this.p1 = p1;
                        this.p2 = p2;
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> class w/ instantiation of existing object', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;
                    public Point() { }
                }
                class Segment {
                    private Point p1;
                    private Point p2;
    
                    public Segment() {
                        this.p1 = new Point();
                        this.p2 = new Point();
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> class w/ instantiation of unexisting object', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;
                    public Point() { }
                }
                class Segment {
                    private Point p1;
                    private Point p2;
    
                    public Segment() {
                        this.p1 = new ColoredPoint();
                        this.p2 = new ColoredPoint();
                    }
                }`,
                {
                    resolve: false,
                    checkType: true
                });
            done();
        });
        it('-> class w/ object instantiation w/out constructor', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;
                }
                class Segment {
                    private Point p1;
                    private Point p2;
    
                    public Segment() {
                        this.p1 = new Point();
                    }
                }`,
                {
                    resolve: true,
                    checkType: false
                });
            done();
        });
        it('-> class w/ real constructor', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;
    
                    public Point(int a, int b) {
                        this.x = a;
                        this.y = b;
                    }
                }
                class Segment {
                    private Point p1;
                    private Point p2;
    
                    public Segment(int a, int b, int c, int d) {
                        this.p1 = new Point(a, b);
                        this.p2 = new Point(c, d);
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> class w/ instantiation of our type w/ bad type parameter', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;
    
                    public Point(int a, int b) {
                        this.x = a;
                        this.y = b;
                    }
                }
                class Segment {
                    private Point p1;
                    private Point p2;
    
                    public Segment(int a, int b, int c, int d) {
                        this.p1 = new Point(a, b);
                        this.p2 = new Point(c, "d");
                    }
                }`,
                {
                    resolve: true,
                    checkType: false // "d" is String != int
                });
            done();
        });
    });

    /* ********************* */
    describe('# About extension', function() {
        it('-> class w/ superclass attribute use', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    public int x;
                    public int y;
                }
                class ColoredPoint extends Point {
                    private String color;

                    public ColoredPoint (String color) {
                        this.x = 1;
                        this.y = 2;
                        this.color = color;
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
    });

    /* ********************* */
    describe('# About implementation', function() {
        it('-> simple implementation', function(done: () => any) {
            TAM.ensureResult(`
                class Point {}
                interface Cercle {
                    Point getCenter();	
                }
                class CercleImpl implements Cercle {
                    private Point center;
                    public Point getCenter() {
                        return this.center;
                    }
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> simple implementation w/ missing methods', function(done: () => any) {
            TAM.ensureResult(`
                class Point {}
                interface Cercle {
                    Point getCenter();	
                }
                class CercleImpl implements Cercle {
                    private Point center;
                }`,
                {
                    resolve: true,
                    checkType: false
                });
            done();
        });
    });
});

/* ###############################################
 * ##     RESOLVE/CHECKTYPE TESTS - PART III    ##
 * ###############################################
 */
describe('# Resolve / CheckType hard tests', function () {
    this.slow(SLOW_TEST_MS);

    /* ********************* */
    describe('# About modifier', function() { 
        it('-> class w/ constant', function(done: () => any) {
            TAM.ensureResult(`
                class Color {
                    public static final String ROUGE = "rouge";
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> class w/ superclass public attribute use', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    public int x;
                    public int y;
                }
                class ColoredPoint extends Point {
                    private String color;
                    public ColoredPoint(int a, int b) {
                        this.x = a;
                        this.y = b;
                        this.color = "rouge";
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> class w/ superclass private attribute use', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;
                }
                class ColoredPoint extends Point {
                    private String color;
                    public Segment(int a, int b, String color) {
                        this.x = a;
                        this.y = b;
                        this.color = color;
                    }
                }`,
                {
                    resolve: false,
                    checkType: true
                });
            done();
        });
        it('-> class w/ superclass attribute use w/ global constant use', function(done: () => any) {
            TAM.ensureResult(`
                class Color {
                    public static final String ROUGE = "rouge";
                }
                class Point {
                    private int x;
                    private int y;
                }
                class ColoredPoint extends Point{
                    private String color;
    
                    public ColoredPoint () {
                        this.color = Color.ROUGE;
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
    });
    /* ********************* */
    describe('# About generics', function() {
        it('-> basic tests', function(done: () => any) {
            TAM.ensureResult(`
                class A<T> { }
                class B<T1, T2> extends A<T1> { }
                class C { }
                class D { }
                class E<T> extends B<C, D> { }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> generic attribute', function(done: () => any) {
            TAM.ensureResult(`
                class Box<T> {
                    private T t;
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> generic methods', function(done: () => any) {
            TAM.ensureResult(`
                class Box<T> {
                    private T t;
                    public void set(T t) { this.t = t; }
                    public T get() { return this.t; }
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> generic with wrong checktype affectation', function(done: () => any) {
            TAM.ensureResult(`
                class Box<T, X> {
                    private T t;

                    public void set(X t) { this.t = t; }
                }`,
                {
                    resolve: true,
                    checkType : false
                });
            done();
        });
        it('-> generic type extending a class', function(done: () => any) {
            TAM.ensureResult(`
                class Thing { }

                class Box<T extends Thing> {
                    private Thing t;

                    public void set(T t) { 
                        this.t = t; 
                    }
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
    });
});

/* ###############################################
 * ##               TAM CODE TESTS              ##
 * ###############################################
 */
describe('# TAM code', function() {
    it('-> public static void main()', function(done: () => any) {
        TAM.ensureResult(`
            class Main {
                public static void main (String args[]) {
                    int a = 1;
                    int b = 2;
                    System.out.println(a + b);
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });
    it('-> class instantiation and usage (methods+attributes) in a public static void main', function(done: () => any) {
        TAM.ensureResult(`
            class SecretNumber {
                private int number;
            
                public SecretNumber() {
                    this.number = 6;
                }
            
                public int get() {
                    return this.number;
                }
            
                public static void main (String args[]) {
                    SecretNumber secretNumber = new SecretNumber();
                    int number = secretNumber.get();
                    System.out.println(number);
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });

});