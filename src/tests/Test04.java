package tests;

interface AA<T> { }
interface AB { }

public class Test04<T1 extends AA<T1> & AB, T2 extends AA<T1>> { }