package util;

import java.util.SplittableRandom;

public class Randomizer {

    private static SplittableRandom random = new SplittableRandom();

    public static boolean decide(float probability) {
        return random.nextDouble() < probability;
    }

    public static boolean decide(double probability) {
        return random.nextDouble() < probability;
    }

    public static int nextInt(int min, int max) {
        return random.nextInt(min, max);
    }

    public static double nextDouble() {
        return random.nextDouble();
    }
}
