package geo;

import java.util.ArrayList;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Sphere</b> class.</p>
 */
public class Sphere {

	/**
	 * Number of iterations performed over the shape-refinement process.
	 */
	private static final int REFINE_STEPS = 5;

	/**
	 * List of Sphere vertices.
	 */
	private ArrayList<Vertex> points;
	/**
	 * List of Triangles that constitute (approximate) the Sphere contour.
	 */
	private ArrayList<Triangle> triangles;
	/**
	 * Origin Vertex of the Sphere.
	 */
	private Vertex origin;
	/**
	 * Radius of the Sphere.
	 */
	private float radius;

	/**
	 * Constructs a new Sphere with the given origin and radius.
	 * 
	 * @param origin Origin of the Sphere.
	 * @param radius Radius of the Sphere.
	 */
	public Sphere(Vertex origin, float radius) {
		this.origin = origin;
		this.radius = radius;

		generateTriangles();
	}

	/**
	 * Generates a list of Triangles that define the Sphere exterior.
	 */
	public void generateTriangles() {
		points = new ArrayList<>();
		triangles = new ArrayList<>();

		genDiamond();
		genRefine();
		genScale();
		genTranslate();
	}

	/**
	 * Returns the list of Triangles corresponding to this Sphere.
	 * 
	 * @return The list of Triangles corresponding to this Sphere.
	 */
	public ArrayList<Triangle> getTriangles() {
		return triangles;
	}

	/**
	 * Generates a diamond that serves as the initial form approximation of this Sphere.
	 */
	private void genDiamond() {
		Vertex u = new Vertex(0f, 0f,  1);
		Vertex d = new Vertex(0f, 0f, -1f);
		Vertex l = new Vertex(-1f, 0f, 0f);
		Vertex r = new Vertex( 1f, 0f, 0f);
		Vertex b = new Vertex(0f, -1f, 0f);
		Vertex f = new Vertex(0f,  1f, 0f);

		points.add(u);
		points.add(d);
		points.add(l);
		points.add(r);
		points.add(b);
		points.add(f);

		triangles.add(new Triangle(u, f, l));
		triangles.add(new Triangle(u, l, b));
		triangles.add(new Triangle(u, b, r));
		triangles.add(new Triangle(u, r, f));
		triangles.add(new Triangle(d, l, f));
		triangles.add(new Triangle(d, f, r));
		triangles.add(new Triangle(d, r, b));
		triangles.add(new Triangle(d, b, l));
	}

	/**
	 * Refines the Sphere form approximation to approach a round object.
	 */
	private void genRefine() {
		// Perform REFINE_STEPS iterations of the refinement process.
		for (int a = 0 ; a < REFINE_STEPS ; a++) {
			ArrayList<Triangle> newTriangles = new ArrayList<>();

			// Replace each Triangle on the Sphere with 4 new Triangles
			// that better approximate the round nature of the Sphere.
			for (Triangle t : triangles) {
				Vertex[] triPoints = t.getPoints();
				Vertex[] midPoints = new Vertex[3];

				Vertex midPoint;
				GeoVector midVector;

				// Find the midpoint coordinates of each line of the Triangle.
				for (int i = 0 ; i < 3 ; ++i) {
					midPoint = triPoints[i].average(triPoints[(i + 1) % 3]);

					midVector = new GeoVector(midPoint);
					midVector.normalize();

					midPoint = new Vertex(midVector.getX(), midVector.getY(), midVector.getZ());
					midPoints[i] = midPoint;
					points.add(midPoint);
				}

				// Construct the new Triangles out of the original Triangle's midpoints.
				newTriangles.add(new Triangle(midPoints[0], triPoints[0], midPoints[2]));
				newTriangles.add(new Triangle(midPoints[1], triPoints[1], midPoints[0]));
				newTriangles.add(new Triangle(midPoints[2], triPoints[2], midPoints[1]));
				newTriangles.add(new Triangle(midPoints[2], midPoints[1], midPoints[0]));
			}

			triangles = newTriangles;
		}
	}

	/**
	 * Scales each Vertex of this Sphere to this Sphere's radius.
	 */
	private void genScale() {
		for (Vertex p : points) {
			p.setX(p.getX()*radius);
			p.setY(p.getY()*radius);
			p.setZ(p.getZ()*radius);
		}
	}

	/**
	 * Translates each Vertex of this Sphere such that the Sphere is centered about its origin.
	 */
	private void genTranslate() {
		for (Vertex p : points) {
			p.setX(p.getX() + origin.getX());
			p.setY(p.getY() + origin.getY());
			p.setZ(p.getZ() + origin.getZ());;
		}
	}
}