package env;

import static org.lwjgl.opengl.GL11.*;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import core.Logger;
import util.Algebra;
import util.RNG;

/**
 * The Colour class represents an RGBA colour.
 */
public class Colour {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * The FP array representation of an opaque white OpenGL colour.
     */
    public static final float[] GL_WHITE = {1, 1, 1, 1};

    /**
     * The FP array representation of an opaque black OpenGL colour.
     */
    public static final float[] GL_BLACK = {0, 0, 0, 1};

    /**
     * The set of options that constrain the generation of random Colours.
     */
    public static enum Option {
        LIGHT,
        DARK,
        DEFAULT
    };

    /**
     * Returns the average Colour represented by the given Colours.
     *
     * @param colours The Colours to average.
     *
     * @return The average Colour.
     */
    public static Colour average(Colour... colours) {
        if (colours.length == 0) {
            Logger.error("Cannot determine average of 0 colours.");
            return new Colour();
        }

        // Returns the average value of the given Colour component accessor function.
        BiFunction<Colour[], Function<Colour, Float>, Float> average = (Colour[] clrs, Function<Colour, Float> supplier) -> {
            return (float) Arrays.stream(clrs).mapToDouble(clr -> supplier.apply(clr)).sum()/clrs.length;
        };

        return new Colour(average.apply(colours, Colour::getRed),
                          average.apply(colours, Colour::getGreen),
                          average.apply(colours, Colour::getBlue),
                          average.apply(colours, Colour::getAlpha));
    }

    /**
     * Returns a random opaque Colour using the given colour option.
     *
     * @param option The colour generation option.
     *
     * @return The random Colour.
     */
    public static Colour random(Option option) {
        switch (option) {
            case LIGHT: {
                float clr = RNG.random(0.7f, 0.9f);
                return new Colour(clr, clr, clr);
            }
            case DARK: {
                float clr = RNG.random(0.15f, 0.25f);
                return new Colour(clr, clr, clr);
            }
            default: {
                return new Colour(RNG.random(), RNG.random(), RNG.random());
            }
        }
    }

    /**
     * Returns a random opaque Colour that satisfies the given component constraints.
     * 
     * @param minR The minimum value of the red component.
     * @param maxR The maximum value of the red component.
     * @param minG The minimum value of the green component.
     * @param maxG The maximum value of the green component.
     * @param minB The minimum value of the blue component.
     * @param maxB The maximum value of the blue component.
     * 
     * @return The random Colour.
     */
    public static Colour random(float minR, float maxR, float minG, float maxG, float minB, float maxB) {
        float red   = RNG.random(minR, maxR);
        float green = RNG.random(minG, maxG);
        float blue  = RNG.random(minB, maxB);
        return new Colour(red, green, blue);
    }

    /**
     * Constructs an opaque black Colour.
     */
    public Colour() {
        this(0, 0, 0, 1);
    }

    /**
     * Constructs a Colour that is a clone of the given Colour.
     *
     * @param colour The Colour to clone.
     */
    public Colour(Colour colour) {
        this(colour.red, colour.green, colour.blue, colour.alpha);
    }

    /**
     * Constructs an opaque monochromatic Colour with the given RGB components.
     *
     * @param rgb The RGB components of this Colour.
     */
    public Colour(float rgb) {
        this(rgb, rgb, rgb, 1f);
    }

    /**
     * Constructs an opaque Colour object with the given RGB components.
     *
     * @param red   The red RGBA component.
     * @param green The green RGBA component.
     * @param blue  The blue RGBA component.
     */
    public Colour(float red, float green, float blue) {
        this(red, green, blue, 1f);
    }

    /**
     * Constructs a Colour object with the given RGBA components.
     *
     * @param red   The red RGBA component.
     * @param green The green RGBA component.
     * @param blue  The blue RGBA component.
     * @param alpha The alpha RGBA component.
     */
    public Colour(float red, float green, float blue, float alpha) {
        this.red   = red;
        this.green = green;
        this.blue  = blue;
        this.alpha = alpha;
    }

    /**
     * Adds the components of the given Colour to this Colour.
     *
     * @param colour The Colour to add.
     */
    public void add(Colour colour) {
        this.red   = Math.min(1f, this.red   + colour.red  );
        this.green = Math.min(1f, this.green + colour.green);
        this.blue  = Math.min(1f, this.blue  + colour.blue );
    }

    /**
     * Illuminates this Colour by individually increasing each RGB component
     * such that the new value of each component lies |weight| of the way between
     * the original component value and 1.
     * 
     * @param weight The magnitude of the illumination.  This value should fall
     *               in the range [0, 1].
     */
    public void illuminate(float weight) {
        this.red   = Algebra.curve(Algebra.average(this.red,   1, weight));
        this.green = Algebra.curve(Algebra.average(this.green, 1, weight));
        this.blue  = Algebra.curve(Algebra.average(this.blue,  1, weight));
    }

    /**
     * Returns the red component of this Colour.
     *
     * @return The red component.
     */
    public float getRed() {
        return this.red;
    }

    /**
     * Returns the green component of this Colour.
     *
     * @return The green component.
     */
    public float getGreen() {
        return this.green;
    }

    /**
     * Returns the blue component of this Colour.
     *
     * @return The blue component.
     */
    public float getBlue() {
        return this.blue;
    }

    /**
     * Returns the alpha component of this Colour.
     *
     * @return The alpha component.
     */
    public float getAlpha() {
        return this.alpha;
    }

    /**
     * Sets the GL colour to this Colour.
     */
    public void glColour() {
        glColor4f(this.red, this.green, this.blue, this.alpha);
    }

    /**
     * Multiplies the RGB components of this Colour by the given scalar.
     *
     * @param scale The scalar to multiply.
     */
    public void scale(float scalar) {
        this.red   = Math.min(1f, scalar*this.red);
        this.green = Math.min(1f, scalar*this.green);
        this.blue  = Math.min(1f, scalar*this.blue);
    }

    /**
     * Changes the RGB components of this Colour by a random value within the given range.
     *
     * @param magnitude The maximum magnitude of the change in the components.
     */
    public void shift(float magnitude) {
        float rng  = RNG.random(magnitude);
        this.red   = Math.min(1f, Math.max(0f, rng + this.red));
        this.green = Math.min(1f, Math.max(0f, rng + this.green));
        this.blue  = Math.min(1f, Math.max(0f, rng + this.blue));
    }

    /**
     * Returns an FP array representation of this Colour.
     *
     * @return The array representation.
     */
    public float[] toArray() {
        return new float[]{this.red, this.green, this.blue, this.alpha};
    }

    /**
     * Returns a String representation of this Colour.
     *
     * @return The String representation.
     */
    public String toString() {
        return String.format("Colour (%.2f, %.2f, %.2f, %.2f)", this.red, this.green, this.blue, this.alpha);
    }


    // Private members
    // -------------------------------------------------------------------------

    /**
     * The red RGBA component of this Colour.
     */
    private float red;

    /**
     * The green RGBA component of this Colour.
     */
    private float green;

    /**
     * The blue RGBA component of this Colour.
     */
    private float blue;

    /**
     * The alpha RGBA component of this Colour.
     */
    private float alpha;
}