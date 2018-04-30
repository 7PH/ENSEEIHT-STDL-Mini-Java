#!/bin/bash

echo -n "Building..              ";
ant > /dev/null;
echo " [OK]";

echo -n "Compiling test file..   ";
tsc;
echo " [OK]";

mocha tests/test.js --reporter $1;
