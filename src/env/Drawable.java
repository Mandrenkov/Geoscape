package env;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>Classes that implement the <b>Drawable</b> interface represent objects that
 * can be rendered to the screen.</p>
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