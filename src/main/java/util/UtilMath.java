package util;

public class UtilMath {

    public static int clamp(int i, int min, int max) {
        if (i < min) return min;
        if (i > max) return max;
        return i;
    }

    public static double clamp(double i, double min, double max) {
        if (i < min) return min;
        if (i > max) return max;
        return i;
    }

    public static int divRoundClosest(int n, int d) {
        return ((n < 0) == (d < 0)) ? ((n + d/2)/d) : ((n - d/2)/d);
    }

    public static int divRoundUp(int n, int d) {
        return (n + (d - 1)) / d;
    }

    public static int divRoundDown(int n, int d) {
        return n/d;
    }

    public static double distanceBetween(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
