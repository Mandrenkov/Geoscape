package util;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import geo.*;

/**
 * @author Mikhail Andrenkov
 * @since May 14, 2017
 * @version 1.0
 *
 * <p>The <i>Render</i> class is responsible for providing utilities to render Drawable entities.</p>
 */
public class Render {
	/**
	 * Adds the given point as a GL vertex.
	 *
	 * @param p Point to be added as a GL vertex.
	 */
	public static void addVertex(Point p) {
		glVertex3f(p.getX(), p.getY(), p.getZ());
	}

	/**
	 * Renders the X, Y, and Z axes for orientation debug purposes.
	 */
	public static void drawAxes() {
		// Length of each axis line
		float axisDistance = 1.1f;

		// Line setup
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glLineWidth(2);

		glBegin(GL_LINES);
			// X Axis
			glColor3f(1.0f, 0.0f, 0.0f);
			glVertex3f(-0f, 0f, 0f);
			glVertex3f(axisDistance, 0f, 0f);

			// Y Axis
			glColor3f(0.0f, 1.0f, 0.0f);
			glVertex3f(0f, -0f, 0f);
			glVertex3f(0f, axisDistance, 0f);

			// Z Axis
			glColor3f(0f, 0f, 1f);
			glVertex3f(0f, 0f, -0f);
			glVertex3f(0f, 0f, axisDistance);
		glEnd();
	}

	/**
	 * Renders the given Sphere in the specified colour.
	 * 
	 * @param sphere The sphere to be drawn.
	 * @param colour Colour to be applied to the Sphere.
	 */
	public static void drawSphere(Sphere sphere, float[] colour) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

		// Render triangles
		glBegin(GL_TRIANGLES);
			ArrayList<Triangle> triangles = sphere.getTriangles();
			for (int t = 0 ; t < triangles.size() ; t++) {
				float[] triColour = colour;

				if (colour == null) {
					float lum = (float) (t + 1)/triangles.size();
					triColour = new float[]{lum, lum, lum};
				}

				drawTriangle(triangles.get(t), triColour);
			}
		glEnd();
	}

	/**
	 * Renders the given Triangle in the specified colour.
	 *
	 * @param points Points of the Triangle to be rendered.
	 * @param colour Colour to be applied to the Triangle.
	 */
	public static void drawTriangle(Triangle triangle, float[] colour) {
		Point[] points = triangle.getPoints();

		// Set triangle colour
		glColor3f(colour[0], colour[1], colour[2]);

		// Add triangle vertices
		addVertex(points[0]);
		addVertex(points[1]);
		addVertex(points[2]);
	}

	/**
	 * Rotates the view about the specified axis by the given value.
	 *
	 * @param axis Rotation axis.
	 * @param value Increment value.
	 */
	public static void rotateAxis(char axis, float value) {
		float x = 0f, y = 0f, z = 0f;

		if (axis == 'x' || axis == 'X') x = 1f;
		else if (axis == 'y' || axis == 'Y') y = 1f;
		else if (axis == 'z' || axis == 'Z') z = 1f;

		glRotatef(value, x, y, z);
	}
}