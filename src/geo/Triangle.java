package geo;

/**
 * @author Mikhail Andrenkov
 * @since April 10, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Triangle</b> class.</p>
 */ 
public class Triangle {
	protected final Point[] POINTS;

	public Triangle(Point ... points) {
		if (points.length != 3) throw new IllegalArgumentException("Triangle must have 3 points.");

		this.POINTS = points;
	}

	public GeoVector getNormal() {
		GeoVector vectorA, vectorB;

		vectorA = new GeoVector(POINTS[0]).to(new GeoVector(POINTS[1]));
		vectorB = new GeoVector(POINTS[0]).to(new GeoVector(POINTS[2]));

		return vectorA.cross(vectorB);
	}

	public Point[] getPoints() {
		return POINTS;
	}

	public String toString() {
		return String.format("%s: %s <--> %s <--> %s",
			this.getClass().getName(),
			POINTS[0],
			POINTS[1],
			POINTS[2]
		);
	}
}