import {TAM} from "./TAM";



const testSlow: number = 2000;



describe('# test 1', function () {
    // slow test <=> takes more than 1 second
    this.slow(testSlow);
    this.timeout(10000);

    describe('# Grammar rules', function () {
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
                done();
            });
        });

        describe('# Inner class declarations', function() {
            it('-> working example', function (done: () => any) {
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
                done();
            });
        });

        describe('# Inner interface declarations', function() {
            it('-> public methods with different return types and generic types', function (done: () => any) {
                const s: string = `
                interface Map<TK, TV> {
                    public TV get(TK key);
                    public void add(TK key, TV value);
                }`;
                done();
            });
            it('-> private methods', function (done: () => any) {
                const s: string = `
                interface Stuff {
                    private void doStuff();
                }`;
                done();
            });
        });


        describe('# test 1.1.2', function () {
            it('-> test 1.1.2.1', function (done: () => any) {
                done();
            });
            it('-> test 1.1.2.2', function (done: () => any) {
                done();
            });
        });
    });

    describe('# test 1.2', function() {
        it("-> test 1.2.1", function(done: () => any) {
            done();
        });
        it("-> test 1.2.2", function(done: () => any) {
            done();
        });
    });
});


describe('# test 2', function() {

    // slow test <=> takes more than 500ms
    this.slow(500);
    this.timeout(10000);


    describe('# test 2.1', function () {
        it('-> test 2.1.1', function (done: () => any) {
            done();
        });
    });

    describe('# test 2.2', function () {
        it('-> test 2.2.1', function (done: () => any) {
            done();
        });
    });
});


