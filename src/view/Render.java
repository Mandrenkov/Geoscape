package view;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import core.*;
import model.*;

/**
 * @author Mikhail Andrenkov
 * @since February 22, 2017
 * @version 1.0
 *
 * <p>The <i>Render</i> class is responsible for rendering the landscape.</p>
 */
public class Render {

	/**
	 * Amount to increment z-axis angle every frame.
	 */
	private static final float Z_ROTATE_DELTA = -0.30f; // -0.30f

	/**
	 * Calls the top-level feature rendering functions.
	 */
	public void run(World world) {
		if (Top.DEBUG) {
			renderAxes();
		}

		renderPlatform();
		
		for (Grid grid : world.getLandscape()) {
			renderGrid(grid);
		}
		
		for (LightSource light : world.getLights()) {
			renderLightSource(light);
		}
			
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
			grid.getTriangles().forEach(t -> renderTerrainTriangle(t));
		glEnd();
	}
	
	private void renderSphere(Sphere sphere) {
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		//glDisable(GL_CULL_FACE);
		
		// Render triangles
		glBegin(GL_TRIANGLES);
			ArrayList<Triangle> triangles = sphere.getTriangles();
			for (int t = 0 ; t < triangles.size() ; t++) {
				float clr = (float) (t + 1)/triangles.size();
				//clr = (float) (Math.random()*0.2 + 0.8);
				//clr = 1f;
				//renderTriangle(triangles.get(t).getPoints(), new float[]{1f,1f,0f});
				renderTriangle(triangles.get(t).getPoints(), new float[]{clr, clr, clr});
			}
		glEnd();
	}
	
	private void renderLightSource(LightSource light) {
		renderSphere(light.getSphere());
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
			float z = iZ == 0 ? World.PLATFORM_MIN_Z : World.PLATFORM_MAX_Z;

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
	 * Renders the given TerrainTriangle.
	 *
	 * @param t TerrainTriangle to be rendered.
	 */
	private void renderTerrainTriangle(TerrainTriangle t) {
		renderTriangle(t.getPoints(), t.getColour());
	}
	
	/**
	 * Renders the given Triangle with the specified colour.
	 *
	 * @param points Points of the Triangle to be rendered.
	 * @param colour Colour to be applied to the Triangle.
	 */
	private void renderTriangle(Point[] points, float[] colour) {
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