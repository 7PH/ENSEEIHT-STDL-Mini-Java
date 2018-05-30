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
                    checkType: true
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
        it('-> this reference in static context', function(done: () => any) {
            TAM.ensureResult(`
                class Color {
                    public String color;
                    public static void main(String args[]) {
                        this.color = "rouge";
                    }
                }`,
                {
                    resolve: false,
                    checkType: true
                });
            done();
        });
        it('-> call of static method from another class', function(done: () => any) {
            TAM.ensureResult(`
                class ColorRed {
                    public static final String ROUGE = "rouge";
                    public static String getColor() {
                        return this.ROUGE;
                    }
                } 
                class ColorUse {
                    public static void main (String args[]) {
                        ColorRed.getColor();
                    }
                }`,
                {
                    resolve: false,
                    checkType: true
                });
            done();
        });
        it('-> class w/ private method use in inner class', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    public int x;
                    public int y;
                    private int getX() {
                        return this.x;
                    }
                    public void translateX(int a) {
                        this.x = this.getX() + a;
                    }
                }
                `,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> class w/ public method use in inner class', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    public int x;
                    public int y;
                    public int getX() {
                        return this.x;
                    }
                    public void translateX(int a) {
                        this.x = this.getX() + a;
                    }
                }
                `,
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
                    public ColoredPoint(int a, int b, String color) {
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
        it('-> class w/ superclass public method use', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    public int x;
                    public int y;
                    public int getX() {
                        return this.x;
                    }
                }
                class ColoredPoint extends Point {
                    private String color;
                    public ColoredPoint(int a, int b, String color) {
                        this.x = a;
                        this.y = b;
                        this.color = color;
                    }
                    public void translateX(int a) {
                        this.x = this.getX() + a;
                    }
                }`,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
        it('-> class w/ superclass private method use', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    public int x;
                    public int y;
                    private int getX() {
                        return this.x;
                    }
                }
                class ColoredPoint extends Point {
                    private String color;
                    public ColoredPoint(int a, int b, String color) {
                        this.x = a;
                        this.y = b;
                        this.color = color;
                    }
                    public void translateX(int a) {
                        this.x = this.getX() + a;
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

    /* ********************* */
    describe('# About chains', function() {
        it('-> chained abstract method w/ implemented method', function(done: () => any) {
            TAM.ensureResult(`
                abstract class Point { 
                    public abstract void compute();
                }

                abstract class PointNomme extends Point {
                    public abstract void compute();
                }

                class PointNommeColore extends PointNomme {
                    public void compute() {}
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> chained abstract method w/ implemented method II', function(done: () => any) {
            TAM.ensureResult(`
                abstract class Point { 
                    public abstract void compute();
                }

                abstract class PointNomme extends Point {}

                class PointNommeColore extends PointNomme {
                    public void compute() {}
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> chained abstract method w/ implemented method III', function(done: () => any) {
            TAM.ensureResult(`
                abstract class Point { 
                    public abstract void compute();
                }

                abstract class PointNomme extends Point {
                    public abstract void compute();
                    public abstract int getX();
                }

                class PointNommeColore extends PointNomme {
                    public void compute() {}
                    public int getX() {
                        return 5;
                    }
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> chained abstract method w/ unimplemented method', function(done: () => any) {
            TAM.ensureResult(`
                abstract class Point { 
                    public abstract void compute();
                }

                abstract class PointNomme extends Point { }

                class PointNommeColore extends PointNomme {

                }`,
                {
                    resolve: false,
                    checkType : true
                });
            done();
        });
        it('-> chained abstract method w/ unimplemented method II', function(done: () => any) {
            TAM.ensureResult(`
                abstract class Point { 
                    public abstract void compute();
                }

                abstract class PointNomme extends Point {
                    public abstract void compute();
                }

                class PointNommeColore extends PointNomme {
                }`,
                {
                    resolve: false,
                    checkType : true
                });
            done();
        });
        it('-> chained abstract method w/ unimplemented method III', function(done: () => any) {
            TAM.ensureResult(`
                abstract class Point { 
                    public abstract void compute();
                    public abstract int getX();
                }

                abstract class PointNomme extends Point {
                    public void compute() {}

                    public abstract int getX();
                }

                class PointNommeColore extends PointNomme {

                }`,
                {
                    resolve: false,
                    checkType : true
                });
            done();
        });
        it('-> chained abstract method w/ far attribute use', function(done: () => any) {
            TAM.ensureResult(`
                abstract class Point {
                    public int x;
                    public abstract void compute();
                    public abstract int getX();
                }

                abstract class PointNomme extends Point {
                }

                class PointNommeColore extends PointNomme {
                    public void compute() { }
                    public int getX() {
                        return this.x;
                    }
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> chained method ', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;
                    public int getX() {
                        return this.x;
                    }
                    public int getY() {
                        return this.y;
                    }
                    public void translate(int x, int y) {
                        this.x = this.x + x;
                        this.y = this.y + y;
                    }
                }
                class Segment {
                    private Point p1;
                    private Point p2;
                    public Point getP1() {
                        return this.p1;
                    }
                    public Point getP2() {
                        return this.p2;
                    }
                    public void translate(int tau) {
                        this.getP1().translate(tau, tau);
                        this.getP2().translate(tau, tau);
                    }
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> chained method w/ private one', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;
                    public int getX() {
                        return this.x;
                    }
                    public int getY() {
                        return this.y;
                    }
                    private void translate(int x, int y) {
                        this.x = this.x + x;
                        this.y = this.y + y;
                    }
                }
                class Segment {
                    private Point p1;
                    private Point p2;
                    public Point getP1() {
                        return this.p1;
                    }
                    public Point getP2() {
                        return this.p2;
                    }
                    public void translate(int tau) {
                        this.getP1().translate(tau, tau);
                        this.getP2().translate(tau, tau);
                    }
                }`,
                {
                    resolve: false,
                    checkType : true
                });
            done();
        });
        it('-> chained method w/ public attribute use', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    public int x;
                    public int y;
                }
                class Segment {
                    private Point p1;
                    private Point p2;
                    public void translate(int tau) {
                        this.p1.x = this.p1.x + tau;
                        this.p1.y = this.p1.y + tau;
                        this.p2.x = this.p2.x + tau;
                        this.p2.y = this.p2.y + tau;
                    }
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> chained method w/ private attribute use', function(done: () => any) {
            TAM.ensureResult(`
                class Point {
                    private int x;
                    private int y;
                }
                class Segment {
                    private Point p1;
                    private Point p2;
                    public void translate(int tau) {
                        this.p1.x = this.p1.x + tau;
                        this.p1.y = this.p1.y + tau;
                        this.p2.x = this.p2.x + tau;
                        this.p2.y = this.p2.y + tau;
                    }
                }`,
                {
                    resolve: false,
                    checkType : true
                });
            done();
        });
    });
});

/* ###############################################
 * ##     RESOLVE/CHECKTYPE TESTS - PART IV     ##
 * ###############################################
 */
describe('# Resolve / CheckType final tests', function () {
    this.slow(SLOW_TEST_MS);

    /* ********************* */
    it('-> use method interface', function(done: () => any) {
        TAM.ensureResult(`
            interface I1 {
                int getI1();
            }
            interface I2 extends I1 {
                int getI2();
            }
            class C implements I2 {
                public int getI1() {
                    return 1;
                }
                public int getI2() {
                    return 2;
                }
            }
            class Test {
                public static void main(String args[]) {
                    I1 c = new C();
                    System.out.println(c.getI1());
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });
    it('-> arrays', function(done: () => any) {
        TAM.ensureResult(`
            class Test {
                public static void main(String args[]) {
                    int t[] = new int[5];
                    t[3] = 4;
                    System.out.println(t[3]);
                    int ta[] = {1, 2, 3, 4};
                    ta[0] = ta[1];
                    System.out.println(ta[0]);
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });
    it('-> attribute use w/out "this"', function(done: () => any) {
        TAM.ensureResult(`
            class Integer {
                private int in;
                public Integer(int _i) {
                    in = _i;
                }
                public int get() {
                    return in;
                }
            }
            class Test {
                public static void main(String args[]) {
                    Integer i = new Integer(1);
                    System.out.println(i.get());
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });
    it('-> attribute and parameter with same names', function(done: () => any) {
        TAM.ensureResult(`
            class Integer {
                private int in;
                public Integer(int in) {
                    in = in;
                }
                public int get() {
                    return in;
                }
            }
            class Test {
                public static void main(String args[]) {
                    Integer i = new Integer(1);
                    System.out.println(i.get());
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });
    it('-> method use w/out "this"', function(done: () => any) {
        TAM.ensureResult(`
            class Integer {
                private int in;
                public int get() {
                    return in;
                }
                public void test() {
                    int a = get();
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });
    it('-> double generic example', function(done: () => any) {
        TAM.ensureResult(`
            class Box <T, H> {
                private T t;
                private H h;
            
                public Box(T _t, H _h) {
                    this.t = _t;
                    this.h = _h;
                }
            
                public void setT(T _t) {
                    this.t = _t;
                }
            
                public T getT() {
                    return this.t;
                }
            }
            
            class Integer {
                public int value;
            
                public Integer(int value) {
                    this.value = value;
                }
            }
            
            class Test {
                public static void main (String args[]) {
                    Integer a = new Integer(1);
                    Integer b = new Integer(2);
                    Box<Integer, Integer> box = new Box<Integer, Integer>(a, b);
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });
    it('-> double generic example w/ generic method use', function(done: () => any) {
        TAM.ensureResult(`
            class Box <T, H> {
                private T t;
                private H h;
                public Box(T _t, H _h) {
                    this.t = _t;
                    this.h = _h;
                }
                public void setT(T _t) {
                    this.t = _t;
                }
                public T getT() {
                    return this.t;
                }
            }
            class Integer {
                public int value;
                public Integer(int value) {
                    this.value = value;
                }
            }
            class Test {
                public static void main (String args[]) {
                    Integer a = new Integer(1);
                    Integer b = new Integer(2);
                    Box<Integer, Integer> box = new Box<Integer, Integer>(a, b);
                    box.setT(new Integer(3));
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });
    it('-> difficult generic', function(done: () => any) {
        TAM.ensureResult(`
            interface Integer {
                int getInteger();
            }
            class IntegerImpl implements Integer {
                private int i;
                public IntegerImpl(int _i) {
                    this.i = _i;
                }
                public int getInteger() {
                    return this.i;
                }
            }
            class Mask <T extends Integer> {
                private T t;
                public Mask(T _t) {
                    this.t = _t;
                }
                public T getValue() {
                    return this.t;
                }
            }
            class Test {
                public static void main(String args[]) {
                    IntegerImpl ii = new IntegerImpl(1);
                    Mask<Integer> i = new Mask<Integer>(ii);
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });
    it('-> generic w/ extends', function(done: () => any) {
        TAM.ensureResult(`
            class Carton { public int i; }
            class Box <T extends Carton> {
                private T t;
                public Box() { }
            }
            class A extends Carton { }
            class Main {
                public static void main(String args[]) {
                    Box<A> b = new Box<A>();
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });
    it('-> generic w/ missing extends', function(done: () => any) {
        TAM.ensureResult(`
            class Carton { public int i; }
            class Box <T extends Carton> {
                private T t;
                public Box() { }
            }
            class A { }
            class Main {
                public static void main(String args[]) {
                    Box<A> b = new Box<A>();
                }
            }`,
            {
                resolve: true,
                checkType : false
            });
        done();
    });
    it('-> big generic', function(done: () => any) {
        TAM.ensureResult(`
            interface I1 { int getI1(); }
            interface I2 { int getI2(); }
            interface I3 extends I2, I1 { int getI3(); }
            interface I4 extends I1, I3 { int getI4(); }
            class C implements I4 {
                public int getI1() { return 1; }
                public int getI2() { return 2; }
                public int getI3() { return 3; }
                public int getI4() { return 4; }
            }
            class Test {
                public static void main(String args[]) {
                    C c = new C();
                    System.out.println(c.getI1());
                    System.out.println(c.getI2());
                }
            }`,
            {
                resolve: true,
                checkType : true
            });
        done();
    });
    describe('# About wild card', function() {
        it('-> generic w/ wildcard', function(done: () => any) {
            TAM.ensureResult(`
                class Carton {}
                
                class Box <T extends Carton> {
                    private T t;
                    public void set(T _t) { this.t = _t; }
                    public T get() { return this.t; }
                }
                class WildCard {
                    public static void main(String args[]) {
                        Box<?> b = new Box<Carton>();
                    }
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> generic w/ wildcard and object use', function(done: () => any) {
            TAM.ensureResult(`
                class UpperBound {}
                class Generic <T extends UpperBound> {
                    private T t;
                    void write(T t) {
                        this.t = t;
                    }
                    T read() {
                        return t;
                    }
                }
                class Test {
                    public static void main(String args[]) {
                        Generic<UpperBound> concreteTypeReference = new Generic<UpperBound>();
                        Generic<?> wildcardReference = concreteTypeReference;
                        UpperBound ub = wildcardReference.read();
                    }
                }`,
                {
                    resolve: true,
                    checkType : true
                });
            done();
        });
        it('-> generic w/ wildcard and error type', function(done: () => any) {
            TAM.ensureResult(`
                class Object {}
                class UpperBound {}
                class Generic <T extends UpperBound> {
                    private T t;
                    void write(T t) {
                        this.t = t;
                    }
                    T read() {
                        return t;
                    }
                }
                class Test {
                    public static void main(String args[]) {
                        Generic<UpperBound> concreteTypeReference = new Generic<UpperBound>();
                        Generic<?> wildcardReference = concreteTypeReference;
                        UpperBound ub = wildcardReference.read();
                        wildcardReference.write(new Object()); // type error
                    }
                }`,
                {
                    resolve: true,
                    checkType : false
                });
            done();
        });
        it('-> generic w/ wildcard and error type II', function(done: () => any) {
            TAM.ensureResult(`
                class Object {}
                class UpperBound {}
                class Generic <T extends UpperBound> {
                    private T t;
                    void write(T t) {
                        this.t = t;
                    }
                    T read() {
                        return t;
                    }
                }
                class Test {
                    public static void main(String args[]) {
                        Generic<UpperBound> concreteTypeReference = new Generic<UpperBound>();
                        Generic<?> wildcardReference = concreteTypeReference;
                        UpperBound ub = wildcardReference.read();
                        wildcardReference.write(new UpperBound()); // type error
                    }
                }`,
                {
                    resolve: true,
                    checkType : false
                });
            done();
        });
        it('-> generic w/ wildcard and error type II', function(done: () => any) {
            TAM.ensureResult(`
                class Object {}
                class UpperBound {}
                class Generic <T extends UpperBound> {
                    private T t;
                    void write(T t) {
                        this.t = t;
                    }
                    T read() {
                        return t;
                    }
                }
                class Test {
                    public static void main(String args[]) {
                        Generic<UpperBound> concreteTypeReference = new Generic<UpperBound>();
                        Generic<?> wildcardReference = concreteTypeReference;
                        UpperBound ub = wildcardReference.read();
                        concreteTypeReference.write(new UpperBound()); // OK
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
    this.slow(1000);

    describe('# public static void main()', function() {
        it('-> System.out.println(-)', function(done: () => any) {
            TAM.ensureResult(`
                class Main {
                    public static void main () {
                        int a = 1;
                        int b = 2;
                        System.out.println(a + b);
                    }
                }`,
                {
                    resolve: true,
                    checkType : true,
                    output: ["3"]
                });
            done();
        });
    });

    describe('# attributes', function() {
        it('-> object instantiation, attribute access and assignment', function (done: () => any) {
            TAM.ensureResult(`
                class Integer {
                    public int value;
                    public Integer() { }
                }
                
                class Main {
                    public static void main() {
                        Integer integer = new Integer();
                        integer.value = 12;
                        System.out.println(integer.value);
                    }
                }`,
                {
                    resolve: true,
                    checkType: true,
                    output: ["12"]
                });
            done();
        });
        it('-> idem with multiple attributes', function (done: () => any) {
            TAM.ensureResult(`
                class Point {
                    public int x;
                    public int y;
                    public Point() { }
                }
                
                class Main {
                    public static void main() {
                        Point point = new Point();
                        point.x = 1;
                        point.y = 2;
                        System.out.println(point.x);
                        System.out.println(point.y);
                    }
                }`,
                {
                    resolve: true,
                    checkType: true,
                    output: ["1", "2"]
                });
            done();
        });
        it('-> attributes in superclass', function (done: () => any) {
            TAM.ensureResult(`
                class SemiPoint1 {
                    public int x;
                }
                
                class Point extends SemiPoint1 {
                    public int y;
                    public Point() { }
                }
                
                class Main {
                    public static void main() {
                        Point point = new Point();
                        point.x = 1;
                        point.y = 2;
                        System.out.println(point.x);
                        System.out.println(point.y);
                    }
                }`,
                {
                    resolve: true,
                    checkType: true,
                    output: ["1", "2"]
                });
            done();
        });
        it('-> attributes of type Object', function (done: () => any) {
            TAM.ensureResult(`
                class Integer {
                    public int value;
                    public Integer() { }
                }
                
                class Point {
                    public Integer x;
                    public Integer y;
                    public Point() { }
                }
                
                class Main {
                    public static void main() {
                        Point point = new Point();
                        point.x = new Integer();
                        point.x.value = 13;
                        point.y = new Integer();
                        point.y.value = 17;
                        System.out.println(point.x.value);
                        System.out.println(point.y.value);
                    }
                }`,
                {
                    resolve: true,
                    checkType: true,
                    output: ["13", "17"]
                });
            done();
        });
    });
    describe('# methods', function() {
        it('-> method call without parameter', function (done: () => any) {
            TAM.ensureResult(`
                class Person {
                    public void sayHello() {
                        System.out.println("Hello");
                    }
                }
                
                class Main {
                    public static void main() {
                        Person person = new Person();
                        person.sayHello();
                    }
                }`,
                {
                    resolve: true,
                    checkType: true,
                    output: ["\"Hello\""]
                });
            done();
        });
        it('-> method call with multiple atomic type parameters', function (done: () => any) {
            TAM.ensureResult(`
                class Person {
                    public void addForMe(int a, int b) {
                        System.out.println(a + b);
                    }
                }
                
                class Main {
                    public static void main() {
                        Person person = new Person();
                        person.addForMe(10, 23);
                    }
                }`,
                {
                    resolve: true,
                    checkType: true,
                    output: ["33"]
                });
            done();
        });
    });
    /*
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
                checkType: true,
                output: ["6"]
            });
        done();
    }); */ /*
    it('-> extends w/ overloading', function(done: () => any) {
        TAM.ensureResult(`
            class Point {
                public int x;
                public int getX() {
                    return this.x;
                }
                public void setX() {
                    this.x = 5;
                }
            }
            class Point2 extends Point {
                public void setX() {
                    this.x = 10;
                }
                public Point2() {}
                public static void main (String args[]) {
                    Point2 p2 = new Point2();
                    p2.setX();
                    System.out.println(p2.getX());
                }
            }`,
            {
                resolve: true,
                checkType : true,
                output: ["10"]
            });
        done();
    }); */
});