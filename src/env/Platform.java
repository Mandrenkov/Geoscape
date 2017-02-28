package env;

import static org.lwjgl.opengl.GL11.*;

import geo.Point;
import util.*;

public class Platform implements Drawable {
	
	public void draw() {
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
			Render.addVertex(cube[0][0][0]); Render.addVertex(cube[0][1][0]); Render.addVertex(cube[1][1][0]); Render.addVertex(cube[1][0][0]);
			Render.addVertex(cube[0][0][1]); Render.addVertex(cube[1][0][1]); Render.addVertex(cube[1][1][1]); Render.addVertex(cube[0][1][1]);

			// Y Constant
			Render.addVertex(cube[0][0][0]); Render.addVertex(cube[1][0][0]); Render.addVertex(cube[1][0][1]); Render.addVertex(cube[0][0][1]);
			Render.addVertex(cube[0][1][0]); Render.addVertex(cube[0][1][1]); Render.addVertex(cube[1][1][1]); Render.addVertex(cube[1][1][0]);

			// Bottom
			Render.addVertex(cube[0][0][0]); Render.addVertex(cube[0][1][0]); Render.addVertex(cube[0][1][1]); Render.addVertex(cube[0][0][1]);
		glEnd();
	}
}
