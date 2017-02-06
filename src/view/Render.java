package view;

import static org.lwjgl.opengl.GL11.*;

import core.*;
import model.*;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>The <i>Render</i> class is responsible for rendering the landscape.</p>
 */ 
public class Render {
	
	/**
	 * Amount to increment z-axis angle every frame.
	 */
	private static final float Z_ROTATE_DELTA = -0.30f;

	/**
	 * Calls the top-level feature rendering functions.
	 */
	public void run(World world) {
		if (Top.DEBUG) {
			renderAxes();
		}
		
		renderPlatform();

		for (Terrain terrain : world.getLandscape())
			renderGrid(terrain.getGrid());
		
		rotateAxis('Z', Z_ROTATE_DELTA);
	}

	/**
	 * Adds the given point as a GL vertex.
	 * 
	 * @param p Point to be added as a GL vertex.
	 */
	private void addVertex(Point p) {
		glVertex3f(p.getX(), p.getY(), p.getZ());
	}

	/**
	 * Renders the X, Y, and Z axes for orientation debug purposes.
	 */
	private void renderAxes() {
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
	 * Renders the triangles of the given Grid.
	 * 
	 * @param grid The Grid to be rendered.
	 */
	private void renderGrid(Grid grid) {
		// Polygon setup
		glPolygonMode(GL_FRONT, GL_FILL);
		glCullFace(GL_BACK);

		// Render triangles
		glBegin(GL_TRIANGLES);
			grid.getTriangles().forEach(t -> renderTriangle(t));	
		glEnd();
	}

	/**
	 * Renders the platform under the landscape.
	 */
	private void renderPlatform() {
		// Front and back faces should be rendered
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		// Set platform colour
		Colour.setColour(Colour.PLATFORM);

		// Points denoting the vertices of the platform cube
		Point[][][] cube = new Point[2][2][2];

		// Generate cube vertices
		for (int iZ = 0 ; iZ < 2 ; iZ++) {
			float z = iZ == 0 ? World.BASE_MIN_Z : World.BASE_MAX_Z;

			for (int iY = 0 ; iY < 2 ; iY++) {
				float y = iY == 0 ? World.MIN_Y : World.MAX_Y;

				for (int iX = 0 ; iX < 2 ; iX++) {
					float x = iX == 0 ? World.MIN_X : World.MAX_X;

					cube[iZ][iY][iX] = new Point(x, y, z);
				}
			}
		}

		// Render platform
		glBegin(GL_QUADS);
			// Top
			//addVertex(cube[1][0][0]); addVertex(cube[1][1][0]); addVertex(cube[1][1][1]); addVertex(cube[1][0][1]);

			// X Constant
			addVertex(cube[0][0][0]); addVertex(cube[0][1][0]); addVertex(cube[1][1][0]); addVertex(cube[1][0][0]);
			addVertex(cube[0][0][1]); addVertex(cube[1][0][1]); addVertex(cube[1][1][1]); addVertex(cube[0][1][1]);

			// Y Constant
			addVertex(cube[0][0][0]); addVertex(cube[1][0][0]); addVertex(cube[1][0][1]); addVertex(cube[0][0][1]);
			addVertex(cube[0][1][0]); addVertex(cube[0][1][1]); addVertex(cube[1][1][1]); addVertex(cube[1][1][0]);

			// Bottom
			addVertex(cube[0][0][0]); addVertex(cube[0][1][0]); addVertex(cube[0][1][1]); addVertex(cube[0][0][1]);
		glEnd();
	}

	/**
	 * Renders the given Triangle.
	 * 
	 * @param t Triangle to be rendered.
	 */
	private void renderTriangle(Triangle t) {
		float[] colour = t.getColour();
		Point[] points = t.getPoints();

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
	private void rotateAxis(char axis, float value) {
		float x = 0f, y = 0f, z = 0f;

		if (axis == 'x' || axis == 'X') x = 1f;
		else if (axis == 'y' || axis == 'Y') y = 1f;
		else if (axis == 'z' || axis == 'Z') z = 1f;

		glRotatef(value, x, y, z);
	}
	
	/*void renderCube() {
    glBegin(GL_QUADS);
        glColor3f(   0.0f,  0.0f,  0.2f );
        glVertex3f(  0.5f, -0.5f, -0.5f );
        glVertex3f( -0.5f, -0.5f, -0.5f );
        glVertex3f( -0.5f,  0.5f, -0.5f );
        glVertex3f(  0.5f,  0.5f, -0.5f );
        glColor3f(   0.0f,  0.0f,  1.0f );
        glVertex3f(  0.5f, -0.5f,  0.5f );
        glVertex3f(  0.5f,  0.5f,  0.5f );
        glVertex3f( -0.5f,  0.5f,  0.5f );
        glVertex3f( -0.5f, -0.5f,  0.5f );
        glColor3f(   1.0f,  0.0f,  0.0f );
        glVertex3f(  0.5f, -0.5f, -0.5f );
        glVertex3f(  0.5f,  0.5f, -0.5f );
        glVertex3f(  0.5f,  0.5f,  0.5f );
        glVertex3f(  0.5f, -0.5f,  0.5f );
        glColor3f(   0.2f,  0.0f,  0.0f );
        glVertex3f( -0.5f, -0.5f,  0.5f );
        glVertex3f( -0.5f,  0.5f,  0.5f );
        glVertex3f( -0.5f,  0.5f, -0.5f );
        glVertex3f( -0.5f, -0.5f, -0.5f );
        glColor3f(   0.0f,  1.0f,  0.0f );
        glVertex3f(  0.5f,  0.5f,  0.5f );
        glVertex3f(  0.5f,  0.5f, -0.5f );
        glVertex3f( -0.5f,  0.5f, -0.5f );
        glVertex3f( -0.5f,  0.5f,  0.5f );
        glColor3f(   0.0f,  0.2f,  0.0f );
        glVertex3f(  0.5f, -0.5f, -0.5f );
        glVertex3f(  0.5f, -0.5f,  0.5f );
        glVertex3f( -0.5f, -0.5f,  0.5f );
        glVertex3f( -0.5f, -0.5f, -0.5f );
    glEnd();
}*/

}