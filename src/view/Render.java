package view;

import static org.lwjgl.opengl.GL11.*;

import core.*;
import model.*;

/**
 * @author Mikhail Andrenkov
 * @since January 23, 2017
 * @version 1.0
 *
 * <p>Member declarations and definitions for the <b>Render</b> class.</p>
 */ 
public class Render {

	private static DataManager data;

	public void run() {
		glDisable(GL_CULL_FACE);
		Render.data = Top.data;

		//renderAxes();
		renderBase();
		for (Terrain terrain : data.landscape) {
			renderGrid(terrain.getGrid());
		}
		//renderCube();

		rotateAxis('Z', -0.2f);
	}

	private void addVertex(Point p) {
		glVertex3f(p.getX(), p.getY(), p.getZ());
	}

	private void renderAxes() {
		float axisDistance = 1.1f;

		glLineWidth(2);
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

		glBegin(GL_LINES);
			glColor3f(1.0f, 0.0f, 0.0f);
			glVertex3f(-0f, 0f, 0f);
			glVertex3f(axisDistance, 0f, 0f);

			glColor3f(0.0f, 1.0f, 0.0f);
			glVertex3f(0f, -0f, 0f);
			glVertex3f(0f, axisDistance, 0f);

			glColor3f(0f, 0f, 1f);
			glVertex3f(0f, 0f, -0f);
			glVertex3f(0f, 0f, axisDistance);

		glEnd();

		glFlush();
	}

	private void renderBase() {
		//glDisable(GL_CULL_FACE);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		//glCullFace(GL_BACK);

		glColor3f(data.colourBase[0], data.colourBase[1], data.colourBase[2]);

		Point[][][] cube = new Point[2][2][2];
		int cubeIndex = 0;

		for (int iZ = 0 ; iZ < 2 ; iZ++) {
			float z = iZ == 0 ? data.BASE_MIN_Z : data.BASE_MAX_Z;

			for (int iY = 0 ; iY < 2 ; iY++) {
				float y = iY == 0 ? data.MIN_Y : data.MAX_Y;

				for (int iX = 0 ; iX < 2 ; iX++) {
					float x = iX == 0 ? data.MIN_X : data.MAX_X;

					cube[iZ][iY][iX] = new Point(x, y, z);
				}
			}
		}

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
			// addVertex(cube[0][0][0]); addVertex(cube[0][0][1]); addVertex(cube[0][1][1]); addVertex(cube[0][1][0]);
			addVertex(cube[0][0][0]); addVertex(cube[0][1][0]); addVertex(cube[0][1][1]); addVertex(cube[0][0][1]);
		glEnd();

		//glEnable(GL_CULL_FACE);
	}

	private void renderGrid(Grid grid) {
		glLineWidth(1);
		glPolygonMode(GL_FRONT, GL_FILL);
		glCullFace(GL_BACK);


		glBegin(GL_TRIANGLES);

		for (Triangle t : grid.getTriangles()) {
			float[] colour = t.getColour();
			Point[] points = t.getPoints();

			glColor3f(colour[0], colour[1], colour[2]);

			addVertex(points[0]);
			addVertex(points[1]);
			addVertex(points[2]);
		}

		glEnd();
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
    }Z*/

	/*
	private void renderWater() {
		glPolygonMode(GL_FRONT, GL_FILL);

		glBegin(GL_QUADS);
			glColor3f (data.colourWater[0], data.colourWater[1], data.colourWater[2]);
	        glVertex3f(-1f + data.PADDING_X, -1f + data.PADDING_Y, data.WATER_Z);
	        glVertex3f(-1f + data.PADDING_X,  1f - data.PADDING_Y, data.WATER_Z);
	        glVertex3f( 1f - data.PADDING_X,  1f - data.PADDING_Y, data.WATER_Z);
	        glVertex3f( 1f - data.PADDING_X, -1f + data.PADDING_Y, data.WATER_Z);
		glEnd();
	}*/

	private void rotateAxis(char axis, float value) {
		float x = 0f, y = 0f, z = 0f;

		if (axis == 'x' || axis == 'X') x = 1f;
		else if (axis == 'y' || axis == 'Y') y = 1f;
		else if (axis == 'z' || axis == 'Z') z = 1f;

		glRotatef(value, x, y, z);
	}

}