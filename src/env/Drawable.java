package env;

/**
 * Classes that implement the Drawable interface represent objects that can be
 * rendered to the screen.
 */
public interface Drawable {

    /**
     * Draws a representation of this Drawable entity.
     */
    public void draw();

    /**
     * Returns the number of Polygons comprising this entity.
     *
     * @return The number of Polygons.
     */
    public int polygons();
}