package env;

import static org.lwjgl.opengl.GL11.*;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;

import core.Logger;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <i>Colour</i> class represents an RGBA colour.</p>
 */
public class Colour {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * The set of options that constrain the generation of random Colours.
     */
    public static enum Option {
        LIGHT,
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
            case LIGHT:
                float clr = (float) Math.random()*0.3f + 0.7f;
                return new Colour(clr, clr, clr);
            default:
                return new Colour((float) Math.random(), (float) Math.random(), (float) Math.random());
        }
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
        glColor3f(this.red, this.green, this.blue);
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
        float rand = (float) ThreadLocalRandom.current().nextDouble(-magnitude, magnitude);
        this.red   = Math.min(1f, Math.max(0f, rand + this.red));
        this.green = Math.min(1f, Math.max(0f, rand + this.green));
        this.blue  = Math.min(1f, Math.max(0f, rand + this.blue));
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