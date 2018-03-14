package geo;

import java.util.function.Function;

import env.Colour;

/**
 * @author Mikhail Andrenkov
 * @since March 13, 2018
 * @version 1.1
 *
 * <p>The <b>Prism</b> class represents a rectangular prism.</p>
 */
public class Prism extends Shape {

    // Public members
    // -------------------------------------------------------------------------

    /**
     * Constructs a Prism with the given corner Vertices and Colour.
     *
     * @param colour The colour of this Prism.
     * @param v1     One corner of this Prism.
     * @param v2     The other corner of this Prism.
     */
    public Prism(Colour colour, Vertex v1, Vertex v2) {
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
                    vertices[i] = new Vertex(xs[x], ys[y], zs[z], colour);
                }
            }
        }

        // Create a clockwise Quad to represent each face of the Prism.
        this.polygons = new Polygon[]{new Quad(vertices[0], vertices[1], vertices[3], vertices[2]),  // Bottom
                                      new Quad(vertices[0], vertices[4], vertices[5], vertices[1]),  // Front
                                      new Quad(vertices[0], vertices[2], vertices[6], vertices[4]),  // Left
                                      new Quad(vertices[2], vertices[3], vertices[7], vertices[6]),  // Back
                                      new Quad(vertices[1], vertices[5], vertices[7], vertices[3]),  // Right
                                      new Quad(vertices[4], vertices[6], vertices[7], vertices[5])}; // Top
    }

    /**
     * Returns a String representation of this Prism.
     *
     * @return The String representation.
     */
    public String toString() {
        Vertex low = this.polygons[0].getVertexes()[0];
        Vertex high = this.polygons[5].getVertexes()[3];
        return String.format("%s to %s", low, high);
    }
}