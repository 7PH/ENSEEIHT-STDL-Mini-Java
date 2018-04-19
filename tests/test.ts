import {TAM} from "./TAM";



const testSlow: number = 2000;



describe('# test 1', function () {
    // slow test <=> takes more than 1 second
    this.slow(testSlow);
    this.timeout(10000);

    describe('# test 1.1', function () {
        describe('# test 1.1.1', function() {
            it('-> test 1.1.1.1', function (done: () => any) {
                done();
            });
            it('-> test 1.1.1.2', function (done: () => any) {
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


