import {TAM} from "./TAM";




const SLOW_TEST_MS: number = 2000;


describe('# Grammar tests', function () {
    this.slow(SLOW_TEST_MS);

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


describe('# Resolve/Checktype simple tests', function () {
    this.slow(SLOW_TEST_MS);

    // SIMPLE TEST ABOUT CLASS
    it('-> class', function(done: () => any) {
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

    it('-> class declaration with inner declarations', function(done: () => any) {
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

    // SIMPLE TEST ABOUT INTERFACE
    it('-> interface', function(done: () => any) {
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

    it('-> interface declaration with inner declarations', function(done: () => any) {
        TAM.ensureResult(
            `interface abc {
                int fun();
            }`,
        {
            resolve: true,
            checkType: true
        });
        done();
    });

    it('-> method overloading in interfaces', function(done: () => any) {
        TAM.ensureResult(
            `interface abc {
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

    it('-> forbidden overload 1', function(done: () => any) {
        TAM.ensureResult(
            `interface abc {
                int fun();
                int fun();
            }`,
            { resolve: false, checkType: true });
        done();
    });

    it('-> forbidden overload 2', function(done: () => any) {
        TAM.ensureResult(
            `interface abc {
                int fun(String foo);
                int fun(String bar);
            }`,
            { resolve: false, checkType: true });
        done();
    });

    it('-> forbidden overload 3', function(done: () => any) {
        TAM.ensureResult(
            `interface abc {
                int fun(String foo);
                String fun(String bar);
            }`,
            { resolve: false, checkType: true });
        done();
    });

    it('-> interface implementation', function(done: () => any) {
        TAM.ensureResult(
            `interface abc {}
            class issou implements abc {}`,
        {
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
            `class abc extends abc { }`,
            {
                resolve: false,
                checkType: true
            });
        done();
    });

    it('-> class implementing a class', function(done: () => any) {
        TAM.ensureResult(
            `class foo { } class bar implements foo { }`,
            {
                resolve: true,
                checkType: false
            });
        done();
    });

    it('-> class implementing a class', function(done: () => any) {
        TAM.ensureResult(
            `class foo { } interface bar extends foo { }`,
            {
                resolve: false,
                checkType: true
            });
        done();
    });

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
        TAM.ensureResult(
            `abstract class foo { 
                public abstract void issou();
            } `,
            {
                resolve: true,
                checkType: true
            });
        done();
    });

    it('-> abstract class w/ extented one', function(done: () => any) {
        TAM.ensureResult(
            `abstract class foo { 
                
            } 
            class foo2 extends foo {
            }`,
            {
                resolve: true,
                checkType: true
            });
        done();
    });

    it('-> abstract class w/ extented one w/ unimplemented method', function(done: () => any) {
        TAM.ensureResult(
            `abstract class foo { 
                public abstract void issou();
            } 
            class foo2 extends foo { }
            `,
            {
                resolve: true,
                checkType: false
            });
        done();
    });

    it('-> abstract class w/ extented one w/ implemented method', function(done: () => any) {
        TAM.ensureResult(
            `abstract class foo { 
                public abstract void issou();
            } 
            class foo2 extends foo {
                public void issou() { }
            }`,
            {
                resolve: true,
                checkType: true
            });
        done();
    });
});



describe('# Resolve / CheckType medium tests', function () {
    this.slow(SLOW_TEST_MS);

    it('-> class w/ custom attribute types', function(done: () => any) {
        TAM.ensureResult(`
            class Point {
                private int x;
                private int y;
            }
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

    it('-> class w/ attribute assignment', function(done: () => any) {
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

    it('-> class w/ attribute assignment & ParameterUse in Constructor', function(done: () => any) {
        TAM.ensureResult(`
            class Point {
                private int x;
                private int y;
            }
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


    it('-> class w/ instantiation of our type & This', function(done: () => any) {
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

                public Point(int a, int b) {
                    this.x = a;
                    this.y = b;
                }
            }
            class ColoredPoint extends Point {
                private String color;

                public ColoredPoint(int x, int y) {
                    this.x = x;
                    this.y = y;
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
            
                public Point(int x, int y) {
                    this.x = x;
                    this.y = y;
                }
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

    it('-> class w/ bad constructor 1', function(done: () => any) {
        TAM.ensureResult(`
            class Point {
                private int x;
                private int y;
            
                public Point(int a, int b) {
                    this.x = a;
                    this.y = b;
                }
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

    it('-> class w/ bad constructor 2', function(done: () => any) {
        TAM.ensureResult(`
            class Color {
                public static final String ROUGE = "rouge";
            }
            class Point {
                private int x;
                private int y;

                public Point(int a, int b) {
                    this.x = a;
                    this.y = b;
                }
            }
            class ColoredPoint extends Point {
                private String color;

                public ColoredPoint (Point p1, Point p2, String c) {
                    this.x = p1;
                    this.y = p2;
                    this.color = c;
                }
            }`,
            {
                resolve: false
            });
        done();
    });

    it('-> class w/ superclass attribute use', function(done: () => any) {
        TAM.ensureResult(`
            class Color {
                public static final String ROUGE = "rouge";
            }
            class Point {
                private int x;
                private int y;

                public Point(int x, int y) {
                    this.x = x;
                    this.y = y;
                }
            }
            class ColoredPoint extends Point {
                private String color;

                public ColoredPoint (String color) {
                    this.color = color;
                }
            }`,
            {
                resolve: true,
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

                public Point(int a, int b) {
                    this.x = a;
                    this.y = b;
                }
            }
            class ColoredPoint {
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

    it('-> simple interface implementation', function(done: () => any) {
        TAM.ensureResult(`
            class Point {
                private int x;
                private int y;

                public Point(int a, int b) {
                    this.x = a;
                    this.y = b;
                }
            }

            interface Cercle {
                Point getCenter();	
            }

            class CercleImpl implements Cercle {
                private Point center;
                
                public Point getCenter() {
                    return this.center;
                }
            }
        `,
            {
                resolve: true,
                checkType : true
            });
        done();
    });

    it('-> simple interface implementation with missing methods', function(done: () => any) {
        TAM.ensureResult(`
            class Point {
                private int x;
                private int y;

                public Point(int a, int b) {
                    this.x = a;
                    this.y = b;
                }
            }

            interface Cercle {
                Point getCenter();	
            }

            class CercleImpl implements Cercle {
                private Point center;
            }
        `,
            {
                resolve: true,
                checkType: false
            });
        done();
    });

    it('-> multiple interface implementations 1', function(done: () => any) {
        TAM.ensureResult(`
            interface A { void imA(); }
            interface B { void imB(); }
            class C implements A, B {
                public void imA() { }
                public void imB() { }
            }`,
            {
                resolve: true,
                checkType: true
            });
        done();
    });

    it('-> multiple interface implementations 2', function(done: () => any) {
        TAM.ensureResult(`
            interface A { void imA(); }
            interface B { void imB(); }
            class C implements A, B {
                public void imA() { }
            }`,
            {
                resolve: true,
                checkType: false
            });
        done();
    });

    it('-> multiple interface implementations 3', function(done: () => any) {
        TAM.ensureResult(`
            interface A { void imA(String a); }
            interface B { void imB(); }
            class C implements A, B {
                public void imA(int a) { }
                public void imB() { }
            }`,
            {
                resolve: true,
                checkType: false
            });
        done();
    });

    it('-> multiple interface implementations 4', function(done: () => any) {
        TAM.ensureResult(`
            interface A { void imA(String a); }
            interface B { void imB(); }
            class C implements A, B {
                public void imA(String b) { }
                public void imB() { }
            }`,
            {
                resolve: true,
                checkType: true
            });
        done();
    });

});