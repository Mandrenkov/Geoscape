package geo;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Triangle</b> class.</p>
 */
public class Triangle {
	/**
	 * List of Points that constitute the Triangle.
	 */
	protected final Point[] POINTS;

	/**
	 * Constructs a Triangle object with the given set of Points.
	 * 
	 * @param points Set of Points that define the Triangle.
	 */
	public Triangle(Point ... points) {
		if (points.length != 3) throw new IllegalArgumentException("Triangle must have 3 points.");

		this.POINTS = points;
	}

	/**
	 * Returns a normalized GeoVector that is perpendicular to the face of the Triangle.
	 * 
	 * @return The normalized GeoVector.
	 */
	public GeoVector getNormal() {
		GeoVector vectorA, vectorB;

		vectorA = new GeoVector(POINTS[0]).to(new GeoVector(POINTS[1]));
		vectorB = new GeoVector(POINTS[0]).to(new GeoVector(POINTS[2]));

		return vectorA.cross(vectorB);
	}

	/**
	 * Returns the Points of this Triangle.
	 * 
	 * @return The Points of this Triangle.
	 */
	public Point[] getPoints() {
		return POINTS;
	}

	/**
	 * Returns a String representation of this Triangle.
	 */
	public String toString() {
		return String.format("%s: %s <--> %s <--> %s",
			this.getClass().getName(),
			POINTS[0],
			POINTS[1],
			POINTS[2]
		);
	}
}