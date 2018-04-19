./build.sh > /dev/null;
tsc;
mocha tests/test.js --reporter $1;
