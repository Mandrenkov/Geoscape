package util;

/**
 * The Algebra class contains a set of Linear Algebra functions.
 */
public class Algebra {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Returns the weighted average of the given values.
     *
     * @param x The first value.
     * @param y The second value.
     * @param w The weight of |y|.
     *          This value should fall within the range [0, 1].
     *
     * @return The weighted average.
     */
    public static float average(float x, float y, float w) {
        return x + w*(y - x);
    }

    /**
     * Maps the given value to a smooth curve within the range [0, 1].
     * The current implementation maps the given value to a cosine function.
     *
     * @param x The value to be mapped to the curve.
     *          This value should fall within the range [0, 1].
     *
     * @return The mapped value.
     */
    public static float curve(float x) {
        return (float) (1f - Math.cos(Math.PI*x))/2f;
    }
}