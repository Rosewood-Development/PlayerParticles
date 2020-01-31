package dev.esophose.playerparticles.util;

/**
 * Credit goes to Riven from this thread:
 * http://www.java-gaming.org/topics/extremely-fast-sine-cosine/36469/view.html
 */
public final class MathL {

    private static final int SIN_BITS, SIN_MASK, SIN_COUNT;
    private static final float radFull, radToIndex;
    private static final float degFull, degToIndex;
    private static final float[] sin, cos;

    static {
        SIN_BITS = 12;
        SIN_MASK = ~(-1 << SIN_BITS);
        SIN_COUNT = SIN_MASK + 1;

        radFull = (float) (Math.PI * 2.0);
        degFull = (float) (360.0);
        radToIndex = SIN_COUNT / radFull;
        degToIndex = SIN_COUNT / degFull;

        sin = new float[SIN_COUNT];
        cos = new float[SIN_COUNT];

        for (int i = 0; i < SIN_COUNT; i++) {
            sin[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * radFull);
            cos[i] = (float) Math.cos((i + 0.5f) / SIN_COUNT * radFull);
        }

        for (int i = 0; i < 360; i += 90) {
            sin[(int) (i * degToIndex) & SIN_MASK] = (float) Math.sin(i * Math.PI / 180.0);
            cos[(int) (i * degToIndex) & SIN_MASK] = (float) Math.cos(i * Math.PI / 180.0);
        }
    }

    public static float sin(double rad) {
        return sin[(int) (rad * radToIndex) & SIN_MASK];
    }

    public static float cos(double rad) {
        return cos[(int) (rad * radToIndex) & SIN_MASK];
    }

}
