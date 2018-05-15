package tests;

class Point { public int x; public int y; }

public class Test01 {
    public static void main(String args[]) {
        // Type -> UL_Ident '[' (+ suite)
        Point[] MyPoints = new Point[10];
        // ArrayAccess -> UL_Ident '[' (+ suite)
        MyPoints[0] = new Point();
    }
}
