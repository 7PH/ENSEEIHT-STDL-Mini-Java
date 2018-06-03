sh launch.sh $1 1 > tmp.tam
java -jar tools/aspartam.jar tmp.tam
java -jar tools/tammachine.jar tmp.tamo
echo "====================================";
cat tmp.res
