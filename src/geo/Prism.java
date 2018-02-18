package geo;

import static org.lwjgl.opengl.GL11.*;

import java.util.function.Function;

/**
 * @author Mikhail Andrenkov
 * @since February 18, 2018
 * @version 1.1
 *
 * <p>The <b>Prism</b> class represents a rectangular prism.</p>
 */
public class Prism extends Composite {

	// Public members
	// -------------------------------------------------------------------------

    /**
     * Constructs a Prism with the given corner Vertices.
     * 
     * @param v1 One corner of this Prism.
     * @param v2 The other corner of this Prism.
     */
	public Prism(Vertex v1, Vertex v2) {
        // Returns an array containing the minumum and maximum values of the given Vertex dimension.
        Function<Function<Vertex, Float>, float[]> minimax = (Function<Vertex, Float> getter) -> {
            float min = Math.min(getter.apply(v1), getter.apply(v2));
            float max = Math.max(getter.apply(v1), getter.apply(v2));
            return new float[]{min, max};
        };
        float[] xs = minimax.apply(Vertex::getX);
        float[] ys = minimax.apply(Vertex::getY);
        float[] zs = minimax.apply(Vertex::getZ);

        // Create a Vertex for each corner of the Prism.
        Vertex[] vertices = new Vertex[8];
        for (int z = 0; z < 2; ++z) {
            for (int y = 0; y < 2; ++y) {
                for (int x = 0; x < 2; ++x) {
                    int i = (z << 2) + (y << 1) + x;
                    vertices[i] = new Vertex(xs[x], ys[y], zs[z]);
                }
            }
        }

        // Create a clockwise Quad to represent each face of the Prism.
        Shape[] faces = new Shape[]{new Quad(vertices[0], vertices[1], vertices[2], vertices[3]),
                                    new Quad(vertices[0], vertices[2], vertices[6], vertices[4]),
                                    new Quad(vertices[2], vertices[6], vertices[7], vertices[3]),
                                    new Quad(vertices[3], vertices[7], vertices[5], vertices[1]),
                                    new Quad(vertices[1], vertices[5], vertices[4], vertices[0]),
                                    new Quad(vertices[6], vertices[4], vertices[5], vertices[7])};
        this.shapes = faces;
	}

	/**
	 * Draws this Sphere.
	 */
	public void draw() {
		glBegin(GL_QUADS);
		for (Shape shape : shapes) {
			shape.draw();
		}
		glEnd();
	}

	/**
	 * Returns a String representation of this Sphere.
	 * 
	 * @return The String representation.
	 */
	public String toString() {
        Vertex low = shapes[0].getVertices()[0];
        Vertex high = shapes[7].getVertices()[3];
		return String.format("%s to %s", low, high);
	}
}