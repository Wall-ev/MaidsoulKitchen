package com.github.wallev.maidsoulkitchen.util;

public class MathUtil {

    public static int max(int... values) {
        int max = values[0];
        for (int value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static int min(int... values) {
        int max = values[0];
        for (int value : values) {
            if (value < max) {
                max = value;
            }
        }
        return max;
    }
}
