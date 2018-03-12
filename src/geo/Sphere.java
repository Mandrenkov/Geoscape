package geo;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import env.Colour;

/**
 * @author Mikhail Andrenkov
 * @since February 18, 2018
 * @version 1.1
 *
 * <p>The <b>Sphere</b> class represents a sphere.</p>
 */
public class Sphere extends Shape {

	// Public members
	// -------------------------------------------------------------------------

	/**
	 * Constructs a new Sphere with the given origin and radius.
	 * 
	 * @param origin The origin of the Sphere.
	 * @param radius The radius of the Sphere.
	 */
	public Sphere(Vertex origin, float radius) {
		this.origin = origin;
		this.radius = radius;

		// Create the Triangular faces of the Sphere.
		ArrayList<Triangle> faces = approximate();
		for (int i = 0; i < REFINE_STEPS; ++i) {
			faces = refine(faces);
		}

		// Derive the set of unique Vertexes that constitute the face Triangles.
		Set<Vertex> vertexes = new HashSet<>();
		for (Triangle face : faces) {
			for (Vertex vertex : face.getVertexes()) {
				vertexes.add(vertex);
			}
		}

		// Colour, scale, and translate each Vertex to match the origin and
		// radius of this Sphere.
		for (Vertex vertex : vertexes) {
			vertex.scale(radius);
			vertex.translate(origin.getX(), origin.getY(), origin.getZ());
		}

		this.polygons = faces.stream().toArray(Polygon[]::new);
	}

	/**
	 * Returns a String representation of this Sphere.
	 * 
	 * @return The String representation.
	 */
	public String toString() {
		return String.format("%s with %.2f", origin, radius);
	}


	// Private members
	// -------------------------------------------------------------------------
	
	/**
	 * The number of refinement iterations to be performed.
	 */
	private static final int REFINE_STEPS = 3;

	/**
	 * The origin of this Sphere.
	 */
	private Vertex origin;

	/**
	 * The radius of this Sphere.
	 */
	private float radius;

	/**
	 * Creates a first-order approximation of this Sphere (i.e., an octohedron).
	 * 
	 * @return The list of Triangles representing the initial faces of the Sphere.
	 */
	private ArrayList<Triangle> approximate() {
		ArrayList<Triangle> faces = new ArrayList<>();

		// Declare the Vertices representing the corners of the octahedron.
		Vertex u = new Vertex(Colour.random(Colour.Option.LIGHT),  0,  0,  1);
		Vertex d = new Vertex(Colour.random(Colour.Option.LIGHT),  0,  0, -1);
		Vertex l = new Vertex(Colour.random(Colour.Option.LIGHT), -1,  0,  0);
		Vertex r = new Vertex(Colour.random(Colour.Option.LIGHT),  1,  0,  0);
		Vertex b = new Vertex(Colour.random(Colour.Option.LIGHT),  0, -1,  0);
		Vertex f = new Vertex(Colour.random(Colour.Option.LIGHT),  0,  1,  0);
		
		// Create the Triangles representing the faces of the octahedron.
		faces.add(new Triangle(u, f, r));
		faces.add(new Triangle(u, r, b));
		faces.add(new Triangle(u, b, l));
		faces.add(new Triangle(u, l, f));
		
		faces.add(new Triangle(d, f, l));
		faces.add(new Triangle(d, l, b));
		faces.add(new Triangle(d, b, r));
		faces.add(new Triangle(d, r, f));

		return faces;
	}

	/**
	 * Refines the faces of the Sphere to better approximate a round object.
	 * 
	 * @return The new list of Triangles representing the faces of the Sphere.
	 */
	private ArrayList<Triangle> refine(ArrayList<Triangle> faces) {
		ArrayList<Triangle> newTriangles = new ArrayList<>();

		// Replace each Triangle face on the Sphere with 4 new Triangles that
		// better represent the round nature of the Sphere.
		for (Triangle face : faces) {
			Vertex[] corners = face.getVertexes();
			Vertex[] midpoints = new Vertex[3];

			// Find the normalized midpoint coordinates of each edge in the Triangle.
			for (int i = 0; i < 3; ++i) {
				midpoints[i] = new Vertex(corners[i], corners[(i + 1) % 3]);
				midpoints[i].normalize(); 
			}

			// Construct 4 new faces using the original Triangle's corners and midpoints.
			newTriangles.add(new Triangle(corners[0], midpoints[0], midpoints[2]));
			newTriangles.add(new Triangle(corners[1], midpoints[1], midpoints[0]));
			newTriangles.add(new Triangle(corners[2], midpoints[2], midpoints[1]));
			newTriangles.add(new Triangle(midpoints[0], midpoints[1], midpoints[2]));
		}
		return newTriangles;
	}
}