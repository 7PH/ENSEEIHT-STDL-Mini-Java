import {TAM} from "./TAM";



const testSlow: number = 2000;



describe('getType()', function () {
    // slow test <=> takes more than 1 second
    this.slow(testSlow);
    this.timeout(10000);

    describe('# AtomicType', function () {
        describe('# Declaration', function() {

            it('IntegerType (invalid - 4 tests)', function (done: () => any) {
                this.slow(testSlow * 4);
                TAM.ensureResult(`int a = "1";`, {resolve: true, checkType: false});
                TAM.ensureResult(`int a = "1";`, {resolve: true, checkType: false});
                TAM.ensureResult(`int a = '1';`, {resolve: true, checkType: false});
                TAM.ensureResult(`int a = true;`, {resolve: true, checkType: false});
                TAM.ensureResult(`int a = {1};`, {resolve: true, checkType: false});
                done();
            });

            it('CharacterType (invalid - 4 tests)', function (done: () => any) {
                this.slow(testSlow * 4);
                TAM.ensureResult(`character a = "c";`, {resolve: true, checkType: false});
                TAM.ensureResult(`character a = 1;`, {resolve: true, checkType: false});
                TAM.ensureResult(`character a = true;`, {resolve: true, checkType: false});
                TAM.ensureResult(`character a = {'c'};`, {resolve: true, checkType: false});
                done();
            });

            it('StringType (invalid - 4 tests)', function (done: () => any) {
                this.slow(testSlow * 4);
                TAM.ensureResult(`String a = 'H';`, {resolve: true, checkType: false});
                TAM.ensureResult(`String a = 1;`, {resolve: true, checkType: false});
                TAM.ensureResult(`String a = true;`, {resolve: true, checkType: false});
                TAM.ensureResult(`String a = {"Hello"};`, {resolve: true, checkType: false});
                done();
            });

            it('BooleanType (invalid - 4 tests)', function (done: () => any) {
                this.slow(testSlow * 4);
                TAM.ensureResult(`boolean a = '1';`, {resolve: true, checkType: false});
                TAM.ensureResult(`boolean a = 1;`, {resolve: true, checkType: false});
                TAM.ensureResult(`boolean a = "true";`, {resolve: true, checkType: false});
                TAM.ensureResult(`boolean a = {true};`, {resolve: true, checkType: false});
                done();
            });

            it('CoupleType (invalid - 4 tests)', function (done: () => any) {
                this.slow(testSlow * 4);
                TAM.ensureResult(`<int, int> a = <"a", 1>;`, {resolve: true, checkType: false});
                TAM.ensureResult(`<<int, boolean>, character> a = <<1, false>, 3>;`, {resolve: true, checkType: false});
                TAM.ensureResult(`int b = 1; <int, boolean> a = <1, b>;`, {resolve: true, checkType: false});
                TAM.ensureResult(`<int, int> a = {1, 1};`, {resolve: true, checkType: false});
                done();
            });
        });


        describe('# Assignment', function () {
            it('IntegerType (invalid - 4 tests)', function (done: () => any) {
                this.slow(testSlow * 4);
                TAM.ensureResult(`int a = 0; a = "1";`, {resolve: true, checkType: false});
                TAM.ensureResult(`int a = 0; a = '1';`, {resolve: true, checkType: false});
                TAM.ensureResult(`int a = 0; a = true;`, {resolve: true, checkType: false});
                TAM.ensureResult(`int a = 0; a = {1};`, {resolve: true, checkType: false});
                done();
            });

            it('CharacterType (invalid - 4 tests)', function (done: () => any) {
                this.slow(testSlow * 4);
                TAM.ensureResult(`character a = 'a'; a = "c";`, {resolve: true, checkType: false});
                TAM.ensureResult(`character a = 'a'; a = 1;`, {resolve: true, checkType: false});
                TAM.ensureResult(`character a = 'a'; a = true;`, {resolve: true, checkType: false});
                TAM.ensureResult(`character a = 'a'; a = {'c'};`, {resolve: true, checkType: false});
                done();
            });

            it('StringType (invalid - 4 tests)', function (done: () => any) {
                this.slow(testSlow * 4);
                TAM.ensureResult(`String a = "a"; a = 'H';`, {resolve: true, checkType: false});
                TAM.ensureResult(`String a = "a"; a = 1;`, {resolve: true, checkType: false});
                TAM.ensureResult(`String a = "a"; a = true;`, {resolve: true, checkType: false});
                TAM.ensureResult(`String a = "a"; a = {"HH"};`, {resolve: true, checkType: false});
                done();
            });

            it('BooleanType (invalid - 4 tests)', function (done: () => any) {
                this.slow(testSlow * 4);
                TAM.ensureResult(`boolean a = true; a = '1';`, {resolve: true, checkType: false});
                TAM.ensureResult(`boolean a = true; a = 1;`, {resolve: true, checkType: false});
                TAM.ensureResult(`boolean a = true; a = "true";`, {resolve: true, checkType: false});
                TAM.ensureResult(`boolean a = true; a = {true};`, {resolve: true, checkType: false});
                done();
            });

            it('CoupleType (invalid - 4 tests)', function (done: () => any) {
                this.slow(testSlow * 4);
                TAM.ensureResult(`<int, int> a = <0, 0>; a = <"a", 1>;`, {resolve: true, checkType: false});
                TAM.ensureResult(`<<int, boolean>, character> a = <<1, true>, 2>; a = <<1, false>, 3>;`, {resolve: true, checkType: false});
                TAM.ensureResult(`int b = 1; <int, boolean> a = <0, true>; a = <1, b>;`, {resolve: true, checkType: false});
                TAM.ensureResult(`<int, int> a = <0, 0>; a = {1, 2};`, {resolve: true, checkType: false});
                done();
            });

            it('RecordType (invalid - 4 tests)', function (done: () => any) {
                this.slow(testSlow * 4);
                TAM.ensureResult(`struct A{int x; int y;} a = {0, 0}; a = {1, 'a'};`, {resolve: true, checkType: false});
                TAM.ensureResult(`struct A{boolean x; int y;} a = {false, 0}; a = {1, true};`, {resolve: true, checkType: false});
                TAM.ensureResult(`struct A{int x; int y;} a = {0, 0}; a = {true, 0};`, {resolve: true, checkType: false});
                TAM.ensureResult(`struct A{int x; int y;} a = {0, 0}; a = {{1, 0}};`, {resolve: true, checkType: false});
                done();
            });
        });
    });

    describe('# RecordType', function() {
        it("wrong assignement 1", function(done: () => any) {
            TAM.ensureResult(`struct A{int x; int y;} a = {1, 'a'};`, {resolve: true, checkType: false});
            done();
        });
        it("wrong assignement 2", function(done: () => any) {
            TAM.ensureResult(`struct A{boolean x; int y;} a = {1, true};`, {resolve: true, checkType: false});
            done();
        });
        it("wrong assignement 3", function(done: () => any) {
            TAM.ensureResult(`struct A{int x; int y;} a = {true, 0};`, {resolve: true, checkType: false});
            done();
        });
        it("struct containing struct", function(done: () => any) {
            TAM.ensureResult(`
                    typedef struct SPoint {int x; int y;} Point;
                    struct Circle {Point center; int radius;} circle = {{0, 1}, 10};
                `, {resolve: true, checkType: true});
            done();
        });
        it("struct invalid access", function(done: () => any) {
            TAM.ensureResult(`
                    typedef struct SPoint {int x; int y;} Point;
                    Point point = {1, 2};
                    int a = point.foo;
                `, {resolve: false, checkType: false});
            done();
        });
        it("struct access with invalid type", function(done: () => any) {
            TAM.ensureResult(`
                    typedef struct SPoint {int x; int y;} Point;
                    Point point = {1, 2};
                    character a = point.x;
                `, {resolve: true, checkType: false});
            done();
        });
        it("struct valid access", function(done: () => any) {
            TAM.ensureResult(`
                    typedef struct SPoint {int x; int y;} Point;
                    Point point = {1, 2};
                    int a = point.x;
                `, {resolve: true, checkType: true});
            done();
        });
    });

    describe('# Constant', function() {
        it('constant re-assignement', function(done: () => any) {
            TAM.ensureResult(
                `const String s = "Hello"; s = "World";`,
                {resolve: false, checkType: false});
            done();
        });
        it('array value re-assignement', function(done: () => any) {
            TAM.ensureResult(
                `const int s[] = new int[10]; s[0] = 1;`,
                {resolve: false, checkType: false});
            done();
        });
    });

    describe('# NamedType', function() {
        it('typedef of atomic types (2 tests)', function (done: () => any) {
            this.slow(testSlow * 2);
            TAM.ensureResult(`typedef character theChar; theChar c = 42;`, {resolve: true, checkType: false});
            TAM.ensureResult(`typedef character theChar; theChar c = 'c';`, {resolve: true, checkType: true});
            done();
        });
        it('typedef of struct - wrong assignement 1', function (done: () => any) {
            TAM.ensureResult(`
                typedef struct SPoint {int x; int y;} Point;
                Point point = {1, 2, 3};
            `, {
                resolve: true,
                checkType: false
            });
            done();
        });
        it('typedef of struct - wrong assignement 2', function (done: () => any) {
            TAM.ensureResult(`
                typedef struct SPoint {int x; int y;} Point;
                Point point = {1};
            `, {
                resolve: true,
                checkType: false
            });
            done();
        });
        it('typedef of struct - wrong assignement 3', function (done: () => any) {
            TAM.ensureResult(`
                typedef struct SPoint {int x; int y;} Point;
                Point point = {'c', 1};
            `, {
                resolve: true,
                checkType: false
            });
            done();
        });
        it('typedef of struct', function (done: () => any) {
            TAM.ensureResult(`
                typedef struct SPoint {int x; int y;} Point;
                Point point = {3, 1};
            `, {
                resolve: true,
                checkType: true
            });
            done();
        });
        it('typedef of typedef of typedef of atomic', function (done: () => any) {
            TAM.ensureResult(`
                typedef int myInt;
                typedef myInt theInt;
                typedef theInt foo;
                foo a = 2;
                print a;
            `, {
                resolve: true,
                checkType: true
            });
            done();
        });
        it('if it quacks like a duck, it\'s a duck (Duck test)', function (done: () => any) {
            TAM.ensureResult(`
                typedef struct SPoint {int x; int y;} Point;
                typedef struct SVector {int x; int y;} Vector;
                typedef struct SCircle {int x; int y; int radius;} Circle;
                Point p1 = {1, 2};
                Point p2 = {3, 4};
                Vector v = {10, 20};
                p1 = v;
                v = p2;
            `, {
                resolve: true,
                checkType: true
            });
            done();
        });
        it('but if it does not then it\'s not', function (done: () => any) {
            TAM.ensureResult(`
                typedef struct SPoint {int x; int y;} Point;
                typedef struct SVector {int x; int y;} Vector;
                typedef struct SCircle {int x; int y; int radius;} Circle;
                Point p1 = {1, 2};
                Circle c = p1;
            `, {
                resolve: true,
                checkType: false
            });
            done();
        });
        it('typedef of typedef of typedef of..', function (done: () => any) {
            TAM.ensureResult(`
                typedef struct SPoint {int x; int y;} Point1;
                typedef Point1 Point2;
                typedef Point2 Point3;
                Point1 p1 = {1, 2};
                Point2 p2 = {3, 4};
                Point3 p3 = {5, 6};
                p1.x = p2.y;
                p2 = p3;
                p3 = p1;
            `, {
                resolve: true,
                checkType: true
            });
            done();
        });
    });

    describe('# PointerType', function() {
        it('wrong assignement', function(done: () => any) {
            TAM.ensureResult(`
                character a = 'c';
                int *ptr = &a;
                `, {resolve: true, checkType: false});
            done();
        });
        it('reference getter', function(done: () => any) {
            TAM.ensureResult(`
                int a = 1;
                int b = 2;
                int c = 3;
                int *ptr = &b;
                *ptr = 42;
                `,
                {resolve: true, checkType: true});
            done();
        });
        it('basic pointer declaration and assignement', function(done: () => any) {
            TAM.ensureResult(`int *ptr = new int(); *ptr = 1;`, {resolve: true, checkType: true});
            done();
        });
        it('basic pointer', function(done: () => any) {
            TAM.ensureResult(`int *ptr = new int(); *ptr = 1;`, {resolve: true, checkType: true});
            done();
        });
        it('pointer of named type -> record type', function(done: () => any) {
            TAM.ensureResult(`
                typedef <int, int> Point;
                Point *ptr = new Point();
                *ptr = <1, 2>;
                int a = fst (*ptr);
                int b = snd (*ptr);
                print a;
                print b;
                `, {resolve: true, checkType: true});
            done();
        });
    });

    describe('# Function', function () {
        it('Wrong return type 1', function (done: () => any) {
            TAM.ensureResult(`int f() { int a = 1; if (a > 0) { return 1; } else { return 'a'; } }`, {resolve: true, checkType: false});
            done();
        });
        it('Wrong return type 2', function (done: () => any) {
            TAM.ensureResult(`int f() { int a = 1; if (a > 0) { return 'a'; } else { return 1; } }`, {resolve: true, checkType: false});
            done();
        });
        it('Function returning a NamedType', function (done: () => any) {
            TAM.ensureResult(`
                    typedef struct SPoint {int x; int y;} Point;
                    Point f(int a) {
                        return {a, a};
                    }
                `, {resolve: true, checkType: true});
            done();
        });
        it('Unreachable statement', function (done: () => any) {
            TAM.ensureResult(`int f() { return 1; return 2; }`, {resolve: false, checkType: false});
            done();
        });
        it('Unreachable statement and wrong return type', function (done: () => any) {
            TAM.ensureResult(`int f() { return 1; return 'a'; }`, {resolve: false, checkType: false});
            done();
        });
    });

    describe('# If', function () {
        it('if ({not boolean}) { .. }', function (done: () => any) {
            TAM.ensureResult(`if (1) { }`, {resolve: true, checkType: false});
            done();
        });
    });

    describe('# While', function () {
        it('while ({not boolean}) { .. }', function (done: () => any) {
            TAM.ensureResult(`while (1) { }`, {resolve: true, checkType: false});
            done();
        });
    });

    describe('# RootBlock', function () {
        it('no return in root block', function (done: () => any) {
            TAM.ensureResult(`return 1;`, {resolve: true, checkType: false});
            done();
        });
    });

    describe('# Valid examples', function() {
        it('full basic example (valid)', function (done: () => any) {
            TAM.ensureResult(`
                // com 1
                <<int, boolean>, character> hello = <<1, false>, 'a'>;
                typedef struct SB {boolean active;} B;
                struct A{int x; int y; B target;} world = {-1, 1, {true}};
                int a = 6;
                boolean even = (a + 1) % 2 == 0;
                int i = 1;
                character g = 'a';
                const int j = 2;
                <int, int> p = <3, 4>;
                int k = fst p;
                
                /* com 2 */
                if (i >= 2) {
                    i = i - i * 2;
                }
            
                if (i < 5) {
                    int j = 5;
                    j = i * (snd p);
                    i = j + 1;
            
                    while (k < 10) {
                        int p = 3;
                        k = k + 1;
                    }
            
                } else {
            
                    if (i + j < 10) {
                        const boolean p = false;
                        print p;
                    }
                    print fst p;
                }
            
                print j;
            `,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });

        it('complex example (valid)', function (done: () => any) {
            TAM.ensureResult(`
                typedef <int, int> Point;
                typedef struct SCircle {<int, int> center; int radius;} Circle;
                Circle circle = {<-1, 1>, 2};
            `,
                {
                    resolve: true,
                    checkType: true
                });
            done();
        });
    });
});


describe('execute()', function() {

    // slow test <=> takes more than 500ms
    this.slow(500);
    this.timeout(10000);


    describe('# Printer', function () {
        it('print {variable:int}', function (done: () => any) {
            TAM.ensureResult(`
                int a = 10;
                int b = 20;
                print b;
                print a;
                print b;
            `,
                {
                    resolve: true,
                    checkType: true,
                    output: ['20', '10', '20']
                });
            done();
        });
        it('print {constant:int}', function (done: () => any) {
            TAM.ensureResult(`
                const int a = -1;
                print a;
            `, {
                resolve: true,
                checkType: true,
                output: ['-1']
            });
            done();
        });
        it('print {character}', function (done: () => any) {
            TAM.ensureResult(`
                const character a = 'c';
                print a;
            `, {
                resolve: true,
                checkType: true,
                output: ['\'c\'']
            });
            done();
        });
        it('print {boolean}', function (done: () => any) {
            TAM.ensureResult(`
                boolean a = true;
                print a;
                a = false;
                print a;
            `, {
                resolve: true,
                checkType: true,
                output: ['1', '0']
            });
            done();
        });
        it('print {NamedType}', function (done: () => any) {
            TAM.ensureResult(`
                typedef int Entier;
                Entier a = 2;
                print a;
            `, {
                resolve: true,
                checkType: true,
                output: ['2']
            });
            done();
        });
        it('print {String}', function (done: () => any) {
            TAM.ensureResult(`
                String c1 = "str1";
                String c2 = "str2";
                String c3 = "str3";
                print c1;
                print c3;
                print c2;
            `, {
                resolve: true,
                checkType: true,
                output: ['"str1"', '"str3"', '"str2"']
            });
            done();
        });
    });

    describe('# BinaryExpression', function () {
        it('+ * % / < > <= >= || && ..', function (done: () => any) {
            TAM.ensureResult(`
                int a = 10;
                int b = 20;
                int c = a - b; // -10
                int d = c + b * 4 / 2; // 30
                
                int e = b % 15; // 5
                boolean f = e > 4; // true
                boolean g = e < 4; // false
                boolean h = e <= 3; // false
                
                boolean i = e >= 3; // true
                boolean j = f || g; // true
                boolean k = f && g; // false
                
                print a; print b; print c; print d;
                print e; print f; print g; print h;
                print i; print j; print k;
            `,
                {
                    resolve: true,
                    checkType: true,
                    output: ['10', '20', '-10', '30', '5', '1', '0', '0', '1', '1', '0']
                });
            done();
        });
    });

    describe('# Constant', function() {
        it('constant usage', function(done: () => any) {
            TAM.ensureResult(
                `const String s = "Hello"; print s;`,
                {resolve: true, checkType: true, output: ['"Hello"']});
            done();
        });
    });

    describe('# TernaryExpression', function () {
        it('a ? b : c', function (done: any) {
            TAM.ensureResult(`
                int a = true ? 1 : 2;
                int b = a <= 1 ? 0 : 1;
                print b;
            `, {
                resolve: true,
                checkType: true,
                output: ['0']
            });
            done();
        });
        it('a ? (b ? c : d) : (e ? f : g)', function (done: any) {
            TAM.ensureResult(`
                int a = true ? 1 : 2;
                int b = a > 1 ? (a > 2 ? 0 : 1) : (a < 0 ? 2 : 3);
                print a;
                print b;
            `, {
                resolve: true,
                checkType: true,
                output: ['1', '3']
            });
            done();
        });
    });


    describe('# Pointer', function () {
        it('reference getter', function(done: () => any) {
            TAM.ensureResult(`
                int a = 1;
                int b = 2;
                int c = 3;
                int *ptr = &b;
                *ptr = 42;
                print a;
                print b;
                print c;
                print *ptr;
                `,
                {resolve: true, checkType: true, output: ['1', '42', '3', '42']});
            done();
        });
        it('pointer of named type -> record type', function(done: () => any) {
            TAM.ensureResult(`
                typedef <int, int> Point;
                Point *ptr = new Point();
                *ptr = <1, 2>;
                int a = fst (*ptr);
                int b = snd (*ptr);
                print a;
                print b;
                `, {resolve: true, checkType: true, output: ['1', '2']});
            done();
        });



        it('basic usage', function (done: any) {
            TAM.ensureResult(`
                int *ptr = new int();
                *ptr = 2;
                print *ptr;
            `, {resolve: true, checkType: true, output: ['2']});
            done();
        });
        it('shared pointer', function (done: any) {
            TAM.ensureResult(`
                int *ptr = new int();
                *ptr = 2;
                int *addr = ptr;
                *ptr = 4;
                print *addr;
            `, {resolve: true, checkType: true, output: ['4']});
            done();
        });
    });

    describe('# NamedType', function() {
        it('declaration and assignement', function (done: () => any) {
            TAM.ensureResult(`
                typedef int entier;
                entier a = 2;
                print a;
            `, {
                resolve: true,
                checkType: true,
                output: ['2']
            });
            done();
        });
    });

    describe('# Record', function () {
        it('access', function (done: () => any) {
            TAM.ensureResult(`
                struct SCircle {int x; int y; int radius;} circle = {1, 2, 3};
                print circle.y;
                print circle.x;
                print circle.radius;
            `, {
                resolve: true,
                checkType: true,
                output: ['2', '1', '3']
            });
            done();
        });
        it('assignement', function(done: () => any) {
            TAM.ensureResult(`    int a = 1;
                typedef struct SPoint {int x; int y;} Point;
                Point p = {1, 2};
                p.x = 10;
                p.y = 20;
                print p.x;
                print p.y;
            `, {
                resolve: true,
                checkType: true,
                output: ['10', '20']
            });
            done();
        });
    });

    describe('# Array', function () {
        it('int a[] = new int[2]', function (done: any) {
            TAM.ensureResult(`
                int a[] = new int[2];
                a[0] = 8;
                a[1] = 4;
                print a[0];
                print a[1];
            `, {
                resolve: true,
                checkType: true,
                output: ['8', '4']
            });
            done();
        });

        it('complex example', function (done: () => any) {
            TAM.ensureResult(`
                int a[] = new int[2];
                a[0] = 8;
                a[1] = 4;
                
                int b = 10; // to fill stack
                
                int e[] = new int[3];
                e[1] = 2;
                e[0] = 1;
                e[2] = a[1] - 1;
                
                int f = e[2];
                
                print a[0]; print a[1];
                print b;
                print e[0]; print e[1]; print f;
                
            `, {
                resolve: true,
                checkType: true,
                output: ['8', '4', '10', '1', '2', '3']
            });
            done();
        });

        it('Array<Record(int, int)>', function (done: () => any) {
            TAM.ensureResult(`
                typedef struct SPoint {int x; int y;} Point;
                Point a[] = new Point[2];
                a[0] = {0, 1};
                a[1] = {2, 3};
                print a[0].x;
                print a[0].y;
                print a[1].y;
                print a[1].x;
            `, {
                resolve: true,
                checkType: true,
                output: ['0', '1', '3', '2']
            });
            done();
        });

        it('Array<Record(<int, boolean>, char)>', function (done: () => any) {
            TAM.ensureResult(`
                typedef struct SS {<int, boolean> a; int b;} S;
                S a[] = new S[2];
                a[0] = {<1, true>, 2};
                a[1] = {<3, false>, 4};
                int r1 = fst (a[0].a);
                boolean r2 = snd (a[0].a);
                int r3 = a[0].b;
                int r4 = fst (a[1].a);
                boolean r5 = snd (a[1].a);
                int r6 = a[1].b;
                print r1;
                print r2;
                print r3;
                print r4;
                print r5;
                print r6;
            `, {
                resolve: true,
                checkType: true,
                output: ['1', '1', '2', '3', '0', '4']
            });
            done();
        });
    });


    describe('# While', function () {
        it('int i = 0; while (i < N) { ++i; }', function (done: any) {
            TAM.ensureResult(`
                const int N = 4;
                int i = 0;
                while (i < N) {
                    print i;
                    i = i + 1;
                }
            `, {
                resolve: true,
                checkType: true,
                output: ['0', '1', '2', '3']
            });
            done();
        });
        it('while (..) { while (..) { .. } }', function (done: any) {
            TAM.ensureResult(`
                const int N = 2;
                int i = 0;
                while (i < N) {
                    int j = 0;
                    while (j < N) {
                        // do stuff
                        print j;
                        j = j + 1;
                    }
                    i = i + 1;
                }
            `, {
                resolve: true,
                checkType: true,
                output: ['0', '1', '0', '1']
            });
            done();
        });
    });


    describe('# String', function () {
        it('affectations', function (done: any) {
            TAM.ensureResult(`
                String c1 = "str1";
                String c2 = c1;
                c1 = "str4";
                print c2;
            `, {
                resolve: true,
                checkType: true,
                output: ['"str1"']
            });
            done();
        });
        it('concatenation', function(done: () => any) {
            TAM.ensureResult(`
                String a = "Hello";
                String b = " ";
                String c = "World";
                String d = a..b..c;
                print d;
            `, {
                resolve: true,
                checkType: true,
                output: ['"Hello World"']
            });
            done();
        });
        it('comparaison test', function(done: () => any) {
            TAM.ensureResult(`
                String a = "Hello";
                String b = "Hello";
                boolean c = a == b; // comparaison are made by address
                print c;
            `, {
                resolve: true,
                checkType: true,
                output: ['0']
            });
            done();
        });
    });





    describe('# Functions', function() {
        describe('f: () => any', function() {
            it('local variables', function(done: () => any) {
                TAM.ensureResult(`
                int f() {
                    int a = 1;
                    int b = 2;
                    return a + b;
                }
                int a = f();
                print a;
                `, {resolve: true, checkType: true, output: ['3']});
                done();
            });
            it('function calling another', function(done: () => any) {
                TAM.ensureResult(`
                int f1() {
                    return 20;
                }
                int f2() {
                    return 1 + f1();
                }
                int a = f2();
                print a;
                `, {resolve: true, checkType: true, output: ['21']});
                done();
            });
            it('local variables in nested functions', function(done: () => any) {
                TAM.ensureResult(`
                int f1() {
                    return 20;
                }
                int f2() {
                    return 1 + f1();
                }
                int a = f2();
                print a;
                `, {resolve: true, checkType: true, output: ['21']});
                done();
            });
            it('multiple returns in one function', function(done: () => any) {
                TAM.ensureResult(`
                    int a = 1;
                    
                    int f() {
                        if (a < 0) {
                            return 1;
                        } else {
                            if (a < 10) {
                                return 2;
                            } else {
                                return 3;
                            }
                        }
                    }
                    
                    a = -1; print f();
                    a = 1; print f();
                    a = 20; print f();
                `, {resolve: true, checkType: true, output: ['1', '2', '3']});
                done();
            });
            it('returning complex values', function(done: () => any) {
                TAM.ensureResult(`
                    typedef struct SPoint {int x; int y;} Point;
                    
                    Point getReferential() {
                        return {1, 4};
                    }
                    
                    Point point = getReferential();
                    print point.x;
                    print point.y;
                `, {resolve: true, checkType: true, output: ['1', '4']});
                done();
            });
        });

        describe('f: (a: any) => any', function() {

            it('identity function', function(done: () => any) {
                TAM.ensureResult(`
                    // noise for filling the stack
                    int a = 1;
                    int b = 1;
                    
                    int func(int a) {
                        return a;
                    }
                    
                    // noise
                    int c = 1;
                    int d = 1;
                    
                    int e = func(3);
                    print e;
                `, {resolve: true, checkType: true, output: ['3']});
                done();
            });
            it('with local variable', function(done: () => any) {
                TAM.ensureResult(`
                    // noise for filling the stack
                    int a = 1;
                    int b = 1;
                    
                    int add5(int a) {
                        int add = 5;
                        return a + add;
                    }
                    
                    // noise
                    int c = 1;
                    int d = 1;
                    
                    int e = add5(5);
                    print e;
                `, {resolve: true, checkType: true, output: ['10']});
                done();
            });
            it('nested functions with local variables', function(done: () => any) {
                TAM.ensureResult(`
                    // noise for filling the stack
                    int a = 1;
                    int b = 1;
                    
                    int add5(int a) {
                        int add = 5;
                        return a + add;
                    }
                    int add10(int a) {
                        int add = 5;
                        return add + add5(a);
                    }
                    
                    // noise
                    int c = 1;
                    int d = 1;
                    
                    int e = add10(5);
                    print e;
                `, {resolve: true, checkType: true, output: ['15']});
                done();
            });
            it('recursive terminal (factorial)', function(done: () => any) {
                TAM.ensureResult(`
                    int fact(int n) {
                        if (n <= 1) {
                            return 1;
                        } else {
                            return n * fact(n - 1);
                        }
                    }
                
                    int a = fact(3);
                    print a;
                    `, {resolve: true, checkType: true, output: ['6']});
                done();
            });
            it('recursive with parameter erasing', function(done: () => any) {
                TAM.ensureResult(`
                    int a = 1;
                    int b = 1;
                    int c = 1;
                
                    int sum(int a, int b) {
                        return a + b;
                    }
                
                    int d = 1;
                    int e = 1;
                
                    print sum(2, 3);
                    print sum(3, 4);
                    `, {resolve: true, checkType: true, output: ['5', '7']});
                done();
            });
        });
        describe('f: (a: any, b: any) => any', function() {
            it('sum', function(done: () => any) {
                TAM.ensureResult(`
                    int sum(int a, int b) {
                        return a + b;
                    }
                    
                    print sum(1, 3);
                    print sum(3, 4);
                    `, {resolve: true, checkType: true, output: ['4', '7']});
                done();
            });
            it('different scope access', function(done: () => any) {
                TAM.ensureResult(`
                    int a = 1;
                    const int b = 10;
                    
                    int f(int c) {
                        int d = 100;
                        return a + b + c + d;
                    }
                    
                    print f(1000);
                    `, {resolve: true, checkType: true, output: ['1111']});
                done();
            });
            it('different scope access in nested functions', function(done: () => any) {
                TAM.ensureResult(`
                    int a = -1;
                    
                    int f1(int a, int b) {
                        int f2(int a, int b) {
                            int e = 10;
                            return a * 10 + b;
                        }
                        
                        int result1 = f2(b, a); // 21
                        return result1;
                    }
                    
                    print f1(1, 2);
                    `, {resolve: true, checkType: true, output: ['21']});
                done();
            });
            it('with complex return type and complex parameter types', function(done: () => any) {
                TAM.ensureResult(`
                    
                    typedef struct SPoint {int x; int y;} Point;
                    
                    Point getCenter(Point point1, Point point2) {
                        int x = (point1.x + point2.x) / 2;
                        int y = (point1.y + point2.y) / 2;
                        return {x, y};
                    }
                    
                    Point p1 = {0, 0};
                    Point p2 = {10, 20};
                    Point center = getCenter(p1, p2);
                    print center.x;
                    print center.y;
                    `, {resolve: true, checkType: true, output: ['5', '10']});
                done();
            });
        });
    });
});


