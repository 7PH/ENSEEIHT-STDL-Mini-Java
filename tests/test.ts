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


describe('# Resolve/Checktype tests', function () {
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
});